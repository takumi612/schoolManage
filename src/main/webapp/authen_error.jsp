<%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/14/2024
  Time: 11:42 AM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<!-- NOTE: I can't figure out how to throw a SQLException to test this. -->

<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<td>

  <h1>Authentication error</h1>
  <p>You don't have authen to access this page</p>
  <p>To continue, click the back button from this page.</p>
  <br>

</td>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />