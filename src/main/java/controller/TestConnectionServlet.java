package controller;

import java.io.IOException;
import java.sql.Connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBConnection;

@WebServlet("/test-db")
public class TestConnectionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	System.out.println("[TestConnectionServlet]");
    	
        response.setContentType("text/html;charset=UTF-8");

        try (Connection connection = DBConnection.getConnection()) {
            response.getWriter().println("<h2>Kết nối SQL Server thành công!</h2>");
            response.getWriter().println("<p>Database: MovieTicketManagement</p>");
        } catch (Exception e) {
            response.getWriter().println("<h2>Kết nối SQL Server thất bại!</h2>");
            response.getWriter().println("<pre>" + e.getMessage() + "</pre>");
        }
    }
}