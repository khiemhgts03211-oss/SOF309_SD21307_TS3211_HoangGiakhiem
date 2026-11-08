package service;

import java.util.List;

public interface GenericService<T, ID> {

    /**
     * Lấy tất cả dữ liệu
     */
    List<T> findAll();

    /**
     * Tìm theo ID
     */
    T findById(ID id);

    /**
     * Thêm mới
     */
    boolean save(T entity);

    /**
     * Cập nhật
     */
    boolean update(T entity);

    /**
     * Xóa theo ID
     */
    boolean delete(ID id);

    /**
     * Đếm tổng số bản ghi
     */
    int count();

    /**
     * Tìm kiếm
     */
    List<T> search(String keyword);

    /**
     * Phân trang
     */
    List<T> findByPage(int page, int pageSize);

}