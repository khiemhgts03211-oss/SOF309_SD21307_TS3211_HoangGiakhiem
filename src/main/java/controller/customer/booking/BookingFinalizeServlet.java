package controller.customer.booking;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import controller.customer.booking.BookingServiceConfirmServlet.SelectedService;

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

@WebServlet("/booking/finalize")
public class BookingFinalizeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MIN_PEOPLE_COUNT = 1;
    private static final int MAX_PEOPLE_COUNT = 20;

    private static final int MIN_SERVICE_QUANTITY = 1;
    private static final int MAX_SERVICE_QUANTITY = 20;

    private ShowtimeService showtimeService;
    private ServiceService serviceService;

    @Override
    public void init() throws ServletException {

        try {

            showtimeService =
                    new ShowtimeServiceImpl();

            serviceService =
                    new ServiceServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo service cho bước kiểm tra booking.",
                    exception);
        }
    }

    /*
     * Không cho truy cập trực tiếp finalize bằng GET.
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
         * 2. KIỂM TRA USER
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

        if (!isCustomer(user)) {

            redirectWrongRole(
                    request,
                    response,
                    user);

            return;
        }

        /*
         * =====================================================
         * 3. LẤY DỮ LIỆU BOOKING TỪ SESSION
         * =====================================================
         */

        Integer showtimeId =
                getIntegerAttribute(
                        session,
                        "bookingShowtimeId");

        Integer peopleCount =
                getIntegerAttribute(
                        session,
                        "bookingPeopleCount");

        if (showtimeId == null
                || showtimeId <= 0
                || peopleCount == null
                || peopleCount < MIN_PEOPLE_COUNT
                || peopleCount > MAX_PEOPLE_COUNT) {

            clearBookingSession(
                    session);

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=booking_data_invalid");

            return;
        }

        try {

            /*
             * =================================================
             * 4. LẤY LẠI SHOWTIME TỪ DATABASE
             * =================================================
             */

            Showtime showtime =
                    showtimeService.getShowtimeById(
                            showtimeId);

            if (showtime == null) {

                clearBookingSession(
                        session);

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=showtime_not_found");

                return;
            }

            /*
             * =================================================
             * 5. KIỂM TRA SUẤT CHIẾU
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
             * 6. KIỂM TRA PHÒNG
             * =================================================
             */

            if (!hasValidRoom(
                    showtime)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/showtimes?movieId="
                                + showtime.getMovieId()
                                + "&error=invalid_room");

                return;
            }

            /*
             * =================================================
             * 7. TÍNH LẠI TIỀN VÉ
             * =================================================
             */

            BigDecimal ticketPrice =
                    showtime.getTicketPrice();

            BigDecimal ticketTotal =
                    calculateTicketTotal(
                            ticketPrice,
                            peopleCount);

            if (ticketTotal.compareTo(
                    BigDecimal.ZERO) <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/booking/services"
                                + "?error=invalid_ticket_total");

                return;
            }

            /*
             * =================================================
             * 8. LẤY DỊCH VỤ ĐÃ CHỌN TỪ SESSION
             * =================================================
             */

            List<SelectedService> sessionSelectedServices =
                    getSelectedServices(
                            session);

            /*
             * =================================================
             * 9. LẤY LẠI DỊCH VỤ ACTIVE TỪ DATABASE
             * =================================================
             */

            List<Service> activeServices =
                    serviceService
                            .getActiveServices();

            if (activeServices == null) {

                activeServices =
                        Collections.emptyList();
            }

            Map<Integer, Service> serviceMap =
                    new HashMap<>();

            for (Service service :
                    activeServices) {

                if (service == null) {
                    continue;
                }

                if (service.getServiceId() <= 0) {
                    continue;
                }

                if (!isActiveService(service)) {
                    continue;
                }

                serviceMap.put(
                        service.getServiceId(),
                        service);
            }

            /*
             * =================================================
             * 10. KIỂM TRA LẠI DỊCH VỤ + TÍNH TIỀN
             * =================================================
             */

            List<SelectedService> validatedServices =
                    new ArrayList<>();

            BigDecimal serviceTotal =
                    BigDecimal.ZERO;

            for (SelectedService selected :
                    sessionSelectedServices) {

                if (selected == null) {
                    continue;
                }

                int serviceId =
                        selected.getServiceId();

                int quantity =
                        selected.getQuantity();

                /*
                 * Quantity đã chọn phải hợp lệ.
                 */
                if (quantity < MIN_SERVICE_QUANTITY
                        || quantity > MAX_SERVICE_QUANTITY) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/booking/services"
                                    + "?error=invalid_quantity");

                    return;
                }

                /*
                 * Kiểm tra service còn tồn tại
                 * và ACTIVE trong DB.
                 */
                Service databaseService =
                        serviceMap.get(
                                serviceId);

                if (databaseService == null) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/booking/services"
                                    + "?error=invalid_service");

                    return;
                }

                /*
                 * Giá cuối cùng luôn lấy từ DB.
                 */
                BigDecimal unitPrice =
                        databaseService.getPrice();

                if (unitPrice == null
                        || unitPrice.compareTo(
                                BigDecimal.ZERO) < 0) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/booking/services"
                                    + "?error=invalid_service");

                    return;
                }

                BigDecimal subtotal =
                        unitPrice.multiply(
                                BigDecimal.valueOf(
                                        quantity));

                serviceTotal =
                        serviceTotal.add(
                                subtotal);

                /*
                 * Tạo lại DTO sạch.
                 */
                SelectedService validated =
                        new SelectedService();

                validated.setServiceId(
                        databaseService
                                .getServiceId());

                validated.setServiceName(
                        normalize(
                                databaseService
                                        .getServiceName()));

                validated.setCategoryName(
                        normalize(
                                databaseService
                                        .getCategoryName()));

                validated.setUnitPrice(
                        unitPrice);

                validated.setQuantity(
                        quantity);

                validated.setSubtotal(
                        subtotal);

                validatedServices.add(
                        validated);
            }

            /*
             * =================================================
             * 11. TÍNH TỔNG TIỀN CUỐI
             * =================================================
             */

            BigDecimal grandTotal =
                    ticketTotal.add(
                            serviceTotal);

            if (grandTotal.compareTo(
                    BigDecimal.ZERO) <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/booking/services"
                                + "?error=invalid_total");

                return;
            }

            /*
             * =================================================
             * 12. LƯU DỮ LIỆU ĐÃ KIỂM TRA
             * =================================================
             *
             * Dữ liệu này sẽ dùng cho 24.8 tạo Ticket.
             */

            session.setAttribute(
                    "finalShowtimeId",
                    showtime.getShowtimeId());

            session.setAttribute(
                    "finalMovieId",
                    showtime.getMovieId());

            session.setAttribute(
                    "finalRoomId",
                    showtime.getRoomId());

            session.setAttribute(
                    "finalPeopleCount",
                    peopleCount);

            session.setAttribute(
                    "finalTicketPrice",
                    ticketPrice);

            session.setAttribute(
                    "finalTicketTotal",
                    ticketTotal);

            session.setAttribute(
                    "finalServiceTotal",
                    serviceTotal);

            session.setAttribute(
                    "finalGrandTotal",
                    grandTotal);

            session.setAttribute(
                    "finalSelectedServices",
                    validatedServices);

            /*
             * =================================================
             * 13. TRUYỀN SANG FINALIZE.JSP
             * =================================================
             */

            request.setAttribute(
                    "showtime",
                    showtime);

            request.setAttribute(
                    "customer",
                    user);

            request.setAttribute(
                    "peopleCount",
                    peopleCount);

            request.setAttribute(
                    "ticketPrice",
                    ticketPrice);

            request.setAttribute(
                    "ticketTotal",
                    ticketTotal);

            request.setAttribute(
                    "selectedServices",
                    validatedServices);

            request.setAttribute(
                    "serviceTotal",
                    serviceTotal);

            request.setAttribute(
                    "grandTotal",
                    grandTotal);

            request.setAttribute(
                    "pageTitle",
                    "Kiểm tra đặt vé");

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/booking/finalize.jsp")
                    .forward(
                            request,
                            response);

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/services"
                            + "?error=server_error");
        }
    }

    /*
     * =========================================================
     * TÍNH TIỀN VÉ
     * =========================================================
     */

    private BigDecimal calculateTicketTotal(
            BigDecimal ticketPrice,
            Integer peopleCount) {

        if (ticketPrice == null
                || ticketPrice.compareTo(
                        BigDecimal.ZERO) <= 0
                || peopleCount == null
                || peopleCount <= 0) {

            return BigDecimal.ZERO;
        }

        return ticketPrice.multiply(
                BigDecimal.valueOf(
                        peopleCount));
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

        if (showtime.getShowtimeId() <= 0
                || showtime.getMovieId() <= 0
                || showtime.getRoomId() <= 0) {

            return false;
        }

        if (showtime.getShowDate() == null
                || showtime.getStartTime() == null) {

            return false;
        }

        BigDecimal price =
                showtime.getTicketPrice();

        if (price == null
                || price.compareTo(
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

        return !isPastShowtime(
                showtime);
    }

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

        LocalDate date =
                showDate.toLocalDate();

        LocalTime time =
                startTime.toLocalTime();

        if (date.isBefore(today)) {
            return true;
        }

        if (date.isAfter(today)) {
            return false;
        }

        return !time.isAfter(now);
    }

    /*
     * =========================================================
     * PHÒNG
     * =========================================================
     */

    private boolean hasValidRoom(
            Showtime showtime) {

        return showtime != null
                && showtime.getRoomId() > 0;
    }

    /*
     * =========================================================
     * SERVICE
     * =========================================================
     */

    private boolean isActiveService(
            Service service) {

        return service != null
                && "ACTIVE".equals(
                        normalizeUpper(
                                service.getStatus()));
    }

    @SuppressWarnings("unchecked")
    private List<SelectedService> getSelectedServices(
            HttpSession session) {

        if (session == null) {
            return Collections.emptyList();
        }

        Object value =
                session.getAttribute(
                        "bookingSelectedServices");

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
                                + "/admin/dashboard"
                                + "?error=forbidden");

                break;

            case "STAFF":

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/dashboard"
                                + "?error=forbidden");

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
     * SESSION
     * =========================================================
     */

    private Integer getIntegerAttribute(
            HttpSession session,
            String name) {

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute(
                        name);

        if (value instanceof Integer) {
            return (Integer) value;
        }

        return null;
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
                "bookingServiceTotal");

        session.removeAttribute(
                "bookingGrandTotal");

        session.removeAttribute(
                "bookingSelectedServices");

        session.removeAttribute(
                "bookingShowtime");

        clearFinalBookingSession(
                session);
    }

    private void clearFinalBookingSession(
            HttpSession session) {

        if (session == null) {
            return;
        }

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

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeUpper(
            String value) {

        return normalize(value)
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