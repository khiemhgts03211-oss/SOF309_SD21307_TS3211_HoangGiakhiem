package controller.customer.booking;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Service;
import model.Showtime;
import model.User;
import service.ServiceService;
import service.ShowtimeService;
import service.impl.ServiceServiceImpl;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/booking/services")
public class BookingServicesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ServiceService serviceService;
    private ShowtimeService showtimeService;

    @Override
    public void init() throws ServletException {

        try {

            serviceService =
                    new ServiceServiceImpl();

            showtimeService =
                    new ShowtimeServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo service cho bước chọn dịch vụ.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        HttpSession session =
                request.getSession(false);

        /*
         * =============================
         * KIỂM TRA ĐĂNG NHẬP
         * =============================
         */

        Object sessionUser =
                session == null
                        ? null
                        : session.getAttribute("user");

        if (!(sessionUser instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User user =
                (User) sessionUser;

        if (!isCustomer(user)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Chỉ khách hàng mới được chọn dịch vụ.");

            return;
        }

        /*
         * =============================
         * LẤY BOOKING TẠM TỪ SESSION
         * =============================
         */

        Integer showtimeId =
                getIntegerAttribute(
                        session,
                        "bookingShowtimeId");

        Integer peopleCount =
                getIntegerAttribute(
                        session,
                        "bookingPeopleCount");

        BigDecimal ticketTotal =
                getBigDecimalAttribute(
                        session,
                        "bookingTicketTotal");

        if (showtimeId == null
                || peopleCount == null
                || ticketTotal == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=booking_session_expired");

            return;
        }

        try {

            /*
             * Lấy lại Showtime từ DB
             */
            Showtime showtime =
                    showtimeService.getShowtimeById(
                            showtimeId);

            if (showtime == null) {

                clearBookingSession(session);

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=showtime_not_found");

                return;
            }

            /*
             * =============================
             * LẤY DỊCH VỤ ACTIVE
             * =============================
             */

            List<Service> services =
                    serviceService
                            .getActiveServices();

            if (services == null) {
                services =
                        Collections.emptyList();
            }

            request.setAttribute(
                    "showtime",
                    showtime);

            request.setAttribute(
                    "peopleCount",
                    peopleCount);

            request.setAttribute(
                    "ticketTotal",
                    ticketTotal);

            request.setAttribute(
                    "services",
                    services);

            request.setAttribute(
                    "serviceCount",
                    services.size());

            request.setAttribute(
                    "pageTitle",
                    "Chọn dịch vụ");

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/booking/services.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=server_error");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Trang chọn dịch vụ chỉ hỗ trợ GET.");
    }

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

    private BigDecimal getBigDecimalAttribute(
            HttpSession session,
            String name) {

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute(name);

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        return null;
    }

    private boolean isCustomer(
            User user) {

        return user != null
                && "CUSTOMER".equals(
                        normalizeUpper(
                                user.getRole()));
    }

    private void clearBookingSession(
            HttpSession session) {

        if (session == null) {
            return;
        }

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
                "bookingShowtime");
    }

    private String normalizeUpper(
            String value) {

        return value == null
                ? ""
                : value.trim()
                        .toUpperCase(
                                Locale.ROOT);
    }

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