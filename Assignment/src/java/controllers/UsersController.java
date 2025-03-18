/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.UsersDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.Users;

/**
 *
 * @author admin
 */
public class UsersController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/login":
                showLoginPage(request, response);
                break;
            case "/register":
                showRegisterPage(request, response);
                break;
            case "/user":
                showUserProfile(request, response);
                break;
            default:
                response.sendRedirect("login");
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            case "/user":
                updateUserProfile(request, response);
                break;
            default:
                response.sendRedirect("login");
                break;
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsersDAO da = new UsersDAO();
        Users u = da.login(username, password);
        String msg = "Username or password is wrong!";
        if (u != null) {
            u.setId(da.getUserIdByUsername(username));
            HttpSession s = request.getSession();
            s.setAttribute("id", u.getId());
            s.setAttribute("username", u.getUsername());
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");
        String email = request.getParameter("email");

        UsersDAO da = new UsersDAO();
        StringBuilder msg = new StringBuilder();

        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || cpassword == null || cpassword.isEmpty()
                || email == null || email.isEmpty()) {
            msg.append("Please enter all information!<br>");
        }

        if (!password.equals(cpassword)) {
            msg.append("Password and confirm password do not match!<br>");
        }

        if (da.getUser(username)) {
            msg.append("Username already exists!<br>");
        }

        if (msg.length() > 0) {
            request.setAttribute("msg", msg.toString());
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("cpassword", cpassword);
            request.setAttribute("email", email);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        da.addUser(new Users(username, email, password));
        HttpSession s = request.getSession();
        s.setAttribute("SessionRegister", "Registration successfully! Please login!");
        s.setAttribute("username", username);
        s.setAttribute("password", password);
        response.sendRedirect("login");
    }

    private void showUserProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession();
        String username = (String) s.getAttribute("username");
        UsersDAO uda = new UsersDAO();
        Users u = uda.getUsersByUsername(username);
        Integer userId = (Integer) s.getAttribute("id");
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }
        request.setAttribute("user", u);
        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession s = request.getSession();
        String username = (String) s.getAttribute("username");
        UsersDAO uda = new UsersDAO();
        Users u = uda.getUsersByUsername(username);
        Integer userId = (Integer) s.getAttribute("id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        int id = userId;
        request.setAttribute("user", u);

        String email = request.getParameter("email");
        String balanceStr = request.getParameter("balance");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("confirmPassword");
        java.sql.Timestamp createdTime = u.getCreatedTime();
        boolean isActive = u.getActive();
        Users newUser = null;

        BigDecimal balance = u.getBalance();
        if (balanceStr != null && !balanceStr.isEmpty()) {
            try {
                balance = new BigDecimal(balanceStr);
            } catch (NumberFormatException e) {
                request.setAttribute("msg", "Invalid balance format!");
                request.getRequestDispatcher("user.jsp").forward(request, response);
                return;
            }
        }

        if (request.getParameter("update") != null) {
            String msg = "";

            if (email == null || email.isEmpty()) {
                msg = "Email cannot be empty!";
            } else if (password != null && !password.isEmpty() && cpassword != null && !cpassword.isEmpty()) {
                if (!password.equals(cpassword)) {
                    msg = "Password confirmation does not match!";
                }
            } else {
                password = u.getPassword();
            }

            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("user.jsp").forward(request, response);
                return;
            }

            newUser = new Users(id, username, email, password, balance, createdTime, isActive);
            boolean updated = uda.updateUsersAllInfo(newUser);

            if (updated) {
                request.setAttribute("msg", "Updated successfully!");
            } else {
                request.setAttribute("msg", "Update error!");
            }
            request.setAttribute("user", newUser);
        }

        if (request.getParameter("delete") != null) {
            uda.deleteUsers(id);
        }

        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

}
