package controller.customer.booking;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Ticket;
import model.User;

@WebServlet("/booking/success")
public class BookingSuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

        Object userValue =
                session.getAttribute("user");

        if (!(userValue instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        Object ticketValue =
                session.getAttribute(
                        "lastCreatedTicket");

        if (!(ticketValue instanceof Ticket)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/home?error=no_recent_booking");

            return;
        }

        Ticket ticket =
                (Ticket) ticketValue;

        request.setAttribute(
                "ticket",
                ticket);

        request.setAttribute(
                "showtime",
                session.getAttribute(
                        "lastBookingShowtime"));

        request.setAttribute(
                "ticketTotal",
                session.getAttribute(
                        "lastBookingTicketTotal"));

        request.setAttribute(
                "serviceTotal",
                session.getAttribute(
                        "lastBookingServiceTotal"));

        request.setAttribute(
                "grandTotal",
                session.getAttribute(
                        "lastBookingGrandTotal"));

        request.setAttribute(
                "pageTitle",
                "Đặt vé thành công");

        request.getRequestDispatcher(
                "/WEB-INF/views/customer/booking/success.jsp")
                .forward(
                        request,
                        response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}