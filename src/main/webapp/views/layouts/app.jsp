<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <%-- Tailwind CSS (Built from npm) --%>
    <link rel="stylesheet" href="/static/css/main.css">
    
    <%-- Font Awesome Icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <%-- jQuery and jQuery Validation --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.5/dist/jquery.validate.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.5/dist/additional-methods.min.js"></script>
    
    <%-- SweetAlert2 --%>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <%-- Style sheets --%>
    <c:forEach var="styleSheet" items="${styleSheets}">
        <link rel="stylesheet" href="${styleSheet}">
    </c:forEach>

    <%-- Other links or metadata --%>
</head>
<body>
<%--Header--%>
    <jsp:include page="/views/layouts/partials/header.jsp"/>
<%--End header--%>

<main>
    <jsp:include page="${contents}"/>
</main>

<%--Footer--%>
<jsp:include page="/views/layouts/partials/footer.jsp"/>
<%--End footer--%>


<%-- Scripts --%>
<script src="${pageContext.request.contextPath}/static/js/alert.js" charset="UTF-8"></script>
<script src="${pageContext.request.contextPath}/static/js/validation-rules.js" charset="UTF-8"></script>
<c:forEach var="script" items="${scripts}">
    <script src="${script}" charset="UTF-8"></script>
</c:forEach>
</body>
</html>