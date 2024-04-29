package md.forum.forum.controllers;

import md.forum.forum.dto.LoginUserDto;
import md.forum.forum.dto.RegisterUserDto;
import md.forum.forum.models.User;
import md.forum.forum.responses.LoginResponse;
import md.forum.forum.services.AuthenticationService;
import md.forum.forum.services.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private static final Logger logger =  LogManager.getLogger(AuthenticationController.class);
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        logger.info("register User: {}", registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        logger.info("login User: {}", loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        logger.info("JWT Token: {}", jwtToken);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        logger.info("Login Response: {}", loginResponse);
        return ResponseEntity.ok(loginResponse);
    }
}
