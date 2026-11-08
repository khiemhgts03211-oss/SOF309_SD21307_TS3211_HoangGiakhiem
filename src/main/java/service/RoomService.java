package service;

import java.util.List;

import model.Room;

public interface RoomService extends GenericService<Room, Integer> {

    /**
     * Phòng đang hoạt động
     */
    List<Room> findAvailableRooms();

    /**
     * Tìm theo trạng thái
     */
    List<Room> findByStatus(String status);

    /**
     * Tìm theo sức chứa
     */
    List<Room> findByCapacity(int capacity);

    /**
     * Kiểm tra tên phòng tồn tại
     */
    boolean existsRoomName(String roomName);

    boolean hasShowtimes(Integer roomId);
    
    List<Room> sort(String sortBy, String order);
}