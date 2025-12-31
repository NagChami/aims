package com.ethical.aims.dao;

import com.ethical.aims.entity.AppUser;

import java.util.Optional;

public interface AppUserDao {
    Optional<AppUser> findByCompanyCodeAndUsername(String companyCode, String username);
    void save(AppUser user);
}
