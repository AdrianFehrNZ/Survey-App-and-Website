<%-- 
    Document   : add-surveyor
    Created on : 30/05/2020, 9:02:27 AM
    Author     : AdrianF
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Participant</title>
    </head>
    <body>
        <DIV style="background-color: lightblue" align="center" >
            <h1>Add Participant</h1>
        </DIV>

        <form action="<%= response.encodeURL(request.getContextPath())%>/surveyservice/survey" method="POST">
            <p>
                Participant:
                <input type="text" name="participant"/>
            </p>
            <p>
                Surveyor:
                <input type="text" name="surveyor"/>
            </p>
            <input type="submit" value="Add Participant"/>
        </form>

    </body>
</html>
