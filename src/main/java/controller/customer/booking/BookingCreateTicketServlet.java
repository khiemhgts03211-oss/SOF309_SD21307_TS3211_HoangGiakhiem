package controller.customer.booking;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import controller.customer.booking.BookingServiceConfirmServlet.SelectedService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Showtime;
import model.Ticket;
import model.TicketDetail;
import model.User;

import service.ShowtimeService;
import service.TicketDetailService;
import service.TicketService;

import service.impl.ShowtimeServiceImpl;
import service.impl.TicketDetailServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/booking/create-ticket")
public class BookingCreateTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MIN_PEOPLE_COUNT = 1;
    private static final int MAX_PEOPLE_COUNT = 20;

    private ShowtimeService showtimeService;
    private TicketService ticketService;
    private TicketDetailService ticketDetailService;

    @Override
    public void init() throws ServletException {

        try {

            showtimeService =
                    new ShowtimeServiceImpl();

            ticketService =
                    new TicketServiceImpl();

            ticketDetailService =
                    new TicketDetailServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo các service đặt vé.",
                    exception);
        }
    }

    /*
     * =========================================================
     * GET
     * =========================================================
     */

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/home");
    }

    /*
     * =========================================================
     * POST
     * =========================================================
     */

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(
                request,
                response);

        /*
         * =====================================================
         * 1. KIỂM TRA SESSION
         * =====================================================
         */

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        Object sessionUser =
                session.getAttribute("user");

        if (!(sessionUser instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User user =
                (User) sessionUser;

        /*
         * =====================================================
         * 2. KIỂM TRA QUYỀN
         * =====================================================
         */

        if (!isActiveCustomer(user)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=forbidden");

            return;
        }

        /*
         * =====================================================
         * 3. LẤY DỮ LIỆU ĐÃ VALIDATE TỪ BƯỚC 24.7
         * =====================================================
         */

        Integer showtimeId =
                getIntegerAttribute(
                        session,
                        "finalShowtimeId");

        Integer peopleCount =
                getIntegerAttribute(
                        session,
                        "finalPeopleCount");

        if (showtimeId == null
                || showtimeId <= 0
                || peopleCount == null
                || peopleCount < MIN_PEOPLE_COUNT
                || peopleCount > MAX_PEOPLE_COUNT) {

            clearBookingData(
                    session);

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=booking_data_invalid");

            return;
        }

        /*
         * Phải lấy danh sách dịch vụ trước
         * khi xóa booking session.
         */
        List<SelectedService> selectedServices =
                getSelectedServices(
                        session);

        try {

            /*
             * =================================================
             * 4. LẤY LẠI SHOWTIME TỪ DATABASE
             * =================================================
             */

            Showtime showtime =
                    showtimeService.getShowtimeById(
                            showtimeId);

            /*
             * Nếu ShowtimeService của project dùng:
             *
             * getShowtimeById(showtimeId)
             *
             * thì đổi đúng dòng trên.
             */

            if (showtime == null) {

                clearBookingData(
                        session);

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=showtime_not_found");

                return;
            }

            /*
             * =================================================
             * 5. KIỂM TRA SHOWTIME
             * =================================================
             */

            if (!isBookableShowtime(
                    showtime)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/showtimes?movieId="
                                + showtime.getMovieId()
                                + "&error=showtime_unavailable");

                return;
            }

            /*
             * =================================================
             * 6. LẤY PHÒNG TỪ SHOWTIME
             * =================================================
             *
             * Không lấy roomId từ request/session.
             */

            int roomId =
                    showtime.getRoomId();

            if (roomId <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/showtimes?movieId="
                                + showtime.getMovieId()
                                + "&error=invalid_room");

                return;
            }

            /*
             * =================================================
             * 7. LẤY GIÁ VÉ TỪ DATABASE
             * =================================================
             */

            BigDecimal ticketPrice =
                    showtime.getTicketPrice();

            if (ticketPrice == null
                    || ticketPrice.compareTo(
                            BigDecimal.ZERO) <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=invalid_ticket_price");

                return;
            }

            /*
             * =================================================
             * 8. TÍNH TIỀN VÉ
             * =================================================
             */

            BigDecimal ticketTotal =
                    calculateTicketTotal(
                            ticketPrice,
                            peopleCount);

            /*
             * =================================================
             * 9. TÍNH TIỀN DỊCH VỤ
             * =================================================
             */

            BigDecimal serviceTotal =
                    calculateServiceTotal(
                            selectedServices);

            /*
             * =================================================
             * 10. TỔNG TIỀN
             * =================================================
             */

            BigDecimal grandTotal =
                    ticketTotal.add(
                            serviceTotal);

            if (grandTotal.compareTo(
                    BigDecimal.ZERO) <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=invalid_total");

                return;
            }

            /*
             * =================================================
             * 11. TẠO BOOKING CODE
             * =================================================
             */

            String bookingCode =
                    generateBookingCode(
                            user.getUserId());

            /*
             * =================================================
             * 12. TẠO TICKET
             * =================================================
             */

            Ticket ticket =
                    new Ticket();

            ticket.setBookingCode(
                    bookingCode);

            ticket.setUserId(
                    user.getUserId());

            ticket.setShowtimeId(
                    showtime.getShowtimeId());

            ticket.setRoomId(
                    roomId);

            ticket.setPeopleCount(
                    peopleCount);

            ticket.setTotalPrice(
                    grandTotal);

            ticket.setStatus(
                    "BOOKED");

            /*
             * =================================================
             * 13. INSERT TICKET
             * =================================================
             */

            boolean ticketCreated =
                    ticketService.createTicket(
                            ticket);

            if (!ticketCreated) {

                System.err.println(
                        "Không thể tạo ticket. BookingCode="
                                + bookingCode);

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=booking_create_failed");

                return;
            }

            /*
             * =================================================
             * 14. KIỂM TRA GENERATED TICKET ID
             * =================================================
             *
             * TicketDAOImpl.insert() phải có:
             *
             * Statement.RETURN_GENERATED_KEYS
             *
             * và:
             *
             * ticket.setTicketId(generatedId);
             */

            int ticketId =
                    ticket.getTicketId();

            System.out.println(
                    "[BookingCreateTicket] ticketId = "
                            + ticketId);

            if (ticketId <= 0) {

                System.err.println(
                        "Ticket đã INSERT nhưng ticketId = "
                                + ticketId
                                + ". Kiểm tra TicketDAOImpl.insert().");

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=invalid_ticket_id");

                return;
            }

            /*
             * =================================================
             * 15. LƯU DỊCH VỤ ĐÃ CHỌN
             * =================================================
             */

            boolean detailsCreated =
                    createTicketDetails(
                            ticketId,
                            selectedServices);

            if (!detailsCreated) {

                System.err.println(
                        "Ticket đã tạo nhưng lưu TicketDetail thất bại. "
                                + "ticketId="
                                + ticketId);

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=ticket_detail_failed");

                return;
            }

            /*
             * =================================================
             * 16. LƯU THÔNG TIN CHO SUCCESS PAGE
             * =================================================
             */

            session.setAttribute(
                    "lastCreatedTicket",
                    ticket);

            session.setAttribute(
                    "lastBookingShowtime",
                    showtime);

            session.setAttribute(
                    "lastBookingSelectedServices",
                    selectedServices);

            session.setAttribute(
                    "lastBookingTicketTotal",
                    ticketTotal);

            session.setAttribute(
                    "lastBookingServiceTotal",
                    serviceTotal);

            session.setAttribute(
                    "lastBookingGrandTotal",
                    grandTotal);

            /*
             * =================================================
             * 17. XÓA BOOKING DATA TẠM
             * =================================================
             *
             * Không xóa lastCreatedTicket...
             */

            clearBookingData(
                    session);

            /*
             * =================================================
             * 18. THÀNH CÔNG
             * =================================================
             */

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/success");

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=server_error");
        }
    }

    /*
     * =========================================================
     * TẠO TICKET DETAILS
     * =========================================================
     */

    private boolean createTicketDetails(
            int ticketId,
            List<SelectedService> selectedServices) {

        if (ticketId <= 0) {
            return false;
        }

        /*
         * Không chọn dịch vụ là hợp lệ.
         */
        if (selectedServices == null
                || selectedServices.isEmpty()) {

            return true;
        }

        for (SelectedService selected :
                selectedServices) {

            if (selected == null) {
                continue;
            }

            int serviceId =
                    selected.getServiceId();

            int quantity =
                    selected.getQuantity();

            BigDecimal unitPrice =
                    selected.getUnitPrice();

            if (serviceId <= 0
                    || quantity <= 0
                    || unitPrice == null
                    || unitPrice.compareTo(
                            BigDecimal.ZERO) < 0) {

                return false;
            }

            /*
             * TicketDetail.amount =
             * tổng tiền của dịch vụ đó.
             */
            BigDecimal amount =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    quantity));

            TicketDetail detail =
                    new TicketDetail();

            detail.setTicketId(
                    ticketId);

            detail.setServiceId(
                    serviceId);

            detail.setQuantity(
                    quantity);

            detail.setAmount(
                    amount);

            /*
             * Đúng theo TicketDetailServiceImpl hiện tại.
             */
            boolean created =
                    ticketDetailService
                            .createTicketDetail(
                                    detail);

            if (!created) {

                System.err.println(
                        "Không tạo được TicketDetail: "
                                + "ticketId="
                                + ticketId
                                + ", serviceId="
                                + serviceId);

                return false;
            }
        }

        return true;
    }

    /*
     * =========================================================
     * TÍNH TIỀN VÉ
     * =========================================================
     */

    private BigDecimal calculateTicketTotal(
            BigDecimal ticketPrice,
            int peopleCount) {

        if (ticketPrice == null
                || ticketPrice.compareTo(
                        BigDecimal.ZERO) <= 0
                || peopleCount <= 0) {

            return BigDecimal.ZERO;
        }

        return ticketPrice.multiply(
                BigDecimal.valueOf(
                        peopleCount));
    }

    /*
     * =========================================================
     * TÍNH TIỀN DỊCH VỤ
     * =========================================================
     */

    private BigDecimal calculateServiceTotal(
            List<SelectedService> selectedServices) {

        BigDecimal total =
                BigDecimal.ZERO;

        if (selectedServices == null
                || selectedServices.isEmpty()) {

            return total;
        }

        for (SelectedService selected :
                selectedServices) {

            if (selected == null) {
                continue;
            }

            int quantity =
                    selected.getQuantity();

            BigDecimal unitPrice =
                    selected.getUnitPrice();

            if (quantity <= 0
                    || unitPrice == null
                    || unitPrice.compareTo(
                            BigDecimal.ZERO) < 0) {

                continue;
            }

            BigDecimal amount =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    quantity));

            total =
                    total.add(
                            amount);
        }

        return total;
    }

    /*
     * =========================================================
     * SHOWTIME
     * =========================================================
     */

    private boolean isBookableShowtime(
            Showtime showtime) {

        if (showtime == null) {
            return false;
        }

        if (showtime.getShowtimeId() <= 0
                || showtime.getMovieId() <= 0
                || showtime.getRoomId() <= 0) {

            return false;
        }

        if (showtime.getShowDate() == null
                || showtime.getStartTime() == null) {

            return false;
        }

        BigDecimal ticketPrice =
                showtime.getTicketPrice();

        if (ticketPrice == null
                || ticketPrice.compareTo(
                        BigDecimal.ZERO) <= 0) {

            return false;
        }

        String status =
                normalizeUpper(
                        showtime.getStatus());

        return "OPEN".equals(status)
                || "ACTIVE".equals(status);
    }

    /*
     * =========================================================
     * BOOKING CODE
     * =========================================================
     */

    private String generateBookingCode(
            int userId) {

        return "BK"
                + userId
                + "-"
                + System.currentTimeMillis();
    }

    /*
     * =========================================================
     * LẤY SELECTED SERVICES
     * =========================================================
     */

    private List<SelectedService> getSelectedServices(
            HttpSession session) {

        if (session == null) {
            return Collections.emptyList();
        }

        Object value =
                session.getAttribute(
                        "finalSelectedServices");

        if (!(value instanceof List<?>)) {

            return Collections.emptyList();
        }

        List<?> rawList =
                (List<?>) value;

        List<SelectedService> result =
                new ArrayList<>();

        for (Object item : rawList) {

            if (item instanceof SelectedService) {

                result.add(
                        (SelectedService) item);
            }
        }

        return result;
    }

    /*
     * =========================================================
     * USER
     * =========================================================
     */

    private boolean isActiveCustomer(
            User user) {

        if (user == null) {
            return false;
        }

        String role =
                normalizeUpper(
                        user.getRole());

        String status =
                normalizeUpper(
                        user.getStatus());

        return "CUSTOMER".equals(role)
                && "ACTIVE".equals(status);
    }

    /*
     * =========================================================
     * SESSION ATTRIBUTE
     * =========================================================
     */

    private Integer getIntegerAttribute(
            HttpSession session,
            String name) {

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute(name);

        if (value instanceof Integer) {

            return (Integer) value;
        }

        return null;
    }

    /*
     * =========================================================
     * CLEAR BOOKING SESSION
     * =========================================================
     */

    private void clearBookingData(
            HttpSession session) {

        if (session == null) {
            return;
        }

        /*
         * Booking
         */
        session.removeAttribute(
                "bookingShowtimeId");

        session.removeAttribute(
                "bookingMovieId");

        session.removeAttribute(
                "bookingRoomId");

        session.removeAttribute(
                "bookingPeopleCount");

        session.removeAttribute(
                "bookingTicketPrice");

        session.removeAttribute(
                "bookingTicketTotal");

        session.removeAttribute(
                "bookingServiceTotal");

        session.removeAttribute(
                "bookingGrandTotal");

        session.removeAttribute(
                "bookingSelectedServices");

        session.removeAttribute(
                "bookingShowtime");

        /*
         * Final validated
         */
        session.removeAttribute(
                "finalShowtimeId");

        session.removeAttribute(
                "finalMovieId");

        session.removeAttribute(
                "finalRoomId");

        session.removeAttribute(
                "finalPeopleCount");

        session.removeAttribute(
                "finalTicketPrice");

        session.removeAttribute(
                "finalTicketTotal");

        session.removeAttribute(
                "finalServiceTotal");

        session.removeAttribute(
                "finalGrandTotal");

        session.removeAttribute(
                "finalSelectedServices");
    }

    /*
     * =========================================================
     * STRING
     * =========================================================
     */

    private String normalizeUpper(
            String value) {

        return value == null
                ? ""
                : value.trim()
                        .toUpperCase(
                                Locale.ROOT);
    }

    /*
     * =========================================================
     * RESPONSE
     * =========================================================
     */

    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding(
                "UTF-8");

        response.setCharacterEncoding(
                "UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate");

        response.setHeader(
                "Pragma",
                "no-cache");

        response.setDateHeader(
                "Expires",
                0);
    }
}