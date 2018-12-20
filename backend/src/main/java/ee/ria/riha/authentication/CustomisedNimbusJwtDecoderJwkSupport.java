package ee.ria.riha.authentication;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a customized copy of {@code NimbusJwtDecoderJwkSupport} JwtDecoder.
 *
 * Given the fact that {@code NimbusJwtDecoderJwkSupport} is a final class, the source code was copy-pasted into the project.
 *
 *
 * As TARA does not seem to fully comply with the OAuth2 spec, a custom JWKMatcher is needed to ignore the differences in the keyID field of the JWKS certificate.
 *
 * This should be removed when the support for keyID is added to TARA.
 *
 */
public class CustomisedNimbusJwtDecoderJwkSupport implements JwtDecoder {

    private final URL jwkSetUrl;
    private final JWSAlgorithm jwsAlgorithm;
    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    /**
     * Constructs a {@code CustomisedNimbusJwtDecoderJwkSupport} using the provided parameters.
     *
     * @param jwkSetUrl the JSON Web Key (JWK) Set {@code URL}
     */
    public CustomisedNimbusJwtDecoderJwkSupport(String jwkSetUrl) {
        this(jwkSetUrl, JwsAlgorithms.RS256);
    }

    /**
     * Constructs a {@code CustomisedNimbusJwtDecoderJwkSupport} using the provided parameters.
     *
     * @param jwkSetUrl    the JSON Web Key (JWK) Set {@code URL}
     * @param jwsAlgorithm the JSON Web Algorithm (JWA) used for verifying the digital signatures
     */
    public CustomisedNimbusJwtDecoderJwkSupport(String jwkSetUrl, String jwsAlgorithm) {
        Assert.hasText(jwkSetUrl, "jwkSetUrl cannot be empty");
        Assert.hasText(jwsAlgorithm, "jwsAlgorithm cannot be empty");
        try {
            this.jwkSetUrl = new URL(jwkSetUrl);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid JWK Set URL " + jwkSetUrl + " : " + ex.getMessage(), ex);
        }
        this.jwsAlgorithm = JWSAlgorithm.parse(jwsAlgorithm);

        ResourceRetriever jwkSetRetriever = new DefaultResourceRetriever(30000, 30000);
        JWKSource jwkSource = new RemoteJWKSet(this.jwkSetUrl, jwkSetRetriever);
        JWSKeySelector<SecurityContext> jwsKeySelector =
                new JWSVerificationKeySelector(this.jwsAlgorithm, jwkSource) {
                    @Override
                    protected JWKMatcher createJWKMatcher(JWSHeader jwsHeader) {
                        return new JWKMatcher.Builder()
                                .keyType(KeyType.forAlgorithm(getExpectedJWSAlgorithm()))
                                // This was commented out intentionally.
                                // Please review the class level comment.
//                                .keyID(jwsHeader.getKeyID())
                                .keyUses(KeyUse.SIGNATURE, null)
                                .algorithms(getExpectedJWSAlgorithm(), null)
                                .x509CertSHA256Thumbprint(jwsHeader.getX509CertSHA256Thumbprint())
                                .build();
                    }
                };

        this.jwtProcessor = new DefaultJWTProcessor<>();
        this.jwtProcessor.setJWSKeySelector(jwsKeySelector);
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt;

        try {
            JWT parsedJwt = JWTParser.parse(token);

            // Verify the signature
            JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);

            Instant expiresAt = null;
            if (jwtClaimsSet.getExpirationTime() != null) {
                expiresAt = jwtClaimsSet.getExpirationTime().toInstant();
            }
            Instant issuedAt = null;
            if (jwtClaimsSet.getIssueTime() != null) {
                issuedAt = jwtClaimsSet.getIssueTime().toInstant();
            } else if (expiresAt != null) {
                // Default to expiresAt - 1 second
                issuedAt = Instant.from(expiresAt).minusSeconds(1);
            }

            Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());

            jwt = new Jwt(token, issuedAt, expiresAt, headers, jwtClaimsSet.getClaims());

        } catch (Exception ex) {
            throw new JwtException("An error occurred while attempting to decode the Jwt: " + ex.getMessage(), ex);
        }

        return jwt;
    }
}

