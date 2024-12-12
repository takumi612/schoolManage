<%@ page import="models.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Educational Portal</title>

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap Icons (optional) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">

  <!-- jQuery (required for Bootstrap Datepicker) -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

  <!-- Bootstrap Bundle JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

  <!-- Bootstrap Datepicker CSS -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css">

  <!-- Bootstrap Datepicker JS -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/index.css">
</head>

<%
  HttpSession sessionHttp = request.getSession();
  User user = (User) sessionHttp.getAttribute("user");
  String url ;
  if(user.getUserRole()){
    url = "studentAdmin";
  }else {
    url ="studentInfo";
  }
  %>

<body>
  <header class="navbar navbar-expand-lg bg-primary text-white py-3">
    <div class="container">
      <!-- Image and text -->
      <nav class="navbar navbar-light">
        <a class="navbar-brand" href="<%=url%>">
          <img src="${pageContext.request.contextPath}/images/icons8-home.svg" width="30" height="30" class="d-inline-block align-top" alt="">
          Home
        </a>
      </nav>
      <div>
        <a href="log-out">
          <button class="btn btn-warning">Sign out</button>
        </a>
      </div>
    </div>
  </header>

<% if(user.getUserRole()){ %>
  <div class="container d-flex justify-content-center align-items-center">
    <nav style="--bs-breadcrumb-divider: '|';" aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="studentAdmin">Student</a></li>
        <li class="breadcrumb-item active" aria-current="page"><a href="classAdmin">Class</a></li>
      </ol>
    </nav>
  </div>
<%
    }
%>
