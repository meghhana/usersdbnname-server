import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class User extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String department = req.getParameter("department");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "root");

            String query = "SELECT * FROM users WHERE  department= ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, department);

            ResultSet rs = pst.executeQuery();
            System.out.println(rs);
            if (rs.next()) {
                out.println(rs.getObject(2));
            } else {
                out.println("user not found");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String method = req.getParameter("_method");

        // Check _method parameter for PUT or DELETE
        if ("put".equalsIgnoreCase(method)) {
            doPut(req, res);
            return;
        } else if ("delete".equalsIgnoreCase(method)) {
            doDelete(req, res);
            return;
        }

        // Handle POST request (create operation)
        String username = req.getParameter("username");
        String userid = req.getParameter("userid");
        String department = req.getParameter("department");
        String section = req.getParameter("section");
        String mail = req.getParameter("mail");

        String salary = req.getParameter("salary");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "root");

            // Check if the user already exists
            String checkQuery = "SELECT * FROM users WHERE department = ?";
            PreparedStatement checkPst = con.prepareStatement(checkQuery);
            checkPst.setString(1, department);
            ResultSet checkRs = checkPst.executeQuery();

            if (checkRs.next()) {
                out.println("user already exists");
            } else {
                // Insert new user
                String insertQuery = "INSERT INTO users (userid,username,department,section,mail,salary ) VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(insertQuery);
                pst.setString(1, userid);
                pst.setString(2, username);
                pst.setString(3, department);
                pst.setString(4, section);
                pst.setString(5, mail);
                pst.setString(6, salary);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    out.println("user created successfully");
                } else {
                    out.println("Error creating user");
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String username = req.getParameter("username");
        String salary = req.getParameter("salary");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "root");

            String updateQuery = "UPDATE users SET salary = ? WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, salary);
            pst.setString(2, username);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                out.println("user updated successfully");
            } else {
                out.println("Error updating user or user does not exist");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String username = req.getParameter("username");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "root");

            String deleteQuery = "DELETE FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, username);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                out.println("user deleted successfully");
            } else {
                out.println("Error deleting user or user does not exist");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
