package service.impl;

import java.util.List;

import dao.RoomDAO;
import dao.impl.RoomDAOImpl;
import model.Room;
import service.RoomService;

public class RoomServiceImpl implements RoomService {

    private final RoomDAO roomDAO;

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    /*
     * ==========================
     * GenericService
     * ==========================
     */

    @Override
    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return roomDAO.findById(id);
    }

    @Override
    public boolean save(Room room) {
        return roomDAO.insert(room);
    }

    @Override
    public boolean update(Room room) {
        return roomDAO.update(room);
    }

    @Override
    public boolean delete(Integer id) {
        return roomDAO.delete(id);
    }

    @Override
    public int count() {
        return roomDAO.count();
    }

    @Override
    public List<Room> search(String keyword) {
        return roomDAO.search(keyword);
    }

    @Override
    public List<Room> findByPage(int page, int pageSize) {
        return roomDAO.findByPage(page, pageSize);
    }

    /*
     * ==========================
     * Business Methods
     * ==========================
     */

    @Override
    public List<Room> findAvailableRooms() {
        return roomDAO.findAvailableRooms();
    }

    @Override
    public List<Room> findByStatus(String status) {
        return roomDAO.findByStatus(status);
    }

    @Override
    public List<Room> findByCapacity(int capacity) {
        return roomDAO.findByCapacity(capacity);
    }

    @Override
    public boolean existsRoomName(String roomName) {
        return roomDAO.existsRoomName(roomName);
    }

    @Override
    public boolean hasShowtimes(Integer roomId) {

        return roomDAO.hasShowtimes(roomId);

    }
    
    @Override
    public List<Room> sort(String sortBy, String order) {

        return roomDAO.sort(sortBy, order);

    }
    
}