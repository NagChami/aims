package com.ethical.aims.service;

public interface AuthService {

    String login(String companyCode, String username, String password, jakarta.servlet.http.HttpSession session);
    String logout(jakarta.servlet.http.HttpSession session);
    String me(jakarta.servlet.http.HttpSession session);
}
