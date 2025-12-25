package com.ethical.aims.config;

import com.ethical.aims.dao.GenericDao;
import com.ethical.aims.dao.impl.GenericDaoImpl;
import com.ethical.aims.entity.Company;
import com.ethical.aims.entity.Section;
import org.springframework.context.annotation.Bean;

public class GenericDaoFactory {

    @Bean
    public GenericDao<Company, Long> userDao() {
        return new GenericDaoImpl<>(Company.class);
    }


    @Bean
    public GenericDao<Section, Long> sectionDao() {
        return new GenericDaoImpl<>(Section.class);
    }
}
