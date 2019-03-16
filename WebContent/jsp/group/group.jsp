<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<link rel="stylesheet" href="css/bootstrap.min.css">
<script src="js/bootstrap.min.js" ></script>
<link rel="stylesheet" href="css/style.css">

<c:set var="currentGroup" value="${group}"></c:set>

<title>Group ${currentGroup.name}</title>
</head>
<body>
    <h3>Group id: ${currentGroup.id}</h3>
    <h3>Group Name: ${currentGroup.name}</h3>
    <br><br>
    
    <div class="container update border">
    <form action="group-update-servlet" class="needs-validation" method="post" novalidate>
      <input type="hidden" name="id" value="${currentStudent.id}" />

      <div class="form-group">
        <label for="name">New name</label>
        <input type="text" class="form-control" id="name" name="name" required>
        <div class="valid-feedback">Valid.</div>
        <div class="invalid-feedback">Please fill out this field.</div>
      </div>

      <button type="submit" class="btn btn-primary">Update</button>
    </form>
    </div>
    <br><br>
    
    <form action="group-delete-servlet?id=${currentGroup.id}" method="post">
      <input type="submit" value="Delete" />
    </form>
    <br><br>
    
    <h3>Students</h3>
    
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
<script src="js/form_validator.js"></script>
</body>
</html>
