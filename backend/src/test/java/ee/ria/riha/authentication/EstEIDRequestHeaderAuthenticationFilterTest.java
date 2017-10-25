package ee.ria.riha.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.ServletException;
import java.io.IOException;
import java.security.cert.X509Certificate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class EstEIDRequestHeaderAuthenticationFilterTest {

    private static final String MARY_ANN_SERIAL_NUMBER = "11412090004";
    private static final String MARY_ANN_GIVEN_NAME = "MARY ÄNN";
    private static final String MARY_ANN_SURNAME = "O’CONNEŽ-ŠUSLIK";
    private static final String MARY_ANN_SUBJECT_DN = "SERIALNUMBER=" + MARY_ANN_SERIAL_NUMBER +
            ",GN=" + MARY_ANN_GIVEN_NAME +
            ",SN=" + MARY_ANN_SURNAME;
    private static final String MARY_ANN_CERT = "MIIEmTCCA4GgAwIBAgIQAPVvrDwlATtUhxSc97Dd6zANBgkqhkiG9w0BAQsFADBs" +
            "MQswCQYDVQQGEwJFRTEiMCAGA1UECgwZQVMgU2VydGlmaXRzZWVyaW1pc2tlc2t1" +
            "czEfMB0GA1UEAwwWVEVTVCBvZiBFU1RFSUQtU0sgMjAxMTEYMBYGCSqGSIb3DQEJ" +
            "ARYJcGtpQHNrLmVlMB4XDTE0MTIwOTE1MjYyMFoXDTE3MTIwOTIxNTk1OVowgb8x" +
            "CzAJBgNVBAYTAkVFMRswGQYDVQQKDBJFU1RFSUQgKE1PQklJTC1JRCkxFzAVBgNV" +
            "BAsMDmF1dGhlbnRpY2F0aW9uMTIwMAYDVQQDDClP4oCZQ09OTkXFvS3FoFVTTElL" +
            "LE1BUlkgw4ROTiwxMTQxMjA5MDAwNDEcMBoGA1UEBAwTT+KAmUNPTk5Fxb0txaBV" +
            "U0xJSzESMBAGA1UEKgwJTUFSWSDDhE5OMRQwEgYDVQQFEwsxMTQxMjA5MDAwNDBZ" +
            "MBMGByqGSM49AgEGCCqGSM49AwEHA0IABHYleZg39CkgQGU8z8b8ehctBEnaGldu" +
            "cij6eTETeOj2LpEwLedMS1pCfNEZAJjDwAZ2DJMBgB05QHrrvzersUKjggGsMIIB" +
            "qDAJBgNVHRMEAjAAMA4GA1UdDwEB/wQEAwIEsDCBmQYDVR0gBIGRMIGOMIGLBgor" +
            "BgEEAc4fAwMBMH0wWAYIKwYBBQUHAgIwTB5KAEEAaQBuAHUAbAB0ACAAdABlAHMA" +
            "dABpAG0AaQBzAGUAawBzAC4AIABPAG4AbAB5ACAAZgBvAHIAIAB0AGUAcwB0AGkA" +
            "bgBnAC4wIQYIKwYBBQUHAgEWFWh0dHA6Ly93d3cuc2suZWUvY3BzLzAsBgNVHREE" +
            "JTAjgSFtYXJ5LmFubi5vLmNvbm5lei1zdXNsaWtAZWVzdGkuZWUwHQYDVR0OBBYE" +
            "FJ3eqIvcJ/uIUPi7T7xHWlzOZM/oMCAGA1UdJQEB/wQWMBQGCCsGAQUFBwMCBggr" +
            "BgEFBQcDBDAYBggrBgEFBQcBAwQMMAowCAYGBACORgEBMB8GA1UdIwQYMBaAFEG2" +
            "/sWxsbRTE4z6+mLQNG1tIjQKMEUGA1UdHwQ+MDwwOqA4oDaGNGh0dHA6Ly93d3cu" +
            "c2suZWUvcmVwb3NpdG9yeS9jcmxzL3Rlc3RfZXN0ZWlkMjAxMS5jcmwwDQYJKoZI" +
            "hvcNAQELBQADggEBALxS9kBbIvUKLKbbxx8oCkzjx3Y30DsnkFYGxLklx5x4Uh0P" +
            "q6nieuiwiYKNgXonOksz+NJ9hOepGdFwCGMdm2getYIGbOv1dOswJVq+ygABGj0w" +
            "vCVT1CO530cXL3gY4aXOmFsGnpqkr0r4pyaMVVlovgjEnFeadw/0d5nT9EfptJNx" +
            "kfBq3WWqaslPRZAhutZzcionO83nugmfEYvTeucvF+odpj12HARZK79Iw74L1C3r" +
            "HTNDYki7wGUzc4hU+LuTldSX4lTMI3mq4K0w/8VaE5XKcU6YJP0h+pE44d9Ay5yL" +
            "fgitMjnjgMnQHZNyvdoA3yXV3QYYSKSAqUYQy6w=";

    private MockHttpServletRequest request = createRequest(MARY_ANN_SUBJECT_DN, MARY_ANN_CERT);

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private EstEIDRequestHeaderAuthenticationFilter filter = new EstEIDRequestHeaderAuthenticationFilter();

    private MockHttpServletRequest createRequest(String subjectDn, String cert) {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.request(HttpMethod.GET, "/login/esteid");

        if (hasText(subjectDn)) {
            request.header("SSLCLIENTSDN", subjectDn);
        }

        if (hasText(cert)) {
            request.header("SSLCLIENTCERT", cert);
        }

        return request.buildRequest(new MockServletContext());
    }

    @Test(expected = BadCredentialsException.class)
    public void failsWhenNoPrincipalInHeaders() throws IOException, ServletException {
        filter.getPreAuthenticatedPrincipal(createRequest(null, MARY_ANN_CERT));
    }

    @Test(expected = BadCredentialsException.class)
    public void failsWhenNoCredentialsInHeaders() throws IOException, ServletException {
        filter.getPreAuthenticatedCredentials(createRequest(MARY_ANN_SUBJECT_DN, null));
    }

    @Test
    public void extractsSerialNumberFromPrincipalSubjectDn() {
        EstEIDPrincipal principal = filter.getPreAuthenticatedPrincipal(request);
        assertThat(principal.getSerialNumber(), is(equalTo(MARY_ANN_SERIAL_NUMBER)));
    }

    @Test
    public void extractsGivenNameFromPrincipalSubjectDn() {
        EstEIDPrincipal principal = filter.getPreAuthenticatedPrincipal(request);
        assertThat(principal.getGivenName(), is(equalTo(MARY_ANN_GIVEN_NAME)));
    }

    @Test
    public void extractsSurnameFromPrincipalSubjectDn() {
        EstEIDPrincipal principal = filter.getPreAuthenticatedPrincipal(request);
        assertThat(principal.getSurname(), is(equalTo(MARY_ANN_SURNAME)));
    }

    @Test(expected = BadCredentialsException.class)
    public void failsWhenNoSerialNumberInSubjectDn() {
        filter.getPreAuthenticatedPrincipal(createRequest("sn=Doe,dn=Joe", MARY_ANN_CERT));
    }

    @Test
    public void extractsCertificateFromCredentials() {
        X509Certificate certificate = filter.getPreAuthenticatedCredentials(request);
        assertThat(certificate, is(notNullValue()));
    }
}