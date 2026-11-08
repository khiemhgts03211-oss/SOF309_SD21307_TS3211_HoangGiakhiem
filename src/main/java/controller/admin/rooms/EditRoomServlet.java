package controller.admin.rooms;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Room;
import service.RoomService;
import service.impl.RoomServiceImpl;

@WebServlet("/admin/rooms/edit")
public class EditRoomServlet extends HttpServlet {

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

        request.setAttribute("room", room);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/rooms/edit.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Room room = new Room();

        room.setRoomId(
                Integer.parseInt(
                        request.getParameter("roomId")));

        room.setRoomName(
                request.getParameter("roomName"));

        room.setCapacity(
                Integer.parseInt(
                        request.getParameter("capacity")));

        room.setStatus(
                request.getParameter("status"));

        boolean success =
                roomService.update(room);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/rooms");

        } else {

            request.setAttribute(
                    "room",
                    room);

            request.setAttribute(
                    "error",
                    "Cập nhật thất bại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/rooms/edit.jsp")
                    .forward(request, response);

        }

    }

}