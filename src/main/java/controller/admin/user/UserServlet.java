package controller.admin.user;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.PasswordUtil;

@WebServlet("/admin/users")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
    	
        UserService userService = new UserServiceImpl();

        String keyword = request.getParameter("keyword");
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String sortBy = request.getParameter("sortBy");
    	String order = request.getParameter("order");
    	
        List<User> users;

        if (keyword != null && !keyword.isBlank()) {

            users = userService.search(keyword);

        } else if ((role != null && !role.isBlank())
                || (status != null && !status.isBlank())) {

            users = userService.filter(role, status);

        } else if (sortBy != null && !sortBy.isBlank()) {

            users = userService.sort(sortBy, order);

        } else {

            users = userService.findAll();

        }

        request.setAttribute("users", users);

        request.setAttribute("keyword", keyword);
        request.setAttribute("role", role);
        request.setAttribute("status", status);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);
        
        request.getRequestDispatcher(
                "/WEB-INF/views/admin/users/list.jsp")
                .forward(request, response);

    }
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	UserService userService = new UserServiceImpl();
    	
    	String id = request.getParameter("userId");

    	User user = new User();

    	if (id != null && !id.isBlank()) {

    	    user.setUserId(Integer.parseInt(id));

    	}

    	user.setUsername(request.getParameter("username"));
    	user.setFullName(request.getParameter("fullName"));
    	user.setEmail(request.getParameter("email"));
    	user.setPhone(request.getParameter("phone"));
    	user.setRole(request.getParameter("role"));
    	user.setStatus(request.getParameter("status"));
    	
    	String password = request.getParameter("password");

    	if (password != null && !password.isBlank()) {

    	    // Có nhập mật khẩu mới
    	    user.setPasswordHash(PasswordUtil.hash(password));

    	} else if (user.getUserId() > 0) {

    	    // Edit nhưng không đổi mật khẩu
    	    User oldUser = userService.findById(user.getUserId());

    	    user.setPasswordHash(oldUser.getPasswordHash());

    	}

    	boolean success;

    	if (user.getUserId() > 0) {

    	    success = userService.update(user);

    	} else {

    	    success = userService.save(user);

    	}

    	if (success) {

    	    response.sendRedirect(
    	            request.getContextPath() + "/admin/users");
    	    return;

    	} else {

    	    request.setAttribute("error", "Lưu người dùng thất bại");

    	    request.setAttribute("user", user);

    	    String page = (user.getUserId() > 0)
    	            ? "/WEB-INF/views/admin/users/edit.jsp"
    	            : "/WEB-INF/views/admin/users/add.jsp";

    	    request.getRequestDispatcher(page)
    	           .forward(request, response);
    	}
    	
    	
    }
    
}