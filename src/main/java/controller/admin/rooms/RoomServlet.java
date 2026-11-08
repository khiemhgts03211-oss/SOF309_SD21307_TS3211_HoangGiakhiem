package controller.admin.rooms;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Room;
import service.RoomService;
import service.impl.RoomServiceImpl;

@WebServlet("/admin/rooms")
public class RoomServlet
extends HttpServlet{

    private static final long serialVersionUID = 1L;

    private final RoomService roomService =
            new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        
        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");

        if (pageParam != null && !pageParam.isBlank()) {
            page = Integer.parseInt(pageParam);
        }

        List<Room> rooms;
        int totalRecord;

        if (keyword != null && !keyword.isBlank()) {

            rooms = roomService.search(keyword);

            totalRecord = rooms.size();

        } else if (status != null && !status.isBlank()) {

            rooms = roomService.findByStatus(status);

            totalRecord = rooms.size();

        } else {

            rooms = roomService.findByPage(page, pageSize);

            totalRecord = roomService.count();
        }

        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        if (sortBy != null && !sortBy.isBlank()) {

            rooms = roomService.sort(sortBy, order);

            totalRecord = rooms.size();

        }
        
        request.setAttribute("rooms", rooms);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);
        
        request.getRequestDispatcher(
                "/WEB-INF/views/admin/rooms/list.jsp")
                .forward(request, response);
    }

}