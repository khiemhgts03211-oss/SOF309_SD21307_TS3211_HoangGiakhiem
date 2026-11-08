package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.RoomDAO;
import model.Room;

public class RoomDAOImpl extends BaseDAO implements RoomDAO {

    /*
     * ==============================
     * SQL QUERY
     * ==============================
     */

    private static final String FIND_ALL =
            "SELECT * FROM rooms ORDER BY room_id DESC";

    private static final String FIND_BY_ID =
            "SELECT * FROM rooms WHERE room_id = ?";

    private static final String INSERT =
            "INSERT INTO rooms("
          + "room_name,"
          + "capacity,"
          + "status"
          + ") VALUES(?,?,?)";

    private static final String UPDATE =
            "UPDATE rooms SET "
          + "room_name=?,"
          + "capacity=?,"
          + "status=? "
          + "WHERE room_id=?";

    private static final String DELETE =
            "DELETE FROM rooms WHERE room_id=?";

    private static final String COUNT =
            "SELECT COUNT(*) FROM rooms";

    private static final String SEARCH =
            "SELECT * FROM rooms "
          + "WHERE room_name LIKE ? "
          + "OR CAST(capacity AS VARCHAR) LIKE ? "
          + "ORDER BY room_id DESC";

    private static final String PAGINATION =
            "SELECT * FROM rooms "
          + "ORDER BY room_id DESC "
          + "OFFSET ? ROWS "
          + "FETCH NEXT ? ROWS ONLY";

    private static final String FIND_BY_STATUS =
            "SELECT * FROM rooms "
          + "WHERE status=? "
          + "ORDER BY room_name ASC";

    private static final String FIND_BY_CAPACITY =
            "SELECT * FROM rooms "
          + "WHERE capacity >= ? "
          + "ORDER BY capacity ASC";

    private static final String FIND_AVAILABLE =
            "SELECT * FROM rooms "
          + "WHERE status='ACTIVE' "
          + "ORDER BY room_name ASC";

    private static final String EXISTS_ROOM_NAME =
            "SELECT COUNT(*) FROM rooms WHERE room_name=?";

    private static final String HAS_SHOWTIMES =
            "SELECT COUNT(*) FROM showtimes WHERE room_id=?";
    
    private static final String SORT_SQL =
            "SELECT * FROM rooms ORDER BY %s %s";
    /*
     * ==============================
     * RESULT SET MAPPING
     * ==============================
     */

    private Room mapResultSet(ResultSet rs) throws SQLException {

        Room room = new Room();

        room.setRoomId(rs.getInt("room_id"));
        room.setRoomName(rs.getString("room_name"));
        room.setCapacity(rs.getInt("capacity"));
        room.setStatus(rs.getString("status"));
        room.setCreatedAt(rs.getTimestamp("created_at"));

        return room;
    }

    @Override
    public java.util.List<Room> findAll() {

        java.util.List<Room> rooms = new java.util.ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(FIND_ALL);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                rooms.add(mapResultSet(resultSet));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return rooms;
    }

    @Override
    public Room findById(Integer id) {

        Room room = null;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(FIND_BY_ID);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                room = mapResultSet(resultSet);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return room;
    }

    @Override
    public boolean insert(Room room) {

        boolean inserted = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(INSERT);

            preparedStatement.setString(1, room.getRoomName());
            preparedStatement.setInt(2, room.getCapacity());
            preparedStatement.setString(3, room.getStatus());

            inserted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return inserted;
    }

    @Override
    public boolean update(Room room) {

        boolean updated = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(UPDATE);

            preparedStatement.setString(1, room.getRoomName());
            preparedStatement.setInt(2, room.getCapacity());
            preparedStatement.setString(3, room.getStatus());
            preparedStatement.setInt(4, room.getRoomId());

            updated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return updated;
    }

    @Override
    public boolean delete(Integer id) {

        boolean deleted = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);

            deleted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return deleted;
    }

    @Override
    public List<Room> search(String keyword) {

        List<Room> rooms = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(SEARCH);

            String search = "%" + keyword + "%";

            preparedStatement.setString(1, search);
            preparedStatement.setString(2, search);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                rooms.add(mapResultSet(resultSet));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return rooms;
    }

    @Override
    public List<Room> findByPage(int page, int pageSize) {

        List<Room> rooms = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(PAGINATION);

            int offset = (page - 1) * pageSize;

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, pageSize);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                rooms.add(mapResultSet(resultSet));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return rooms;
    }

	@Override
	public int count() {

	    int total = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(COUNT);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            total = resultSet.getInt(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return total;
	}

	@Override
	public List<Room> findAvailableRooms() {

	    List<Room> rooms = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_AVAILABLE);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            rooms.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return rooms;
	}

	@Override
	public List<Room> findByStatus(String status) {

	    List<Room> rooms = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_STATUS);

	        preparedStatement.setString(1, status);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            rooms.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return rooms;
	}

	@Override
	public List<Room> findByCapacity(int capacity) {

	    List<Room> rooms = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_CAPACITY);

	        preparedStatement.setInt(1, capacity);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            rooms.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return rooms;
	}
	
	@Override
	public boolean existsRoomName(String roomName) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_ROOM_NAME);

	        preparedStatement.setString(1, roomName);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            exists = resultSet.getInt(1) > 0;

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return exists;
	}

	@Override
	public boolean hasShowtimes(Integer roomId) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(HAS_SHOWTIMES);

	        preparedStatement.setInt(1, roomId);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            exists = resultSet.getInt(1) > 0;

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return exists;
	}
	
	@Override
	public List<Room> sort(String sortBy, String order) {

	    List<Room> rooms = new ArrayList<>();

	    if (!sortBy.equals("room_name")
	            && !sortBy.equals("capacity")
	            && !sortBy.equals("created_at")) {

	        sortBy = "room_id";
	    }

	    if (!order.equalsIgnoreCase("ASC")
	            && !order.equalsIgnoreCase("DESC")) {

	        order = "DESC";
	    }

	    String sql = String.format(SORT_SQL, sortBy, order);

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(sql);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            rooms.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return rooms;
	}
	
}