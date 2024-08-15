package warranty.api.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import warranty.api.security.user.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils underTest;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private final String jwtSecret = "36763979244226452948404D635166546A576D5A7134743777217A25432A462D";
    private final int jwtExpirationMs = 1000 * 60 * 60; // 1 hour

    public static final String EMAIL = "roch.the.glock@gmail.com";


    @BeforeEach
    void setUp() {
        underTest = new JwtUtils(jwtSecret, jwtExpirationMs);
    }

    @Test
    void shouldGenerateJwtToken() {
        // given
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        // when
        String token = underTest.generateJwtTokenForUser(authentication);

        // then
        assertThat(token).isNotNull();
        assertThat(underTest.getEmailFromToken(token)).isEqualTo(EMAIL);
    }

    @Test
    void shouldValidateJwtToken() {
        // given
        String token = generateTestToken(EMAIL);

        // when
        boolean isValid = underTest.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldInvalidateExpiredJwtToken() {
        // given
        String expiredToken = generateExpiredTestToken(EMAIL);

        // when
        boolean isValid = underTest.validateToken(expiredToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldExtractEmailFromToken() {
        // given
        String token = generateTestToken(EMAIL);

        // when
        String email = underTest.getEmailFromToken(token);

        // then
        assertThat(email).isEqualTo(EMAIL);
    }

    private String generateTestToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredTestToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() - 1000); // Expired 1 second ago

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}