package de.neuefische.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class MongoUserController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public MongoUserController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/me1")
    public String getUserInfo1(Principal principal) {
        if (principal == null) {
            return "anonymousUser";
        }
        return principal.getName();
    }

    @GetMapping("/me2")
    public String getUserInfo2() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginData loginData){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.username(), loginData.password()));

        return jwtService.createToken(loginData.username());
    }
}
