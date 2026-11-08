package controller.customer.booking;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Showtime;
import model.User;
import service.ShowtimeService;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/booking/confirm")
public class BookingConfirmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MIN_PEOPLE_COUNT = 1;
    private static final int MAX_PEOPLE_COUNT = 20;

    private ShowtimeService showtimeService;

    @Override
    public void init() throws ServletException {

        try {

            showtimeService =
                    new ShowtimeServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo ShowtimeService.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        /*
         * Không cho truy cập trực tiếp bằng GET.
         * Người dùng phải đi từ create.jsp bằng POST.
         */
        response.sendRedirect(
                request.getContextPath()
                        + "/home");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        /*
         * =====================================================
         * 1. KIỂM TRA ĐĂNG NHẬP
         * =====================================================
         */

        HttpSession session =
                request.getSession(false);

        Object sessionUser =
                session == null
                        ? null
                        : session.getAttribute("user");

        if (!(sessionUser instanceof User)) {

            saveRedirectAfterLogin(request);

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User user =
                (User) sessionUser;

        /*
         * =====================================================
         * 2. KIỂM TRA TÀI KHOẢN
         * =====================================================
         */

        if (!isActiveUser(user)) {

            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?error=account_inactive");

            return;
        }

        /*
         * =====================================================
         * 3. CHỈ CUSTOMER ĐƯỢC ĐẶT VÉ
         * =====================================================
         */

        if (!isCustomer(user)) {

            redirectWrongRole(
                    request,
                    response,
                    user);

            return;
        }

        /*
         * =====================================================
         * 4. LẤY SHOWTIME ID
         * =====================================================
         */

        Integer showtimeId =
                parsePositiveInteger(
                        request.getParameter("showtimeId"));

        if (showtimeId == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=invalid_showtime");

            return;
        }

        /*
         * =====================================================
         * 5. LẤY SỐ LƯỢNG NGƯỜI
         * =====================================================
         */

        Integer peopleCount =
                parsePeopleCount(
                        request.getParameter("peopleCount"));

        if (peopleCount == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/create?showtimeId="
                            + showtimeId
                            + "&error=invalid_people_count");

            return;
        }

        try {

            /*
             * =================================================
             * 6. LẤY LẠI SHOWTIME TỪ DATABASE
             * =================================================
             *
             * Không tin dữ liệu gửi từ HTML.
             *
             * Nếu service của project đang dùng
             * getShowtimeById(showtimeId)
             * thì đổi method bên dưới.
             */

            Showtime showtime =
                    showtimeService.getShowtimeById(
                            showtimeId);

            if (showtime == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=showtime_not_found");

                return;
            }

            /*
             * =================================================
             * 7. KIỂM TRA SUẤT CHIẾU
             * =================================================
             */

            if (!isBookableShowtime(showtime)) {

                redirectToShowtimes(
                        request,
                        response,
                        showtime,
                        "showtime_unavailable");

                return;
            }

            /*
             * =================================================
             * 8. KIỂM TRA PHÒNG
             * =================================================
             */

            if (!hasValidRoom(showtime)) {

                redirectToShowtimes(
                        request,
                        response,
                        showtime,
                        "invalid_room");

                return;
            }

            /*
             * =================================================
             * 9. TÍNH TIỀN VÉ
             * =================================================
             *
             * ticketTotal =
             * ticketPrice × peopleCount
             *
             * KHÔNG lấy totalPrice từ request.
             */

            BigDecimal ticketPrice =
                    showtime.getTicketPrice();

            BigDecimal ticketTotal =
                    ticketPrice.multiply(
                            BigDecimal.valueOf(
                                    peopleCount));

            /*
             * =================================================
             * 10. LẤY ROOM ID TỪ SHOWTIME
             * =================================================
             *
             * Không dùng:
             *
             * request.getParameter("roomId")
             */

            int roomId =
                    showtime.getRoomId();

            /*
             * =================================================
             * 11. LƯU TẠM BOOKING VÀO SESSION
             * =================================================
             *
             * Các thông tin này sẽ dùng ở bước
             * 24.3 Chọn dịch vụ.
             */

            session.setAttribute(
                    "bookingShowtimeId",
                    showtime.getShowtimeId());

            session.setAttribute(
                    "bookingMovieId",
                    showtime.getMovieId());

            session.setAttribute(
                    "bookingRoomId",
                    roomId);

            session.setAttribute(
                    "bookingPeopleCount",
                    peopleCount);

            session.setAttribute(
                    "bookingTicketPrice",
                    ticketPrice);

            session.setAttribute(
                    "bookingTicketTotal",
                    ticketTotal);

            /*
             * Có thể lưu Showtime để tiện hiển thị.
             * Tuy nhiên ở bước tạo ticket cuối cùng
             * vẫn phải truy vấn DB lại.
             */
            session.setAttribute(
                    "bookingShowtime",
                    showtime);

            /*
             * =================================================
             * 12. CHUYỂN SANG BƯỚC 24.3
             * =================================================
             */

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/services");

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/create?showtimeId="
                            + showtimeId
                            + "&error=server_error");
        }
    }

    /*
     * =========================================================
     * KIỂM TRA SỐ LƯỢNG NGƯỜI
     * =========================================================
     */

    private Integer parsePeopleCount(
            String value) {

        if (value == null
                || value.isBlank()) {

            return null;
        }

        try {

            int peopleCount =
                    Integer.parseInt(
                            value.trim());

            if (peopleCount < MIN_PEOPLE_COUNT
                    || peopleCount > MAX_PEOPLE_COUNT) {

                return null;
            }

            return peopleCount;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    /*
     * =========================================================
     * KIỂM TRA SHOWTIME
     * =========================================================
     */

    private boolean isBookableShowtime(
            Showtime showtime) {

        if (showtime == null) {
            return false;
        }

        if (showtime.getShowtimeId() <= 0) {
            return false;
        }

        if (showtime.getMovieId() <= 0) {
            return false;
        }

        if (showtime.getRoomId() <= 0) {
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

        if (!"OPEN".equals(status)
                && !"ACTIVE".equals(status)) {

            return false;
        }

        return !isPastShowtime(showtime);
    }

    /*
     * =========================================================
     * KIỂM TRA PHÒNG
     * =========================================================
     */

    private boolean hasValidRoom(
            Showtime showtime) {

        if (showtime == null) {
            return false;
        }

        if (showtime.getRoomId() <= 0) {
            return false;
        }

        String roomName =
                normalize(
                        showtime.getRoomName());

        return !roomName.isBlank();
    }

    /*
     * =========================================================
     * KIỂM TRA SUẤT CHIẾU ĐÃ QUA
     * =========================================================
     */

    private boolean isPastShowtime(
            Showtime showtime) {

        Date showDate =
                showtime.getShowDate();

        Time startTime =
                showtime.getStartTime();

        if (showDate == null
                || startTime == null) {

            return true;
        }

        LocalDate today =
                LocalDate.now();

        LocalTime now =
                LocalTime.now();

        LocalDate showLocalDate =
                showDate.toLocalDate();

        LocalTime showLocalTime =
                startTime.toLocalTime();

        if (showLocalDate.isBefore(today)) {
            return true;
        }

        if (showLocalDate.isAfter(today)) {
            return false;
        }

        return !showLocalTime.isAfter(now);
    }

    /*
     * =========================================================
     * USER
     * =========================================================
     */

    private boolean isCustomer(
            User user) {

        return user != null
                && "CUSTOMER".equals(
                        normalizeUpper(
                                user.getRole()));
    }

    private boolean isActiveUser(
            User user) {

        return user != null
                && "ACTIVE".equals(
                        normalizeUpper(
                                user.getStatus()));
    }

    /*
     * =========================================================
     * REDIRECT SAI QUYỀN
     * =========================================================
     */

    private void redirectWrongRole(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        String role =
                normalizeUpper(
                        user == null
                                ? null
                                : user.getRole());

        switch (role) {

            case "ADMIN":

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/dashboard?error=forbidden");

                break;

            case "STAFF":

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/dashboard?error=forbidden");

                break;

            default:

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=forbidden");

                break;
        }
    }

    /*
     * =========================================================
     * REDIRECT VỀ SHOWTIMES
     * =========================================================
     */

    private void redirectToShowtimes(
            HttpServletRequest request,
            HttpServletResponse response,
            Showtime showtime,
            String errorCode)
            throws IOException {

        int movieId =
                showtime == null
                        ? 0
                        : showtime.getMovieId();

        if (movieId > 0) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/showtimes?movieId="
                            + movieId
                            + "&error="
                            + errorCode);

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error="
                            + errorCode);
        }
    }

    /*
     * =========================================================
     * REDIRECT SAU LOGIN
     * =========================================================
     */

    private void saveRedirectAfterLogin(
            HttpServletRequest request) {

        String requestUri =
                request.getRequestURI();

        String queryString =
                request.getQueryString();

        String redirectUrl =
                requestUri;

        if (queryString != null
                && !queryString.isBlank()) {

            redirectUrl +=
                    "?"
                    + queryString;
        }

        request.getSession(true)
                .setAttribute(
                        "redirectAfterLogin",
                        redirectUrl);
    }

    /*
     * =========================================================
     * PARSE ID
     * =========================================================
     */

    private Integer parsePositiveInteger(
            String value) {

        if (value == null
                || value.isBlank()) {

            return null;
        }

        try {

            int parsedValue =
                    Integer.parseInt(
                            value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    /*
     * =========================================================
     * STRING
     * =========================================================
     */

    private String normalizeUpper(
            String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
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

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

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