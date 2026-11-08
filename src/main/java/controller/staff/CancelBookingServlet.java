package controller.staff;

import java.io.IOException;
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

@WebServlet("/staff/cancel-booking")
public class CancelBookingServlet extends HttpServlet {

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

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        /*
         * Project hiện tại lưu user đăng nhập
         * bằng session attribute "user".
         */
        User loggedInUser =
                (User) session.getAttribute("user");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

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
                    Integer.parseInt(ticketIdValue.trim());

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

            String currentStatus = ticket.getStatus();

            /*
             * Vé đã hủy rồi thì không cập nhật lại.
             */
            if ("CANCELLED".equalsIgnoreCase(currentStatus)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/booking-detail"
                                + "?ticketId="
                                + ticketId
                                + "&error=already_cancelled");

                return;
            }

            /*
             * Không cho hủy vé đã check-in.
             */
            if ("CHECKED_IN".equalsIgnoreCase(currentStatus)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/booking-detail"
                                + "?ticketId="
                                + ticketId
                                + "&error=checked_in");

                return;
            }

            /*
             * Cập nhật trạng thái vé.
             */
            ticketService.updateStatus(
                    ticketId,
                    "CANCELLED");

            /*
             * Nếu payment vẫn đang PENDING thì hủy luôn.
             * Không tự động đổi payment PAID vì cần quy trình hoàn tiền.
             */
            List<Payment> payments =
                    paymentService.getPaymentsByTicket(ticketId);

            if (payments != null) {

                for (Payment payment : payments) {

                    if (payment != null
                            && "PENDING".equalsIgnoreCase(
                                    payment.getStatus())) {

                        paymentService.updatePaymentStatus(
                                payment.getPaymentId(),
                                "CANCELLED");
                    }
                }
            }

            /*
             * Kiểm tra cập nhật thành công.
             */
            Ticket updatedTicket =
                    ticketService.getTicketById(ticketId);

            if (updatedTicket == null
                    || !"CANCELLED".equalsIgnoreCase(
                            updatedTicket.getStatus())) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/booking-detail"
                                + "?ticketId="
                                + ticketId
                                + "&error=update_failed");

                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/booking-detail"
                            + "?ticketId="
                            + ticketId
                            + "&success=cancelled");

        } catch (NumberFormatException exception) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=invalid_id");

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/bookings"
                            + "?error=server_error");
        }
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