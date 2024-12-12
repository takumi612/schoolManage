<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modern Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles/login.css">
</head>
<body>

<%
    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
    String errorMessage;
    if(request.getAttribute("error")!= null){
       errorMessage = (String) request.getAttribute("error");
    }else{
        errorMessage ="";
    }

%>

<div class="login-container">
    <div class="background-overlay"></div>
    <div class="container h-100">
        <div class="row h-100 align-items-center justify-content-end">
            <div class="col-12 col-md-5">
                <div class="login-form p-4 bg-white rounded-4 shadow-lg">
                    <h2 class="text-center mb-2 fw-bold">Login</h2>
                    <p class="text-center text-muted mb-4">Welcome onboard with us!</p>
                    <p class="text-center text-muted mb-4"><%=errorMessage%></p>
                    <form id="loginForm" action ="login" method="GET">
                        <div class="mb-3">
                            <div class="form-floating">
                                <input type="text" class="form-control custom-input" id="username" placeholder="Enter your username" aria-label="Username" name="userName" required>
                                <label for="username">Username</label>
                                <div class="invalid-feedback">Please enter a valid username</div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="form-floating">
                                <input type="password" class="form-control custom-input" id="password" placeholder="Enter your password" aria-label="Password" name="userPassword" required>
                                <label for="password">Password</label>
                                <div class="invalid-feedback">Password is required</div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-warning w-100 py-3 text-dark fw-bold" id="loginBtn">
                                <span class="spinner-border spinner-border-sm d-none me-2" role="status" aria-hidden="true"></span>
                                Log In
                            </button>
                        </div>
<%--                        <div class="text-center">--%>
<%--                            <p class="mb-0">Don't have an account? <a href="hello-servlet" class="text-decoration-none">Sign up</a></p>--%>
<%--                        </div>--%>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>