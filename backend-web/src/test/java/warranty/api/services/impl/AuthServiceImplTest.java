package warranty.api.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import warranty.api.exception.UserEmailUnavailableException;
import warranty.api.model.User;
import warranty.api.model.dto.LoginRequestDto;
import warranty.api.model.dto.UserDto;
import warranty.api.model.responses.JwtResponseDto;
import warranty.api.repository.UserRepository;
import warranty.api.security.jwt.JwtUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AuthServiceImpl} class.
 * This class tests the behavior of methods for authenticating and registering users.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private AuthServiceImpl underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    /**
     * Set up the test environment by initializing the service with mocked dependencies.
     */
    @BeforeEach
    void setUp() {
        underTest = new AuthServiceImpl(authenticationManager, jwtUtils, userRepository, passwordEncoder);
    }

    /**
     * Test the successful authentication of a user.
     * Ensures the authentication manager and JWT utils are called with the correct arguments,
     * and a JWT token is returned.
     */
    @Test
    void shouldAuthenticateUser() {
        // Given: A login request with valid credentials.
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@example.com", "password");

        // Mock the authentication object and JWT token generation.
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtTokenForUser(authentication)).thenReturn("jwt-token");

        // When: The authenticateUser method is called. (method under test)
        JwtResponseDto jwtResponse = underTest.authenticateUser(loginRequestDto);

        // Then: Verify that the authentication manager was called with the correct arguments,
        // and the JWT token was generated and returned.
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtTokenForUser(authentication);
        assertThat(jwtResponse.getToken()).isEqualTo("jwt-token");
    }

    /**
     * Test the successful registration of a new user.
     * Ensures the user repository's save method is called with the correct arguments,
     * and the user's password is encoded.
     */
    @Test
    void shouldRegisterUser() {
        // Given: A user DTO with valid details.
        UserDto userDto = new UserDto("John Doe", "john.doe@example.com", "password");

        // Mock the user repository and password encoder.
        when(userRepository.existsByEmail(userDto.email())).thenReturn(false);
        when(passwordEncoder.encode(userDto.password())).thenReturn("encoded-password");

        // When: The registerUser method is called. (method under test)
        underTest.registerUser(userDto);

        // Then: Verify that the user was saved with the correct details.
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(userDto.name());
        assertThat(capturedUser.getEmail()).isEqualTo(userDto.email());
        assertThat(capturedUser.getPassword()).isEqualTo("encoded-password");
    }

    /**
     * Test that a {@link UserEmailUnavailableException} is thrown when trying to register
     * a user with an email that is already taken.
     */
    @Test
    void shouldThrowExceptionWhenEmailIsTaken() {
        // Given: A user DTO with an email that is already taken.
        UserDto userDto = new UserDto("John Doe", "john.doe@example.com", "password");

        // Mock the user repository to return true when checking if the email is taken.
        when(userRepository.existsByEmail(userDto.email())).thenReturn(true);

        // When / Then: Assert that registering a user with a taken email throws an exception.
        assertThatThrownBy(() -> underTest.registerUser(userDto))
                .isInstanceOf(UserEmailUnavailableException.class)
                .hasMessageContaining("User with email " + userDto.email() + " already exists");

        // Verify that the user repository's save method is never called.
        verify(userRepository, never()).save(any());
    }
}
