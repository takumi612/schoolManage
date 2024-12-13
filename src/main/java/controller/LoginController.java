package controller;

import models.User;
import services.implement.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("userPassword");
        String error;


        // Lấy User dựa trên userName
        UserService userService = new UserService();
        User user = userService.selectByUserName(username);

//            // Mã hóa mật khẩu
//            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//            System.out.println("Mật khẩu đã mã hóa: " + hashedPassword);
//
//            // Kiểm tra mật khẩu
//            boolean passwordMatches = BCrypt.checkpw(password, hashedPassword);

        if (user != null && password.equals(user.getUserPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if(!user.getUserRole()){
                response.sendRedirect("studentInfo");
            }else{
                response.sendRedirect("studentAdmin");
            }
        } else {
            request.setAttribute("error", "Invalid username or password" + username + "    " + password);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

