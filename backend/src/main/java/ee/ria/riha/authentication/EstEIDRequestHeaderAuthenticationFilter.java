package ee.ria.riha.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.naming.ldap.Rdn;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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
public class EstEIDRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    private static final String PEM_CERTIFICATE_HEADER = "-----BEGIN CERTIFICATE-----";
    private static final String PEM_CERTIFICATE_FOOTER = "-----END CERTIFICATE-----";

    private static final String NON_BASE_64_CHARACTER = "[^A-Za-z0-9+/=]";

    private static final String SERIAL_NUMBER = "serialnumber";
    private static final String GIVEN_NAME = "gn";
    private static final String SURNAME = "sn";

    public EstEIDRequestHeaderAuthenticationFilter() {
        setExceptionIfHeaderMissing(false);
        setPrincipalRequestHeader("SSL_CLIENT_S_DN");
        setCredentialsRequestHeader("SSL_CLIENT_CERT");
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String subjectDn = (String) super.getPreAuthenticatedPrincipal(request);

        if (!hasText(subjectDn)) {
            return null;
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

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String pem = (String) super.getPreAuthenticatedCredentials(request);

        if (!hasText(pem)) {
            return null;
        }

        log.debug("Extracting credentials certificate from: {}", pem);
        byte[] certificate = getCertificateBytes(pem);
        try (ByteArrayInputStream certStream = new ByteArrayInputStream(certificate)) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return certFactory.generateCertificate(certStream);
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

}
