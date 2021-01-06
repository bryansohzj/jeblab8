<%-- 
    Document   : search
    Created on : 6 Jan, 2021, 10:08:14 AM
    Author     : 184645L
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Shopping System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <b>Shopping System - Search</b>
        <form action="search" method="post">
            <p>
                Please enter a title or part of the title of the book: <br/>
                <input type="text" name="searchterm" required/><br/>
                <input type="submit" value="Search"/>
            </p>
        </form>
        <a href="index.html">Back</a>
    </body>
</html>