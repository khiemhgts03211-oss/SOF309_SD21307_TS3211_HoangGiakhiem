package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.EmailUtil;

import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/forgot-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	UserService userService = new UserServiceImpl();
    	
    	String account = request.getParameter("account");

    	User user;

    	if (account.contains("@")) {
    		user = userService.findByEmail(account);
    	} else {
    	    user = userService.findByUsername(account);
    	}

    	if (user == null) {

    	    request.setAttribute("error", "Tài khoản không tồn tại.");

    	    request.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp")
    	           .forward(request, response);
    	    return;
    	}

    	String email = user.getEmail();

        String otp = String.format(
                "%06d",
                new Random().nextInt(999999));

        HttpSession session =
                request.getSession();

        session.setAttribute("otp", otp);

        session.setAttribute(
                "resetEmail",
                email);

        session.setAttribute(
                "otpTime",
                System.currentTimeMillis());
        
        try {

            EmailUtil.sendOTP(email, otp);

        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    "Không gửi được Email.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/forgot-password.jsp")
                    .forward(request, response);

            return;
        }
        
        try {

            EmailUtil.sendOTP(email, otp);

        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    "Không gửi được Email.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/forgot-password.jsp")
                    .forward(request, response);

            return;
        }

        // Chuyển sang trang nhập OTP
        response.sendRedirect(
                request.getContextPath() + "/verify-otp");
    }
}