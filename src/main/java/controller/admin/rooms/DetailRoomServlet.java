package controller.admin.rooms;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Room;
import service.RoomService;
import service.impl.RoomServiceImpl;

@WebServlet("/admin/rooms/detail")
public class DetailRoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RoomService roomService =
            new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        Room room = roomService.findById(id);

        if (room == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/rooms");

            return;
        }

        request.setAttribute("room", room);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/rooms/detail.jsp")
                .forward(request, response);

    }

}