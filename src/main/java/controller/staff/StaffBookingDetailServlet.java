package controller.staff;

import java.io.IOException;
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
import model.TicketDetail;
import model.User;
import service.PaymentService;
import service.TicketDetailService;
import service.TicketService;
import service.impl.PaymentServiceImpl;
import service.impl.TicketDetailServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/booking-detail")
public class StaffBookingDetailServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TicketService ticketService;

    private TicketDetailService ticketDetailService;

    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {

        ticketService =
                new TicketServiceImpl();

        ticketDetailService =
                new TicketDetailServiceImpl();

        paymentService =
                new PaymentServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("user");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        String ticketIdValue =
                request.getParameter("ticketId");

        if (ticketIdValue == null
                || ticketIdValue.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=invalid_id");

            return;
        }

        try {

            int ticketId =
                    Integer.parseInt(
                            ticketIdValue.trim());

            if (ticketId <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/bookings"
                                + "?error=invalid_id");

                return;
            }

            Ticket ticket =
                    ticketService.getTicketById(
                            ticketId);

            if (ticket == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/bookings"
                                + "?error=not_found");

                return;
            }

            /*
             * Dịch vụ đi kèm
             */
            List<TicketDetail> ticketDetails =
                    ticketDetailService
                            .getTicketDetailsByTicket(
                                    ticketId);

            if (ticketDetails == null) {

                ticketDetails =
                        Collections.emptyList();
            }

            /*
             * Danh sách thanh toán của vé
             */
            List<Payment> payments =
                    paymentService
                            .getPaymentsByTicket(
                                    ticketId);

            if (payments == null) {

                payments =
                        Collections.emptyList();
            }

            boolean paymentPaid = false;

            Payment latestPayment = null;

            for (Payment payment : payments) {

                if (latestPayment == null
                        || payment.getPaymentId()
                                > latestPayment
                                        .getPaymentId()) {

                    latestPayment = payment;
                }

                if ("PAID".equalsIgnoreCase(
                        payment.getStatus())) {

                    paymentPaid = true;
                }
            }

            request.setAttribute(
                    "ticket",
                    ticket);

            request.setAttribute(
                    "ticketDetails",
                    ticketDetails);

            request.setAttribute(
                    "payments",
                    payments);

            request.setAttribute(
                    "latestPayment",
                    latestPayment);

            request.setAttribute(
                    "paymentPaid",
                    paymentPaid);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/"
                    + "bookings/detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=invalid_id");

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải thông tin đặt phòng.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/"
                    + "bookings/detail.jsp")
                    .forward(request, response);
        }
    }
}