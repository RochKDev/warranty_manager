package warranty.api.services;

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
import warranty.api.services.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    AuthServiceImpl underTest;

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

    @BeforeEach
    void setUp() {
        underTest = new AuthServiceImpl(authenticationManager, jwtUtils, userRepository, passwordEncoder);
    }

    @Test
    void shouldAuthenticateUser() {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@example.com", "password");

        // Mock the authentication object
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        // Mock the jwt token
        when(jwtUtils.generateJwtTokenForUser(authentication)).thenReturn("jwt-token");

        // When - method under test
        JwtResponseDto jwtResponse = underTest.authenticateUser(loginRequestDto);

        // Then
        // Verify that the authentication manager was called with the correct arguments
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        // Verify that the jwt token was generated
        verify(jwtUtils).generateJwtTokenForUser(authentication);
        // Verify that the jwt token was returned
        assertThat(jwtResponse.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void shouldRegisterUser() {
        // Given
        UserDto userDto = new UserDto("John Doe", "john.doe@example.com", "password");

        // Mock the user repository to return false when checking if the email is taken
        when(userRepository.existsByEmail(userDto.email())).thenReturn(false);
        // Mock the password encoder to return the "encoded password"
        when(passwordEncoder.encode(userDto.password())).thenReturn("encoded-password");

        // When - method under test
        underTest.registerUser(userDto);

        // Then
        // Verify that the user was saved
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(userDto.name());
        assertThat(capturedUser.getEmail()).isEqualTo(userDto.email());
        assertThat(capturedUser.getPassword()).isEqualTo("encoded-password");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsTaken() {
        // Given
        UserDto userDto = new UserDto("John Doe", "john.doe@example.com", "password");

        // Mock the user repository to return true when checking if the email is taken
        when(userRepository.existsByEmail(userDto.email())).thenReturn(true);

        // when & then - method under test
        // Verify that the UserEmailUnavailableException is thrown when the email is taken
        assertThatThrownBy(() -> underTest.registerUser(userDto))
                .isInstanceOf(UserEmailUnavailableException.class)
                .hasMessageContaining("User with email " + userDto.email() + " already exists");

        verify(userRepository, never()).save(any());
    }
}