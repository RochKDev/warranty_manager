package warranty.api.services.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import warranty.api.exception.UserEmailUnavailableException;
import warranty.api.model.User;
import warranty.api.model.dto.LoginRequestDto;
import warranty.api.model.dto.UserDto;
import warranty.api.model.responses.JwtResponseDto;
import warranty.api.repository.UserRepository;
import warranty.api.security.jwt.JwtUtils;
import warranty.api.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                           UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);

        return new JwtResponseDto(jwt);
    }

    @Override
    public void registerUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.email()).isPresent()) {
            log.error("User with email {} already exists", userDto.email());
            throw new UserEmailUnavailableException("User with email " + userDto.email() + " already exists");
        }

        User user = User.builder()
                .name(userDto.name())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .build();

        userRepository.save(user);
    }
}
