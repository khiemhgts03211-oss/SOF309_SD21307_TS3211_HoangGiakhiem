package dao;

import java.util.List;

public interface GenericDAO<T, K> {

    List<T> findAll();

    T findById(K id);

    boolean insert(T entity);

    boolean update(T entity);

    boolean delete(K id);

    List<T> search(String keyword);

    List<T> findByPage(int page, int pageSize);

    int count();
}