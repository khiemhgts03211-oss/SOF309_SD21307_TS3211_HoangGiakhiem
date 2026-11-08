package controller.admin.rooms;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Room;
import service.RoomService;
import service.impl.RoomServiceImpl;

@WebServlet("/admin/rooms/add")
public class AddRoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RoomService roomService =
            new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/rooms/add.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String roomName = request.getParameter("roomName");
        String capacityStr = request.getParameter("capacity");
        String status = request.getParameter("status");

        if (roomService.existsRoomName(roomName)) {

            request.setAttribute("error",
                    "Room Name đã tồn tại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/rooms/add.jsp")
                    .forward(request, response);

            return;
        }

        int capacity;

        try {

            capacity = Integer.parseInt(capacityStr);

        } catch (Exception e) {

            request.setAttribute("error",
                    "Capacity không hợp lệ.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/rooms/add.jsp")
                    .forward(request, response);

            return;
        }

        if (capacity <= 0) {

            request.setAttribute("error",
                    "Capacity phải lớn hơn 0.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/rooms/add.jsp")
                    .forward(request, response);

            return;
        }

        Room room = new Room();

        room.setRoomName(roomName);
        room.setCapacity(capacity);
        room.setStatus(status);

        boolean success = roomService.save(room);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/rooms");

        } else {

            request.setAttribute("error",
                    "Thêm phòng thất bại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/rooms/add.jsp")
                    .forward(request, response);

        }

    }

}