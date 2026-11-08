package controller.admin.tickets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Ticket;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets/export")
public class ExportTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Ticket> tickets =
                ticketService.getAllTickets();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("Tickets");

        Row header =
                sheet.createRow(0);

        String[] columns = {
                "ID",
                "Booking Code",
                "Customer",
                "Movie",
                "Room",
                "Showtime",
                "People",
                "Total Price",
                "Status",
                "Created At"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

        }

        int rowNum = 1;

        for (Ticket t : tickets) {

            Row row =
                    sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(t.getTicketId());

            row.createCell(1)
                    .setCellValue(t.getBookingCode());

            row.createCell(2)
                    .setCellValue(t.getCustomerName());

            row.createCell(3)
                    .setCellValue(t.getMovieTitle());

            row.createCell(4)
                    .setCellValue(t.getRoomName());

            row.createCell(5)
                    .setCellValue(
                            String.valueOf(t.getShowtime()));

            row.createCell(6)
                    .setCellValue(t.getPeopleCount());

            BigDecimal price =
                    t.getTotalPrice();

            row.createCell(7)
                    .setCellValue(price.doubleValue());

            row.createCell(8)
                    .setCellValue(t.getStatus());

            row.createCell(9)
                    .setCellValue(
                            String.valueOf(t.getCreatedAt()));

        }

        for (int i = 0; i < columns.length; i++) {

            sheet.autoSizeColumn(i);

        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=tickets.xlsx");

        workbook.write(response.getOutputStream());

        workbook.close();

    }

}