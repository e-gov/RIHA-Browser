package ee.ria.riha.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

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

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String subjectDn = (String) super.getPreAuthenticatedPrincipal(request);

        if (!hasText(subjectDn)) {
            return null;
        }

        log.debug("Extracting principal from subject DN: {}", subjectDn);
        try {
            for (Rdn rdn : new LdapName(subjectDn).getRdns()) {
                if (rdn.getType().equalsIgnoreCase(SERIAL_NUMBER)) {
                    String serialNumber = (String) rdn.getValue();
                    return new EstEIDPrincipal(serialNumber);
                }
            }

            throw new BadCredentialsException(
                    "Subject DN does not contain serial number needed for principal extraction");
        } catch (InvalidNameException e) {
            throw new BadCredentialsException("Could not retrieve RDNs from subject DN", e);
        }
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
