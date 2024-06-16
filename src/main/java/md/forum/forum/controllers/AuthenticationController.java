package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import md.forum.forum.dto.auth.JwtResponseDTO;
import md.forum.forum.dto.auth.LoginUserDto;
import md.forum.forum.dto.auth.RefreshTokenRequestDTO;
import md.forum.forum.dto.auth.RegisterUserDto;
import md.forum.forum.models.User;
import md.forum.forum.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication controller methods")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Operation(summary = "Register new account")
    @PostMapping("/signUp")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Login to account and get token")
    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody LoginUserDto loginUserDto) {
        return authenticationService.authenticateAndGetToken(loginUserDto);
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return authenticationService.refreshToken(refreshTokenRequestDTO);
    }
}
