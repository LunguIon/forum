package md.forum.forum.controllers;

import md.forum.forum.dto.JwtResponseDTO;
import md.forum.forum.dto.LoginUserDto;
import md.forum.forum.dto.RefreshTokenRequestDTO;
import md.forum.forum.dto.RegisterUserDto;
import md.forum.forum.security.model.RefreshToken;
import md.forum.forum.models.User;
import md.forum.forum.security.service.AuthenticationService;
import md.forum.forum.security.service.JwtService;
import md.forum.forum.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody LoginUserDto loginUserDto) {
        return authenticationService.authenticateAndGetToken(loginUserDto);
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return authenticationService.refreshToken(refreshTokenRequestDTO);
    }

}
