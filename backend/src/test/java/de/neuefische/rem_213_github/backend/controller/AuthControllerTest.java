package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.AccessToken;
import de.neuefische.rem_213_github.backend.api.Credentials;
import de.neuefische.rem_213_github.backend.config.JwtConfig;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static de.neuefische.rem_213_github.backend.controller.AuthController.ACCESS_TOKEN_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    private String url() {
        return "http://localhost:" + port + ACCESS_TOKEN_URL;
    }

    @AfterEach
    public void clearDb() {
        userRepository.deleteAll();
    }

    @Test
    public void successfulLogin() {
        // Given
        String username = "bill";
        String password = "12345";
        String role = "user";
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(
                UserEntity.builder()
                        .name(username)
                        .role(role)
                        .password(hashedPassword).build()
        );
        Credentials credentials = Credentials.builder()
                .username(username)
                .password(password).build();

        // When
        ResponseEntity<AccessToken> response = restTemplate
                .postForEntity(url(), credentials, AccessToken.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertNotNull(response.getBody());

        String token = response.getBody().getToken();
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token).getBody();
        assertThat(claims.getSubject(), is(username));
        assertThat(claims.get("role", String.class), is(role));
    }

    @Test
    public void badCredentials() {
        // Given
        Credentials credentials = Credentials.builder()
                .username("Lisa")
                .password("password").build();

        // When
        HttpEntity<Credentials> httpEntity = new HttpEntity<>(credentials, getHttpHeaders());
        ResponseEntity<AccessToken> response = restTemplate
                .postForEntity(url(), httpEntity, AccessToken.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody(), nullValue());
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    public void noCredentials() {
        // Given
        Credentials credentials = new Credentials();

        // When
        HttpEntity<Credentials> httpEntity = new HttpEntity<>(credentials, getHttpHeaders());
        ResponseEntity<AccessToken> response = restTemplate
                .postForEntity(url(), httpEntity, AccessToken.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}
