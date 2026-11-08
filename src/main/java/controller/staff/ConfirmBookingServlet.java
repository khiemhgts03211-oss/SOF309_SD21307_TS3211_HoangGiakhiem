package controller.staff;

import java.io.IOException;
import java.math.BigDecimal;
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
import service.impl.PaymentServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/confirm-booking")
public class ConfirmBookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TicketService ticketService;
    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {

        ticketService = new TicketServiceImpl();
        paymentService = new PaymentServiceImpl();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("user");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        String ticketIdParam =
                request.getParameter("ticketId");

        if (ticketIdParam == null
                || ticketIdParam.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=invalid_id");

            return;
        }

        try {

            int ticketId =
                    Integer.parseInt(ticketIdParam.trim());

            if (ticketId <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/bookings"
                                + "?error=invalid_id");

                return;
            }

            Ticket ticket =
                    ticketService.getTicketById(ticketId);

            if (ticket == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/bookings"
                                + "?error=not_found");

                return;
            }

            String ticketStatus = ticket.getStatus();

            if ("CANCELLED".equalsIgnoreCase(ticketStatus)) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=cancelled");

                return;
            }

            if ("CHECKED_IN".equalsIgnoreCase(ticketStatus)) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=checked_in");

                return;
            }

            if ("CONFIRMED".equalsIgnoreCase(ticketStatus)) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=already_confirmed");

                return;
            }

            List<Payment> payments =
                    paymentService.getPaymentsByTicket(ticketId);

            if (payments == null || payments.isEmpty()) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=no_payment");

                return;
            }

            BigDecimal paidAmount = BigDecimal.ZERO;
            boolean hasPaidPayment = false;

            for (Payment payment : payments) {

                if (payment == null) {
                    continue;
                }

                if ("PAID".equalsIgnoreCase(
                        payment.getStatus())) {

                    hasPaidPayment = true;

                    if (payment.getAmount() != null) {

                        paidAmount =
                                paidAmount.add(
                                        payment.getAmount());
                    }
                }
            }

            if (!hasPaidPayment) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=not_paid");

                return;
            }

            BigDecimal ticketTotal =
                    ticket.getTotalPrice() == null
                            ? BigDecimal.ZERO
                            : ticket.getTotalPrice();

            if (paidAmount.compareTo(ticketTotal) < 0) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=insufficient_payment");

                return;
            }

            ticketService.updateStatus(
                    ticketId,
                    "CONFIRMED");

            Ticket updatedTicket =
                    ticketService.getTicketById(ticketId);

            if (updatedTicket == null
                    || !"CONFIRMED".equalsIgnoreCase(
                            updatedTicket.getStatus())) {

                redirectDetail(
                        request,
                        response,
                        ticketId,
                        "error=update_failed");

                return;
            }

            redirectDetail(
                    request,
                    response,
                    ticketId,
                    "success=confirmed");

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=invalid_id");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=server_error");
        }
    }

    private void redirectDetail(
            HttpServletRequest request,
            HttpServletResponse response,
            int ticketId,
            String parameter)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/booking-detail"
                        + "?ticketId="
                        + ticketId
                        + "&"
                        + parameter);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/bookings");
    }
}