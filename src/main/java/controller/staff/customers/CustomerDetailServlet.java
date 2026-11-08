package controller.staff.customers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Payment;
import model.Ticket;
import model.User;
import service.PaymentService;
import service.TicketService;
import service.UserService;
import service.impl.PaymentServiceImpl;
import service.impl.TicketServiceImpl;
import service.impl.UserServiceImpl;

@WebServlet("/staff/customers/detail")
public class CustomerDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;
    private TicketService ticketService;
    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {

        userService = new UserServiceImpl();
        ticketService = new TicketServiceImpl();
        paymentService = new PaymentServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        if (!canManageCustomers(loggedInUser)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này.");

            return;
        }

        Integer customerId =
                parsePositiveInteger(
                        request.getParameter("id"));

        if (customerId == null) {

            redirectToList(
                    request,
                    response,
                    "error=invalid_id");

            return;
        }

        try {

            User customer =
                    userService.findById(customerId);

            if (customer == null) {

                redirectToList(
                        request,
                        response,
                        "error=not_found");

                return;
            }

            if (!isCustomer(customer)) {

                redirectToList(
                        request,
                        response,
                        "error=not_customer");

                return;
            }

            List<Ticket> tickets =
                    ticketService.getTicketsByUser(
                            customerId);

            if (tickets == null) {
                tickets = Collections.emptyList();
            }

            int bookedTickets = 0;
            int paidTickets = 0;
            int confirmedTickets = 0;
            int checkedInTickets = 0;
            int cancelledTickets = 0;

            BigDecimal totalBookingAmount =
                    BigDecimal.ZERO;

            BigDecimal totalPaidAmount =
                    BigDecimal.ZERO;

            Timestamp firstBookingDate = null;
            Timestamp latestBookingDate = null;

            for (Ticket ticket : tickets) {

                if (ticket == null) {
                    continue;
                }

                if (ticket.getTotalPrice() != null) {

                    totalBookingAmount =
                            totalBookingAmount.add(
                                    ticket.getTotalPrice());
                }

                String ticketStatus =
                        normalize(ticket.getStatus())
                                .toUpperCase();

                switch (ticketStatus) {

                    case "BOOKED":
                        bookedTickets++;
                        break;

                    case "PAID":
                        paidTickets++;
                        break;

                    case "CONFIRMED":
                        confirmedTickets++;
                        break;

                    case "CHECKED_IN":
                        checkedInTickets++;
                        break;

                    case "CANCELLED":
                        cancelledTickets++;
                        break;

                    default:
                        break;
                }

                Timestamp createdAt =
                        ticket.getCreatedAt();

                if (createdAt != null) {

                    if (firstBookingDate == null
                            || createdAt.before(
                                    firstBookingDate)) {

                        firstBookingDate = createdAt;
                    }

                    if (latestBookingDate == null
                            || createdAt.after(
                                    latestBookingDate)) {

                        latestBookingDate = createdAt;
                    }
                }

                List<Payment> payments =
                        paymentService.getPaymentsByTicket(
                                ticket.getTicketId());

                if (payments == null) {
                    continue;
                }

                for (Payment payment : payments) {

                    if (payment != null
                            && "PAID".equalsIgnoreCase(
                                    payment.getStatus())
                            && payment.getAmount() != null) {

                        totalPaidAmount =
                                totalPaidAmount.add(
                                        payment.getAmount());
                    }
                }
            }

            BigDecimal averageBookingAmount =
                    BigDecimal.ZERO;

            if (!tickets.isEmpty()) {

                averageBookingAmount =
                        totalBookingAmount.divide(
                                BigDecimal.valueOf(
                                        tickets.size()),
                                2,
                                RoundingMode.HALF_UP);
            }

            long memberDays = 0;

            if (customer.getCreatedAt() != null) {

                LocalDate joinedDate =
                        customer.getCreatedAt()
                                .toLocalDateTime()
                                .toLocalDate();

                memberDays =
                        ChronoUnit.DAYS.between(
                                joinedDate,
                                LocalDate.now());

                if (memberDays < 0) {
                    memberDays = 0;
                }
            }

            request.setAttribute("customer", customer);
            request.setAttribute("tickets", tickets);

            request.setAttribute(
                    "totalTickets",
                    tickets.size());

            request.setAttribute(
                    "bookedTickets",
                    bookedTickets);

            request.setAttribute(
                    "paidTickets",
                    paidTickets);

            request.setAttribute(
                    "confirmedTickets",
                    confirmedTickets);

            request.setAttribute(
                    "checkedInTickets",
                    checkedInTickets);

            request.setAttribute(
                    "cancelledTickets",
                    cancelledTickets);

            request.setAttribute(
                    "totalBookingAmount",
                    totalBookingAmount);

            request.setAttribute(
                    "totalPaidAmount",
                    totalPaidAmount);

            request.setAttribute(
                    "averageBookingAmount",
                    averageBookingAmount);

            request.setAttribute(
                    "firstBookingDate",
                    firstBookingDate);

            request.setAttribute(
                    "latestBookingDate",
                    latestBookingDate);

            request.setAttribute(
                    "memberDays",
                    memberDays);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/customers/detail.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải chi tiết khách hàng.");

            request.setAttribute(
                    "tickets",
                    Collections.emptyList());

            request.setAttribute("totalTickets", 0);
            request.setAttribute("bookedTickets", 0);
            request.setAttribute("paidTickets", 0);
            request.setAttribute("confirmedTickets", 0);
            request.setAttribute("checkedInTickets", 0);
            request.setAttribute("cancelledTickets", 0);
            request.setAttribute("memberDays", 0);

            request.setAttribute(
                    "totalBookingAmount",
                    BigDecimal.ZERO);

            request.setAttribute(
                    "totalPaidAmount",
                    BigDecimal.ZERO);

            request.setAttribute(
                    "averageBookingAmount",
                    BigDecimal.ZERO);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/customers/detail.jsp")
                    .forward(request, response);
        }
    }

    private User getLoggedInUser(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute("user");

        return value instanceof User
                ? (User) value
                : null;
    }

    private boolean canManageCustomers(User user) {

        if (user == null || user.getRole() == null) {
            return false;
        }

        String role =
                String.valueOf(user.getRole())
                        .trim();

        return "STAFF".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }

    private boolean isCustomer(User user) {

        return user != null
                && user.getRole() != null
                && "CUSTOMER".equalsIgnoreCase(
                        String.valueOf(
                                user.getRole()).trim());
    }

    private Integer parsePositiveInteger(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            int id = Integer.parseInt(value.trim());

            return id > 0 ? id : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response,
            String parameter)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/customers?"
                        + parameter);
    }
}