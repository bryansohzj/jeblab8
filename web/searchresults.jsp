<%@page import="jeb.Products"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shopping System - Search Result</title>
    </head>
    <body>
        <p>
            <b>Search Results</b>
        </p>
        <hr/>
        <p>
            <!--A reminder note to the user on the search term he used--> 
            Search results for "<b><%=session.getAttribute("searchterm")%></b>"
        </p>
        <hr/>
        <p>
            <!--Results-->
            <table>
                <tr>
                    <th>Item</th>
                    <th>Description</th>
                    <th>Price Per Unit</th>
                </tr>
                <%
                    List<Products> searchresult = (List<Products>)session.getAttribute("searchresult");
                    if(searchresult == null || searchresult.size() <= 0){
                %>
                <tr>
                    <td colspan="6">(No result is found)</td>
                </tr>
                <%
                    }else{
                        for(Products product : searchresult){
                %>
                <tr>
                    <td><%=product.getItem()%></td>
                    <td><%=product.getDescription()%></td>
                    <td><%=product.getPricePerUnit()%></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </p>
        <hr/>
        <a href="search.jsp">Do another search</a><br/>
        <a href="index.html">Go back to menu</a>
    </body>
</html>