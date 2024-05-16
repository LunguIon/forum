package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@Tag(name = "OAuth controller")
public class OauthController {
    @Operation(summary = "Google Authentication")
    @GetMapping("/google")
    public Object googleAuthentication(Authentication authentication) {
        return authentication.getPrincipal();
    }
}
