package controller.customer.booking;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

@WebServlet("/booking/services/confirm")
public class BookingServiceConfirmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MIN_PEOPLE_COUNT = 1;
    private static final int MAX_PEOPLE_COUNT = 20;

    private static final int MIN_SERVICE_QUANTITY = 0;
    private static final int MAX_SERVICE_QUANTITY = 20;

    private ShowtimeService showtimeService;
    private ServiceService serviceService;

    @Override
    public void init() throws ServletException {

        try {
            showtimeService = new ShowtimeServiceImpl();
            serviceService = new ServiceServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo service cho bước xác nhận đặt vé.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/booking/services");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

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

        User user = (User) sessionUser;

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

            clearBookingSession(session);

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=booking_session_expired");

            return;
        }

        try {

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

            if (!isBookableShowtime(showtime)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/showtimes?movieId="
                                + showtime.getMovieId()
                                + "&error=showtime_unavailable");

                return;
            }

            /*
             * =========================================
             * TÍNH TIỀN VÉ
             * =========================================
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
             * =========================================
             * LẤY DỊCH VỤ ACTIVE
             * =========================================
             */

            List<Service> activeServices =
                    serviceService.getActiveServices();

            if (activeServices == null) {
                activeServices = Collections.emptyList();
            }

            /*
             * =========================================
             * TÍNH TIỀN DỊCH VỤ
             * =========================================
             */

            List<SelectedService> selectedServices =
                    new ArrayList<>();

            BigDecimal serviceTotal =
                    BigDecimal.ZERO;

            for (Service service : activeServices) {

                if (service == null
                        || service.getServiceId() <= 0) {
                    continue;
                }

                if (!isActiveService(service)) {
                    continue;
                }

                String parameterName =
                        "service_"
                                + service.getServiceId();

                Integer quantity =
                        parseServiceQuantity(
                                request.getParameter(
                                        parameterName));

                if (quantity == null) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/booking/services"
                                    + "?error=invalid_quantity");

                    return;
                }

                if (quantity == 0) {
                    continue;
                }

                BigDecimal unitPrice =
                        service.getPrice();

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
                        calculateServiceSubtotal(
                                unitPrice,
                                quantity);

                serviceTotal =
                        serviceTotal.add(
                                subtotal);

                SelectedService selectedService =
                        new SelectedService();

                selectedService.setServiceId(
                        service.getServiceId());

                selectedService.setServiceName(
                        normalize(
                                service.getServiceName()));

                selectedService.setCategoryName(
                        normalize(
                                service.getCategoryName()));

                selectedService.setUnitPrice(
                        unitPrice);

                selectedService.setQuantity(
                        quantity);

                selectedService.setSubtotal(
                        subtotal);

                selectedServices.add(
                        selectedService);
            }

            /*
             * =========================================
             * 24.6 TỔNG TIỀN
             * =========================================
             */

            BigDecimal grandTotal =
                    calculateGrandTotal(
                            ticketTotal,
                            serviceTotal);

            /*
             * =========================================
             * LƯU SESSION
             * =========================================
             */

            session.setAttribute(
                    "bookingShowtimeId",
                    showtime.getShowtimeId());

            session.setAttribute(
                    "bookingMovieId",
                    showtime.getMovieId());

            session.setAttribute(
                    "bookingRoomId",
                    showtime.getRoomId());

            session.setAttribute(
                    "bookingPeopleCount",
                    peopleCount);

            session.setAttribute(
                    "bookingTicketPrice",
                    ticketPrice);

            session.setAttribute(
                    "bookingTicketTotal",
                    ticketTotal);

            session.setAttribute(
                    "bookingServiceTotal",
                    serviceTotal);

            session.setAttribute(
                    "bookingGrandTotal",
                    grandTotal);

            session.setAttribute(
                    "bookingSelectedServices",
                    selectedServices);

            /*
             * =========================================
             * TRUYỀN SANG JSP
             * =========================================
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
                    selectedServices);

            request.setAttribute(
                    "serviceTotal",
                    serviceTotal);

            request.setAttribute(
                    "grandTotal",
                    grandTotal);

            request.setAttribute(
                    "pageTitle",
                    "Xác nhận đặt vé");

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/booking/confirm.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/booking/services"
                            + "?error=server_error");
        }
    }

    private BigDecimal calculateTicketTotal(
            BigDecimal ticketPrice,
            Integer peopleCount) {

        if (ticketPrice == null
                || peopleCount == null
                || peopleCount <= 0) {

            return BigDecimal.ZERO;
        }

        return ticketPrice.multiply(
                BigDecimal.valueOf(
                        peopleCount));
    }

    private BigDecimal calculateServiceSubtotal(
            BigDecimal unitPrice,
            Integer quantity) {

        if (unitPrice == null
                || quantity == null
                || quantity <= 0) {

            return BigDecimal.ZERO;
        }

        return unitPrice.multiply(
                BigDecimal.valueOf(
                        quantity));
    }

    private BigDecimal calculateGrandTotal(
            BigDecimal ticketTotal,
            BigDecimal serviceTotal) {

        BigDecimal safeTicketTotal =
                ticketTotal == null
                        ? BigDecimal.ZERO
                        : ticketTotal;

        BigDecimal safeServiceTotal =
                serviceTotal == null
                        ? BigDecimal.ZERO
                        : serviceTotal;

        return safeTicketTotal.add(
                safeServiceTotal);
    }

    private boolean isBookableShowtime(
            Showtime showtime) {

        if (showtime == null
                || showtime.getShowtimeId() <= 0
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

        if (!"OPEN".equals(status)
                && !"ACTIVE".equals(status)) {

            return false;
        }

        return !isPastShowtime(showtime);
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

    private boolean isActiveService(
            Service service) {

        return service != null
                && "ACTIVE".equals(
                        normalizeUpper(
                                service.getStatus()));
    }

    private Integer parseServiceQuantity(
            String value) {

        if (value == null
                || value.isBlank()) {

            return 0;
        }

        try {

            int quantity =
                    Integer.parseInt(
                            value.trim());

            if (quantity < MIN_SERVICE_QUANTITY
                    || quantity > MAX_SERVICE_QUANTITY) {

                return null;
            }

            return quantity;

        } catch (NumberFormatException exception) {

            return null;
        }
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

    private void clearBookingSession(
            HttpSession session) {

        if (session == null) {
            return;
        }

        session.removeAttribute("bookingShowtimeId");
        session.removeAttribute("bookingMovieId");
        session.removeAttribute("bookingRoomId");
        session.removeAttribute("bookingPeopleCount");
        session.removeAttribute("bookingTicketPrice");
        session.removeAttribute("bookingTicketTotal");
        session.removeAttribute("bookingServiceTotal");
        session.removeAttribute("bookingGrandTotal");
        session.removeAttribute("bookingSelectedServices");
        session.removeAttribute("bookingShowtime");
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeUpper(
            String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

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

    public static class SelectedService
            implements Serializable {

        private static final long serialVersionUID = 1L;

        private int serviceId;
        private String serviceName;
        private String categoryName;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal subtotal;

        public SelectedService() {
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(
                int serviceId) {

            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(
                String serviceName) {

            this.serviceName =
                    serviceName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(
                String categoryName) {

            this.categoryName =
                    categoryName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(
                BigDecimal unitPrice) {

            this.unitPrice =
                    unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(
                int quantity) {

            this.quantity =
                    quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(
                BigDecimal subtotal) {

            this.subtotal =
                    subtotal;
        }
    }
}