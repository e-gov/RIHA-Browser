package ee.ria.riha.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.naming.ldap.Rdn;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.ldap.support.LdapUtils.newLdapName;
import static org.springframework.util.StringUtils.hasText;

/**
 * Pre-authenticated filter which obtains pre-authenticated user certificate subject and PEM encoded certificate from
 * request headers. Produces {@link EstEIDPrincipal} from certificate subject and {@link
 * java.security.cert.X509Certificate} as credential.
 *
 * @author Valentin Suhnjov
 */
@Slf4j
public class EstEIDRequestHeaderAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final String PEM_CERTIFICATE_HEADER = "-----BEGIN CERTIFICATE-----";
    private static final String PEM_CERTIFICATE_FOOTER = "-----END CERTIFICATE-----";

    private static final String NON_BASE_64_CHARACTER = "[^A-Za-z0-9+/=]";

    private static final String SERIAL_NUMBER = "serialnumber";
    private static final String GIVEN_NAME = "gn";
    private static final String SURNAME = "sn";

    private String principalHeader = "SSLCLIENTSDN";
    private String credentialsHeader = "SSLCLIENTCERT";
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/login/esteid", "GET");

    public EstEIDRequestHeaderAuthenticationFilter() {
        setContinueFilterChainOnUnsuccessfulAuthentication(false);
    }

    /**
     * Try to authenticate pre-authenticated EstEID user. In case of successful authentication, filter chain execution
     * will stop resulting in status 200 response. In case of exception, exception will be propagated resulting in error
     * response.
     *
     * @param req   servlet request
     * @param res   servlet response
     * @param chain filter chain
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        if (requestMatcher.matches(((HttpServletRequest) req))) {
            if (log.isDebugEnabled()) {
                log.debug("Checking EstEID pre-authentication");
            }
            // Filtering ends here regardless of authentication outcome. Response status indicates authentication result.
            super.doFilter(req, res, (request, response) -> { /* Empty filter chain */ });
        } else {
            chain.doFilter(req, res);
        }
    }

    /**
     * Extracts pre-authenticated principal from request headers.
     *
     * @param request servlet request
     * @return extracted {@link EstEIDPrincipal}
     * @throws BadCredentialsException in case principal header is null or empty, or when serial number extraction
     *                                 fails
     */
    @Override
    protected EstEIDPrincipal getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String subjectDn = request.getHeader(principalHeader);

        if (!hasText(subjectDn)) {
            throw new BadCredentialsException("Header does not contain pre-authenticated principal");
        }

        log.debug("Extracting principal from subject DN: {}", subjectDn);

        Map<String, String> principalParts = new HashMap<>();
        for (Rdn rdn : newLdapName(subjectDn).getRdns()) {
            principalParts.put(rdn.getType().toLowerCase(), ((String) rdn.getValue()));
        }

        if (!principalParts.containsKey(SERIAL_NUMBER)) {
            throw new BadCredentialsException(
                    "Subject DN does not contain serial number needed for principal extraction");
        }

        EstEIDPrincipal principal = new EstEIDPrincipal(principalParts.get(SERIAL_NUMBER));
        principal.setGivenName(principalParts.get(GIVEN_NAME));
        principal.setSurname(principalParts.get(SURNAME));

        return principal;
    }

    /**
     * Extracts pre-authenticated credentials from request headers assuming that it is PEM encoded {@link
     * X509Certificate}.
     *
     * @param request servlet request
     * @return instance of {@link java.security.cert.X509Certificate}
     * @throws BadCredentialsException in case credentials header is null or empty, or certificate could not be
     *                                 extracted
     */
    @Override
    protected X509Certificate getPreAuthenticatedCredentials(HttpServletRequest request) {
        String pem = request.getHeader(credentialsHeader);

        if (!hasText(pem)) {
            throw new BadCredentialsException("Header does not contain pre-authenticated credentials");
        }

        if (log.isDebugEnabled()) {
            log.debug("Extracting credentials certificate from: {}", pem);
        }

        byte[] certificate = getCertificateBytes(pem);
        try (ByteArrayInputStream certStream = new ByteArrayInputStream(certificate)) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certFactory.generateCertificate(certStream);
        } catch (IOException | CertificateException e) {
            throw new BadCredentialsException("Could not generate certificate from certificate data", e);
        }
    }

    /**
     * Normalize certificate and retrieve actual certificate bytes. Received PEM encoded certificate can have incorrect
     * formatting caused by load balancer/proxy processing. Try to retrieve raw certificate bytes given that it is
     * encoded with base64 encoding.
     *
     * @param pem PEM encoded certificate
     * @return certificate actual bytes
     */
    private byte[] getCertificateBytes(String pem) {
        String binary = pem
                .replace(PEM_CERTIFICATE_HEADER, "")
                .replace(PEM_CERTIFICATE_FOOTER, "");

        binary = binary.replaceAll(NON_BASE_64_CHARACTER, "");

        return Base64.getDecoder().decode(binary);
    }

    public void setRequestMatcher(RequestMatcher requestMatcher) {
        Assert.notNull(requestMatcher, "requestMatcher should not be null");
        this.requestMatcher = requestMatcher;
    }

    public String getPrincipalHeader() {
        return principalHeader;
    }

    public void setPrincipalHeader(String principalHeader) {
        Assert.hasText(principalHeader, "principalHeader must not be null or empty");
        this.principalHeader = principalHeader;
    }

    public String getCredentialsHeader() {
        return credentialsHeader;
    }

    public void setCredentialsHeader(String credentialsHeader) {
        Assert.hasText(credentialsHeader, "credentialsHeader must not be null or empty");
        this.credentialsHeader = credentialsHeader;
    }
}
