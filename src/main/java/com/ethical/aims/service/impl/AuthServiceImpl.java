package com.ethical.aims.service.impl;

import com.ethical.aims.dao.AppUserDao;
import com.ethical.aims.entity.AppUser;
import com.ethical.aims.service.AuthService;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AppUserDao appUserDao, PasswordEncoder passwordEncoder) {
        this.appUserDao = appUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String companyCode, String username, String password,
                        HttpSession session) {

        JsonObject res = new JsonObject();
       log.debug("In login();;;;companyCode[{}], username[{}], password[{}]" +
                       " session[{}]", companyCode, username, password,
                session);
        String cc = (companyCode == null) ? "" : companyCode.trim();
        String un = (username == null) ? "" : username.trim();

        if (cc.isEmpty() || un.isEmpty() || password == null || password.isEmpty()) {
            res.addProperty("ok", false);
            res.addProperty("message", "Company Code, Username and Password are required.");
            return res.toString();
        }

        var optUser = appUserDao.findByCompanyCodeAndUsername(cc, un);
        if (optUser.isEmpty()) {
            // generic message (don’t reveal if user exists)
            res.addProperty("ok", false);
            res.addProperty("message", "Invalid credentials");
            return res.toString();
        }

        AppUser user = optUser.get();
        System.out.println("In login();;;;user[{}]" +user);
        log.debug("In login();;;;user[{}]" ,user);

        if (Boolean.FALSE.equals(user.getActive())) {
            res.addProperty("ok", false);
            res.addProperty("message", "User is inactive");
            return res.toString();
        }

        LocalDateTime now = LocalDateTime.now();

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
            res.addProperty("ok", false);
            res.addProperty("message", "Account locked. Try later.");
            return res.toString();
        }

        System.out.println("encoded password for admin123::::"+
                new BCryptPasswordEncoder().encode("admin123"));

        boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
        System.out.println("matches:::"+matches);
        if (!matches) {
            int fails = (user.getFailedLoginCount() == null ? 0 : user.getFailedLoginCount()) + 1;
            user.setFailedLoginCount(fails);

            // lock after 5 failures for 15 minutes
            if (fails >= 5) {
                user.setLockedUntil(now.plusMinutes(15));
                user.setFailedLoginCount(0);
            }

            appUserDao.save(user);

            res.addProperty("ok", false);
            res.addProperty("message", "Invalid credentials");

            System.out.println("In login();;;;res.toString()[{}]" +res.toString());
            return res.toString();
        }

        // success: reset counters
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(now);
        appUserDao.save(user);

        // store minimal session info (no password hash)
        JsonObject sessionUser = new JsonObject();
        sessionUser.addProperty("userId", user.getUserId());
        sessionUser.addProperty("companyId", user.getCompanyId());
        sessionUser.addProperty("username", user.getUsername());
        sessionUser.addProperty("firstName", user.getFirstName());
        sessionUser.addProperty("lastName", user.getLastName());
        sessionUser.addProperty("roleName", user.getRoleName());
        sessionUser.addProperty("location", user.getLocation());

        session.setAttribute("USER", sessionUser.toString());
        session.setMaxInactiveInterval(30 * 60); // 30 min

        res.addProperty("ok", true);
        res.addProperty("message", "Login success");
        res.add("user", sessionUser);

        System.out.println("In login();;;;AT THE END:::::res.toString()[{}]" +res.toString());
        return res.toString();
    }

    @Override
    public String me(HttpSession session) {
        JsonObject res = new JsonObject();
        Object u = (session == null) ? null : session.getAttribute("USER");
        if (u == null) {
            res.addProperty("ok", false);
            return res.toString();
        }
        res.addProperty("ok", true);
        // stored as JSON string
        res.addProperty("user", u.toString());
        return res.toString();
    }

    @Override
    public String logout(HttpSession session) {
        JsonObject res = new JsonObject();
        if (session != null) {
            session.invalidate();
        }
        res.addProperty("ok", true);
        return res.toString();
    }

}