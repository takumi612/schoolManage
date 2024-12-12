package controller;

import models.User;
import utils.HibernateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

import java.io.IOException;


@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("userPassword");
        String error;


        try (Session sessionFactory = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();

            // Query lấy User dựa trên userName
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).where(cb.equal(root.get("userName"), username));

            User user = sessionFactory.createQuery(cr).uniqueResult();

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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
