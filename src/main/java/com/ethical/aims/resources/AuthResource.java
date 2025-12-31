package com.ethical.aims.resources;

import com.ethical.aims.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);
    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body, HttpServletRequest req) {
        log.debug("In AuthResource()--login():::[{}]", body);
        return authService.login(
                body.get("companyCode"),
                body.get("username"),
                body.get("password"),
                req.getSession(true)
        );
    }

    @GetMapping("/me")
    public String me(HttpServletRequest req) {
        return authService.me(req.getSession(false));
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest req) {
        return authService.logout(req.getSession(false));
    }


}
