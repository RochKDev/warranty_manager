package warranty.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.dto.LoginRequestDto;
import warranty.api.model.dto.UserDto;
import warranty.api.model.responses.JwtResponseDto;
import warranty.api.services.AuthService;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(authService.authenticateUser(loginRequestDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody UserDto userDto) {
        authService.registerUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
