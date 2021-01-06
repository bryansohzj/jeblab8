package jeb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jeb.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author 184645L
 */
@Stateless
public class SearchBean {
    @Resource(name="jdbc/jed")
    private DataSource dsBookCatalogue;
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultset = null;
    public List<Products> searchBook(String searchTerm){
        List<Products> searchResult = new ArrayList<>();
        try {
            String sql = "select * from catalogue where item like ?";
            //Initializing
            //Get the connection from the DataSource
            connection = dsBookCatalogue.getConnection();
            //Create a state,emt using the Connection
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + searchTerm + "%");
            //Make a query to the DB using ResultSet through the Statement
            resultset = statement.executeQuery();

            //retrieval
            while(resultset.next()){
                //Create a book object
                Products product = new Products();
                //Retrieve the data from the recordset and store it into a book object.
//                String isbn = resultset.getString("isbn");
//                product.setIsbn(isbn);
//                product.setAuthor(resultset.getString("author"));
//                product.setTitle(resultset.getString("title"));
//                product.setYear(resultset.getDate("year"));
//                product.setPublisher(resultset.getString("publisher"));
//                product.setAbout(resultset.getString("about"));
                //Store the book object into the list
                searchResult.add(product);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);                 
        } finally {
            //clean up memory
            if(resultset != null){
                try {
                    resultset.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return searchResult;
    }
}
