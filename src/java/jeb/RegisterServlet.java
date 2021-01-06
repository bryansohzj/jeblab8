package jeb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Resource(name="jdbc/jed")
    private DataSource dsLab8;
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException{
        
        int customerId = 0;
        
//        String customerIdString = request.getParameter("customerId");
//        int customerId = Integer.parseInt(customerIdString);
        String name = request.getParameter("name");
        String emailaddress = request.getParameter("emailaddress");
        String deliveryAddress = request.getParameter("deliveryAddress");
        String postalcode = request.getParameter("postalcode");
        String contactnumber = request.getParameter("contactnumber");
        
        String sqlInsert1 = "INSERT INTO customer (name, emailaddress, deliveryAddress, postalcode, contactnumber";
        String sqlInsert2 = "VALUES(?,?,?,?,?";
        
        sqlInsert1 += ") ";
        sqlInsert2 += ") ";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
//        PreparedStatement preparedStatement2 = null;
        ResultSet resultset = null;
        
        

        try
        {
            connection = dsLab8.getConnection();
            connection.setAutoCommit(false);
            
            preparedStatement = connection.prepareStatement(sqlInsert1 + sqlInsert2);
//            preparedStatement.setInt(1, customerId);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, emailaddress);
            preparedStatement.setString(3, deliveryAddress);
            preparedStatement.setString(4, postalcode);
            preparedStatement.setString(5, contactnumber);
            
//            preparedStatement.setString(8, confirmPassword);
            //insert data into db
            preparedStatement.executeUpdate();
            
             String sqlCheck = "SELECT * FROM customer WHERE emailaddress = ?";
            preparedStatement = connection.prepareStatement(sqlCheck);
            preparedStatement.setString(1, emailaddress);
            
            resultset = preparedStatement.executeQuery();
            
            if(resultset.next()){
                customerId = resultset.getInt("customerId");
            }

            //code reach this line, insertion must have been successful
            //commit the transaction
            connection.commit();
            
            request.getSession().setAttribute("message", name);
            
            CustomerRecord customerRecord = new CustomerRecord();
            
            customerRecord.setName(request.getParameter("name"));
            customerRecord.setEmailAddress(request.getParameter("emailaddress"));
            customerRecord.setDeliveryAddress(request.getParameter("deliveryAddress"));
            customerRecord.setPostalCode(request.getParameter("postalCode"));
            customerRecord.setContactnumber(request.getParameter("contactnumber"));
            customerRecord.setCustomerId(customerId);
            
            HttpSession session = request.getSession();
            
            List<CustomerRecord> customerlist = (List<CustomerRecord>)
                    session.getAttribute("customerlist");
            if(customerlist == null){
                customerlist = new ArrayList<>();
            } else {
                customerlist.clear();
            }
            
            customerlist.add(customerRecord);
            session.setAttribute("customerlist", customerlist);
            
        }
        catch(SQLException ex)
        {
            request
                    .getSession()
                    .setAttribute("message",
                     "An error has occured in the insertion process, Please check with your DB administrator for more details.");
            try
            {
                //rollback if error
                connection.rollback();
            }
            catch(SQLException ex1)
            {
                    ex1.printStackTrace();
                    System.err.println(ex1.getMessage());
                    
            }
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
        
        finally
        {
            //resultset, statement, connection closed in finally
            //ensure that they will be closed no matter what happens in system
            if(resultset != null) {
                try {
                    resultset.close();
                } catch(SQLException ex) {
                    try {
                        //Roll back if there is an error
                        connection.rollback();
                    } catch (SQLException ex1) {
                        ex1.printStackTrace();
                        System.err.println(ex1.getMessage());
                    }
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException ex) {
                    try{
                        connection.rollback();
                    }catch(SQLException ex1){
                        ex1.printStackTrace();
                        System.err.println(ex1.getMessage());
                    }
                }
            }
            if(connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch(SQLException ex) {
                    try{
                        connection.rollback();
                    }catch(SQLException ex1){
                        ex1.printStackTrace();
                        System.err.println(ex1.getMessage());
                    }
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
            response.sendRedirect(this.getServletContext().getContextPath() +
                    "/search.jsp");
        }
    }
}

