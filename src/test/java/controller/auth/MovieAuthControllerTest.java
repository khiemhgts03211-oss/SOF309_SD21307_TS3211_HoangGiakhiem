package controller.auth;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import service.impl.UserServiceImpl;
import utils.PasswordUtil;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Timeout(10)
class MovieAuthControllerTest {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    final HttpServletResponse response = mock(HttpServletResponse.class);
    final HttpSession session = mock(HttpSession.class);
    final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    @BeforeEach void configureRequest() {
        when(request.getContextPath()).thenReturn("/MovieTicketManagement");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        param("fullName", "QA Movie");
        param("username", "movie_user");
        param("email", "movie@example.com");
        param("phone", "0912345678");
        param("password", "Aa123456");
        param("confirmPassword", "Aa123456");
    }

    void param(String key, String value) {
        when(request.getParameter(key)).thenReturn(value);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "username|abc|false", "username|abcd|true", "username|abcde|true",
        "username|a123456789012345678|true", "username|a1234567890123456789|true",
        "username|a12345678901234567890|false", "username|aa-aa|false",
        "password|Aa12345|false", "password|Aa123456|true", "password|Aa1234567|true",
        "password|aa123456|false", "password|AA123456|false", "password|Aabcdefg|false",
        "phone|091234567|false", "phone|0912345678|true", "phone|09123456789|false",
        "phone|84912345678|true", "phone|0212345678|false", "phone|''|true",
        "email|movie.example.com|false", "email|movie@example.com|true",
        "fullName|' '|false", "confirmPassword|Other123|false"
    }, delimiter = '|')
    void registrationValidation(String field, String input, boolean accepted) throws Exception {
        param(field, input);
        if (field.equals("password")) param("confirmPassword", input);
        try (var services = mockConstruction(UserServiceImpl.class, (service, context) ->
                when(service.save(any(User.class))).thenReturn(true))) {
            new RegisterServlet().doPost(request, response);
            var service = services.constructed().get(0);
            if (accepted) {
                var saved = ArgumentCaptor.forClass(User.class);
                verify(service).save(saved.capture());
                User user = saved.getValue();
                assertEquals("ACTIVE", user.getStatus());
                assertEquals("CUSTOMER", user.getRole());
                assertTrue(PasswordUtil.verify(field.equals("password") ? input : "Aa123456", user.getPasswordHash()));
                verify(session).setAttribute(eq("success"), anyString());
                verify(response).sendRedirect("/MovieTicketManagement/login");
            } else {
                verify(service, never()).save(any());
                verify(request).setAttribute(eq("error"), anyString());
                verify(dispatcher).forward(request, response);
                verify(response, never()).sendRedirect(anyString());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "email", "phone", "save"})
    void registrationServiceRejection(String scenario) throws Exception {
        try (var services = mockConstruction(UserServiceImpl.class, (service, context) -> {
            when(service.existsUsername("movie_user")).thenReturn(scenario.equals("username"));
            when(service.existsEmail("movie@example.com")).thenReturn(scenario.equals("email"));
            when(service.existsPhone("0912345678")).thenReturn(scenario.equals("phone"));
        })) {
            new RegisterServlet().doPost(request, response);
            verify(services.constructed().get(0), times(scenario.equals("save") ? 1 : 0)).save(any());
            String error = switch (scenario) {
                case "username" -> "Tên đăng nhập đã tồn tại.";
                case "email" -> "Email đã được sử dụng.";
                case "phone" -> "Số điện thoại đã được sử dụng.";
                default -> "Không thể tạo tài khoản.";
            };
            verify(request).setAttribute("error", error);
            verify(dispatcher).forward(request, response);
        }
    }

    @ParameterizedTest
    @CsvSource({"username,99,true", "username,100,true", "username,101,false",
                "password,254,true", "password,255,true", "password,256,false",
                "username,0,false", "password,0,false"})
    void loginLengthLimits(String field, int length, boolean reachesService) throws Exception {
        String value = "x".repeat(length);
        param(field, value);
        try (var services = mockConstruction(UserServiceImpl.class)) {
            var controller = new LoginServlet();
            controller.init();
            controller.doPost(request, response);
            var service = services.constructed().get(0);
            verify(service, times(reachesService ? 1 : 0)).login(
                    field.equals("username") ? value : "movie_user",
                    field.equals("password") ? value : "Aa123456");
            verify(request).setAttribute(eq("errorMessage"), anyString());
            verify(dispatcher).forward(request, response);
        }
    }

    @ParameterizedTest
    @CsvSource({"CUSTOMER,/home", "STAFF,/staff/dashboard", "ADMIN,/admin/dashboard"})
    void loginByRole(String role, String destination) throws Exception {
        User user = new User();
        user.setRole(role);
        user.setStatus("ACTIVE");
        param("username", " movie_user ");
        HttpSession old = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(old);
        try (var services = mockConstruction(UserServiceImpl.class, (service, context) ->
                when(service.login("movie_user", "Aa123456")).thenReturn(user))) {
            var controller = new LoginServlet();
            controller.init();
            controller.doPost(request, response);
            verify(services.constructed().get(0)).login("movie_user", "Aa123456");
            verify(old).invalidate();
            verify(session).setAttribute("user", user);
            verify(session).setMaxInactiveInterval(1800);
            verify(response).sendRedirect("/MovieTicketManagement" + destination);
        }
    }

    @ParameterizedTest
    @CsvSource({"CUSTOMER,LOCKED", "UNKNOWN,ACTIVE"})
    void loginRejectsStatusOrRole(String role, String status) throws Exception {
        User user = new User();
        user.setRole(role);
        user.setStatus(status);
        try (var services = mockConstruction(UserServiceImpl.class, (service, context) ->
                when(service.login("movie_user", "Aa123456")).thenReturn(user))) {
            var controller = new LoginServlet();
            controller.init();
            controller.doPost(request, response);
            verify(services.constructed().get(0)).login("movie_user", "Aa123456");
            verify(request).setAttribute("errorMessage", status.equals("LOCKED")
                    ? "Tài khoản đã bị khóa hoặc không còn hoạt động." : "Vai trò tài khoản không hợp lệ.");
            verify(dispatcher).forward(request, response);
            verifyNoInteractions(session);
        }
    }
}
