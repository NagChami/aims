package com.ethical.aims.dao.impl;

import com.ethical.aims.dao.GenericDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Transactional
public class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {

    @PersistenceContext
    private EntityManager em;

    private final Class<T> entityClass;

    // Automatically determined from subclass
    public GenericDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> load(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public Optional<T> get(ID id) {
        return load(id);
    }

    @Override
    public List<T> list(String jpql, Map<String, Object> params) {
        TypedQuery<T> query = em.createQuery(jpql, entityClass);
        if (params != null) params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public List<T> list(String jpql, Map<String, Object> params, int offset, int limit) {
        TypedQuery<T> query = em.createQuery(jpql, entityClass);
        if (params != null) params.forEach(query::setParameter);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<T> listNative(String sql, Map<String, Object> params) {
        Query query = em.createNativeQuery(sql, entityClass);
        if (params != null) params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public void save(T entity) {
        em.merge(entity);
    }

    @Override
    public void saveBatch(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            em.merge(entities.get(i));
            if (i % 50 == 0) { em.flush(); em.clear(); }
        }
    }

    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public void deleteBatch(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            delete(entities.get(i));
            if (i % 50 == 0) { em.flush(); em.clear(); }
        }
    }
}