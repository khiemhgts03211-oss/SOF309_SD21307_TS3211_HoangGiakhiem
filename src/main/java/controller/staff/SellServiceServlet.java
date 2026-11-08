package controller.staff;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Service;
import model.Ticket;
import model.TicketDetail;

import service.ServiceService;
import service.TicketDetailService;
import service.TicketService;

import service.impl.ServiceServiceImpl;
import service.impl.TicketDetailServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/sell-service")
public class SellServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    private final ServiceService serviceService =
            new ServiceServiceImpl();

    private final TicketDetailService ticketDetailService =
            new TicketDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String bookingCode =
                request.getParameter("bookingCode");

        if (bookingCode != null && !bookingCode.isBlank()) {

            Ticket ticket =
                    ticketService.getTicketByBookingCode(bookingCode);

            if (ticket == null) {

                request.setAttribute(
                        "error",
                        "Booking code does not exist.");

            } else {

                request.setAttribute("ticket", ticket);

                List<TicketDetail> details =
                        ticketDetailService.getTicketDetailsByTicket(
                                ticket.getTicketId());

                request.setAttribute(
                        "details",
                        details);

            }

        }

        List<Service> services =
                serviceService.getAllServices();

        request.setAttribute(
                "services",
                services);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/sell-service.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Integer ticketId =
                Integer.parseInt(
                        request.getParameter("ticketId"));

        Integer serviceId =
                Integer.parseInt(
                        request.getParameter("serviceId"));

        Integer quantity =
                Integer.parseInt(
                        request.getParameter("quantity"));

        Service service =
                serviceService.getServiceById(serviceId);

        if (service == null) {

            request.setAttribute(
                    "error",
                    "Service not found.");

            doGet(request, response);

            return;

        }

        BigDecimal amount =
                service.getPrice()
                        .multiply(
                                BigDecimal.valueOf(quantity));

        TicketDetail detail =
                new TicketDetail();

        detail.setTicketId(ticketId);
        detail.setServiceId(serviceId);
        detail.setQuantity(quantity);
        detail.setAmount(amount);

        boolean inserted =
                ticketDetailService.createTicketDetail(detail);

        if (inserted) {

            Ticket ticket =
                    ticketService.getTicketById(ticketId);

            ticket.setTotalPrice(
                    ticket.getTotalPrice().add(amount));

            ticketService.updateTicket(ticket);

            request.setAttribute(
                    "success",
                    "Service sold successfully.");

            request.setAttribute(
                    "ticket",
                    ticket);

            List<TicketDetail> details =
                    ticketDetailService.getTicketDetailsByTicket(
                            ticketId);

            request.setAttribute(
                    "details",
                    details);

        } else {

            request.setAttribute(
                    "error",
                    "Cannot sell service.");

        }

        List<Service> services =
                serviceService.getAllServices();

        request.setAttribute(
                "services",
                services);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/sell-service.jsp")
                .forward(request, response);

    }

}