<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<script src="webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<link rel='stylesheet' href='webjars/bootstrap/4.3.1/css/bootstrap.min.css'>
<link rel="stylesheet" href="css/style.css">

<title>Students</title>
</head>
<body>
    <h1>STUDENTS</h1><br>
    
    <div class="container create border">
    <form action="students" class="needs-validation" method="post" novalidate>
      <input type="hidden" name="id" value="${currentStudent.id}" />

      <div class="form-group">
        <label for="firstName">First Name</label>
        <input type="text" class="form-control" id="firstName" name="firstName" required>
        <div class="valid-feedback">Valid.</div>
        <div class="invalid-feedback">Please fill out this field.</div>
      </div>

      <div class="form-group">
        <label for="lastName">Last Name</label>
        <input type="text" class="form-control" id="lastName" name="lastName" required>
        <div class="valid-feedback">Valid.</div>
        <div class="invalid-feedback">Please fill out this field.</div>
      </div>
      
      <div class="form-group">
       <label for="group">Group</label>
       <select id="group" id="group" name="group">
       
          <c:forEach var="group" items="${groups}">
            <option value="${group.id}">${group.name}</option>
          </c:forEach>
        
       </select> 
      </div>

      <button type="submit" class="btn btn-primary">Create</button>
    </form>
    </div>
    
    <br><br>
    
    <table border="1" align="center">
        <tr>
          <th>ID</th>
          <th>Full Name</th>
        </tr>
        <c:forEach var="student" items="${students}">
        
            <tr>
              <td>${student.id}</td>
              <td><a href="student?id=${student.id}">${student.firstName} ${student.lastName}</a></td>
            </tr>
            
        </c:forEach>
    </table>
    
<script src="js/form_validator.js"></script></body>
</html>
