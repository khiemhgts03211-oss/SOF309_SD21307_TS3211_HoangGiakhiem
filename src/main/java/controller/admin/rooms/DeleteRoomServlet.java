package controller.admin.rooms;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.RoomService;
import service.impl.RoomServiceImpl;

@WebServlet("/admin/rooms/delete")
public class DeleteRoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RoomService roomService =
            new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        if (roomService.hasShowtimes(id)) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/rooms?error=used");

            return;

        }

        roomService.delete(id);

        response.sendRedirect(
                request.getContextPath()
                + "/admin/rooms");

    }

}