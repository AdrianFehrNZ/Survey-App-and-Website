<%-- 
    Document   : homepage
    Created on : 22/05/2020, 2:11:24 PM
    Author     : AdrianF
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Survey Admin Homepage</title>
        <style>
            body {background: url(survey.jpg) no-repeat; background-size: 100% 250%;}
        </style>
    </head>
    <body>
        <DIV style="background-color: lightblue" align="center" >
            <h1>Survey Admin Homepage</h1>
        </DIV>

        <br /><br /><br />

        <div align="center" style="font-size: 30px">
            <p style="font-weight: bold;"/>
            <a href="<%= response.encodeURL(request.getContextPath())%>/surveyservice/survey" style="color:black;">See Survey Results</a>
            <p style="font-weight: bold;"/>
            <a href="<%= response.encodeURL(request.getContextPath())%>/surveyservice/survey/surveyors" style="color:black;">Surveyors</a>
            <p style="font-weight: bold;"/>
            <a href="/DMSAssignment3/add-surveyor.jsp" style="color:black;">Add Participant</a>
            <p style="font-weight: bold;"/>
            <a href="<%= response.encodeURL(request.getContextPath())%>/surveyservice/survey/participants" style="color:black;">See Participants</a>
            <p style="font-weight: bold;"/>
            <a href="<%= response.encodeURL(request.getContextPath())%>/surveyservice/survey/locations" style="color:black;">See Locations</a>
            <div/>
    </body>
</html>
