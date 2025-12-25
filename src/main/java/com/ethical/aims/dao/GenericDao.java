package com.ethical.aims.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericDao<T, ID> {

    Optional<T> load(ID id);
    Optional<T> get(ID id);

    // JPQL query with parameters
    List<T> list(String jpql, Map<String, Object> params);

    // JPQL with pagination
    List<T> list(String jpql, Map<String, Object> params, int offset, int limit);

    // Native SQL query
    List<T> listNative(String sql, Map<String, Object> params);

    void save(T entity);
    void saveBatch(List<T> entities); // batch insert/update

    void delete(T entity);
    void deleteBatch(List<T> entities); // batch delete
}
