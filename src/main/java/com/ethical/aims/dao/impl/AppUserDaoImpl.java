package com.ethical.aims.dao.impl;

import com.ethical.aims.dao.AppUserDao;
import com.ethical.aims.entity.AppUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class AppUserDaoImpl implements AppUserDao {

    private static final Logger log = LoggerFactory.getLogger(AppUserDaoImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<AppUser> findByCompanyCodeAndUsername(String companyCode, String username) {
        // avoids needing a Company entity: uses subquery
        String sql = """
                SELECT u.*
                      FROM APP_USER u
                      JOIN COMPANY c ON c.COMPANY_ID = u.COMPANY_ID
                      WHERE c.COMPANY_CODE = :companyCode
                        AND u.USERNAME = :username
                      LIMIT 1
""";

        log.debug("Inside AppUserDaoImpl at findByCompanyCodeAndUsername();;;;" +
                " companyCode[{}],  username[{}]",  companyCode,  username);
        // If you DO NOT have Company entity, use native SQL below instead.
        Query q = em.createNativeQuery(sql, AppUser.class);
        q.setParameter("companyCode", companyCode);
        q.setParameter("username", username);

        @SuppressWarnings("unchecked")
        List<AppUser> list = q.getResultList();
        log.debug("Inside AppUserDaoImpl at findByCompanyCodeAndUsername();;;;" +
                " list[{}]", list);

        return list.stream().findFirst();
    }

    @Override
    public void save(AppUser user) {
        em.merge(user);
    }
}
