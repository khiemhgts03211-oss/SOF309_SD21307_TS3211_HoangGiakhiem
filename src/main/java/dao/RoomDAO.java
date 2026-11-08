package dao;

import java.util.List;

import model.Room;

public interface RoomDAO extends GenericDAO<Room, Integer> {

    /**
     * Phòng đang hoạt động
     */
    List<Room> findAvailableRooms();

    /**
     * Tìm theo trạng thái
     */
    List<Room> findByStatus(String status);

    /**
     * Kiểm tra tên phòng tồn tại
     */
    boolean existsRoomName(String roomName);

	List<Room> findByCapacity(int capacity);
	
	boolean hasShowtimes(Integer roomId);
	
	List<Room> sort(String sortBy, String order);
	
}