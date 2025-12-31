package com.ethical.aims.dao.impl;


import com.ethical.aims.entity.ContextAspect;
import org.springframework.stereotype.Repository;

@Repository
public class ContextAspectDaoImpl extends GenericDaoImpl<ContextAspect, Long> {
    public ContextAspectDaoImpl() {
        super(ContextAspect.class);
    }
}
