package jeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 184645L
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    //Inject your EJB here
    @EJB
    private SearchBean searchbean;
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException{
        String searchTerm = request.getParameter("searchterm");
        List<Products> searchResult = new ArrayList<>();
        searchResult = searchbean.searchProduct(searchTerm);
        
        //Set the search term and the search results in the session
        HttpSession session = request.getSession();
        session.setAttribute("searchterm", searchTerm);
        session.setAttribute("searchresult", (Object)searchResult);
        
        //Client side redirect
        response.sendRedirect(this.getServletContext().getContextPath() + "/searchresults.jsp"); 
    }
}