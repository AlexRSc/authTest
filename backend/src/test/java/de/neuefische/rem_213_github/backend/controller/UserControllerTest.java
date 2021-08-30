package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.NewPassword;
import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.config.JwtConfig;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private String url(){
        return "http://localhost:" + port + "/user";
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createNewUserAsAdmin(){
        // Given
        User userToAdd = User.builder()
                .name("Lisa")
                .role("user")
                .avatar("http://thispersondoesnotexist.com/image")
                .build();

        // When
        HttpEntity<User> httpEntity = new HttpEntity<>(userToAdd, authorizedHeader("Frank", "admin"));
        ResponseEntity<User> response = restTemplate.exchange(url(), HttpMethod.POST, httpEntity, User.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        User actualUser = response.getBody();
        assertThat(actualUser.getName(), is("Lisa"));
        assertThat(actualUser.getAvatar(), is("http://thispersondoesnotexist.com/image"));
        assertThat(actualUser.getRole(), is("user"));
        assertThat(actualUser.getPassword(), is(notNullValue()));
    }

    @Test
    public void userCanNotCreateANewUser(){
        // Given
        User userToAdd = User.builder()
                .name("Lisa")
                .role("user")
                .avatar("http://thispersondoesnotexist.com/image")
                .build();

        // When
        HttpEntity<User> httpEntity = new HttpEntity<>(userToAdd, authorizedHeader("Frank", "user"));
        ResponseEntity<User> response = restTemplate.exchange(url(), HttpMethod.POST, httpEntity, User.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void userCanChangeHerPassword(){
        // Given
        userRepository.save(UserEntity.builder()
                        .name("Gwen")
                        .password("old-password")
                        .avatarUrl("http://thispersondoesnotexist.com/image")
                        .role("user")
                .build());

        NewPassword newPassword = new NewPassword("new-password");

        // When
        HttpEntity<NewPassword> httpEntity = new HttpEntity<>(newPassword, authorizedHeader("Gwen","user"));
        ResponseEntity<User> response = restTemplate
                .exchange(url()+"/password", HttpMethod.PUT, httpEntity, User.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        User actualUser = response.getBody();
        assertThat(actualUser.getName(), is("Gwen"));
        assertThat(actualUser.getAvatar(), is("http://thispersondoesnotexist.com/image"));
        assertThat(actualUser.getRole(), is("user"));
        assertThat(actualUser.getPassword(), is("new-password"));

        UserEntity foundUserEntity = userRepository.findByName("Gwen").orElseThrow();
        assertThat(foundUserEntity.getPassword(), is(not("old-password")));
    }

    private HttpHeaders authorizedHeader(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        return headers;
    }
}