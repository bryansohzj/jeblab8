package jeb;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//




import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.validator.routines.EmailValidator;
//
///**
// *
// * @author 184461L
// */
@WebServlet("/validate")
public class ValidationServlet extends HttpServlet {
    @Resource(name="jdbc/jed")
    private DataSource dsLab8;
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException{
         PrintWriter out=response.getWriter();  
        String emailaddress = request.getParameter("emailaddress");
        
        EmailValidator emailValidator = EmailValidator.getInstance();
        
        if(emailValidator.isValid(emailaddress))
        {
            boolean unique = true;
            
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultset = null;
            
            try{
                connection = dsLab8.getConnection();
                preparedStatement = connection.prepareStatement("SELECT count(*) FROM customer WHERE emailaddress = ?");
                preparedStatement.setString(1, emailaddress);
                resultset = preparedStatement.executeQuery();
            
                resultset.next();
                int count = resultset.getInt(1);
                if(count >= 1){
                    unique = false;
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            finally{
                if(resultset != null){
                try {
                    resultset.close();
                } catch(SQLException ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getMessage());
                    }
                }
            
                if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getMessage());
                    }
                }
            
            if(connection != null){
                try {
                    connection.close();
                } catch(SQLException ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getMessage());
                    }
                }
            
            }
            //process routing since db operations are settled
            if(unique){
                //if book is unique, continue to add it in db
            RequestDispatcher rd=
                request.getRequestDispatcher("/register");
            rd.forward(request, response);
             }
            else {
                //else return to register.jsp
                    out.print("Please try again!");
                    response.sendRedirect(this.getServletContext().getContextPath() + "/index.html");
                }
            }
        else{
            //else if email not valid return to register.jsp
            out.print("Please try again!");
            response.sendRedirect(this.getServletContext().getContextPath() + "/index.html");
        }
        out.close();
    }
}