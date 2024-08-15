package warranty.api.services;

import warranty.api.model.dto.LoginRequestDto;
import warranty.api.model.dto.UserDto;
import warranty.api.model.responses.JwtResponseDto;

public interface AuthService {

    void registerUser(UserDto userDto);

    JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto);
}
