package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.BookingDTO;

public class DashboardDAO extends BaseDAO {

    private int count(String sql) {

        try {

            Connection con = getConnection();

            preparedStatement = con.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return 0;
    }

    public int totalUsers() {
        return count("SELECT COUNT(*) FROM users");
    }

    public int totalMovies() {
        return count("SELECT COUNT(*) FROM movies");
    }

    public int totalRooms() {
        return count("SELECT COUNT(*) FROM rooms");
    }

    public int totalShowtimes() {
        return count("SELECT COUNT(*) FROM showtimes");
    }

    public int totalTickets() {
        return count("SELECT COUNT(*) FROM tickets");
    }

    public double totalRevenue() {

        try {

            Connection con = getConnection();

            preparedStatement = con.prepareStatement(
                    "SELECT ISNULL(SUM(total_price),0) FROM tickets WHERE status='PAID'");

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return 0;
    }
    
    public Map<Integer, Double> revenueByMonth() {

        Map<Integer, Double> map = new LinkedHashMap<>();

        String sql =
            "SELECT MONTH(created_at) month, " +
            "SUM(total_price) revenue " +
            "FROM tickets " +
            "WHERE status='PAID' " +
            "GROUP BY MONTH(created_at) " +
            "ORDER BY month";

        try (
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                map.put(
                    rs.getInt("month"),
                    rs.getDouble("revenue")
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return map;
    }
    
    public Map<Integer, Integer> ticketByMonth() {

        Map<Integer, Integer> map = new LinkedHashMap<>();

        String sql = """
            SELECT MONTH(created_at) AS month,
                   COUNT(*) AS total
            FROM tickets
            GROUP BY MONTH(created_at)
            ORDER BY MONTH(created_at)
            """;

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                map.put(
                        rs.getInt("month"),
                        rs.getInt("total"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    
    public Map<String, Integer> movieTicketChart() {

        Map<String, Integer> data = new LinkedHashMap<>();

        String sql = """
            SELECT TOP 10
                m.title,
                ISNULL(SUM(t.people_count), 0) AS total
            FROM movies m
            INNER JOIN showtimes s
                ON m.movie_id = s.movie_id
            INNER JOIN tickets t
                ON s.showtime_id = t.showtime_id
            WHERE UPPER(LTRIM(RTRIM(ISNULL(t.status, ''))))
                  NOT IN ('CANCELLED', 'FAILED')
            GROUP BY
                m.movie_id,
                m.title
            ORDER BY
                total DESC,
                m.title ASC
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                String movieTitle =
                        resultSet.getString("title");

                int totalTickets =
                        resultSet.getInt("total");

                if (movieTitle == null
                        || movieTitle.isBlank()) {

                    movieTitle = "Phim chưa xác định";
                }

                data.put(
                        movieTitle.trim(),
                        Math.max(totalTickets, 0));
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Lỗi thống kê vé bán theo phim: "
                            + exception.getMessage());

            exception.printStackTrace();
        }

        return data;
    }
    
    public List<BookingDTO> recentBookings() {

        List<BookingDTO> list = new ArrayList<>();

        String sql = """
            SELECT TOP 10
                t.ticket_id,
                t.booking_code,
                u.full_name,
                m.title,
                t.total_price,
                t.status,
                t.created_at
            FROM tickets t
            JOIN users u
                ON t.user_id = u.user_id
            JOIN showtimes s
                ON t.showtime_id = s.showtime_id
            JOIN movies m
                ON s.movie_id = m.movie_id
            ORDER BY t.created_at DESC
            """;

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                BookingDTO b = new BookingDTO();

                b.setTicketId(rs.getInt("ticket_id"));
                b.setBookingCode(rs.getString("booking_code"));
                b.setCustomerName(rs.getString("full_name"));
                b.setMovieTitle(rs.getString("title"));
                b.setTotalPrice(rs.getDouble("total_price"));
                b.setStatus(rs.getString("status"));
                b.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(b);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;

    }
    
}