package md.forum.forum.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google")
public class OauthController {
    @GetMapping
    public Object googleAuthentication(Authentication authentication){
        return authentication.getPrincipal();
    }
}
