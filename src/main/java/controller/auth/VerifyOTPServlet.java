package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/verify-otp")
public class VerifyOTPServlet extends HttpServlet {

    private static final long OTP_EXPIRE = 5 * 60 * 1000; // 5 phút

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/verify-otp.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/forgot-password");
            return;
        }

        String otp = request.getParameter("otp");

        String sessionOTP =
                (String) session.getAttribute("otp");

        Long otpTime =
                (Long) session.getAttribute("otpTime");

        if (sessionOTP == null || otpTime == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/forgot-password");

            return;
        }

        long current = System.currentTimeMillis();

        if (current - otpTime > OTP_EXPIRE) {

            session.removeAttribute("otp");
            session.removeAttribute("otpTime");

            request.setAttribute(
                    "error",
                    "Mã OTP đã hết hạn.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/verify-otp.jsp")
                    .forward(request, response);

            return;
        }

        if (!sessionOTP.equals(otp)) {

            request.setAttribute(
                    "error",
                    "Mã OTP không chính xác.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/verify-otp.jsp")
                    .forward(request, response);

            return;
        }

        session.setAttribute("otpVerified", true);

        response.sendRedirect(
                request.getContextPath()
                + "/reset-password");

    }

}