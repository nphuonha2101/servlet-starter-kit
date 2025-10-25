
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--Start localization feature--%>
<%--<c:choose>--%>
<%--    <c:when test="${empty sessionScope.lang || sessionScope.lang eq 'default'}">--%>
<%--        <fmt:setLocale value="default"/>--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--        <fmt:setLocale value="${sessionScope.lang}"/>--%>
<%--    </c:otherwise>--%>
<%--</c:choose>--%>

<%--<fmt:setBundle basename="properties.pageTitle" var="pageTitleMsg"/>--%>
<%--End localization feature--%>

<!DOCTYPE html>
<%--<c:choose>--%>
<%--    <c:when test="${empty sessionScope.lang || sessionScope.lang eq 'default'}">--%>
<%--        <html lang="vi">--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--        <html lang="en">--%>
<%--    </c:otherwise>--%>
<%--</c:choose>--%>
<html lang="en">

<head>
    <title>${title}</title>
    <%-- Tailwind CSS (Built from npm) --%>
    <link rel="stylesheet" href="/static/css/main.css">
    
    <%-- Style sheets --%>
    <c:forEach var="styleSheet" items="${styleSheets}">
        <link rel="stylesheet" href="${styleSheet}">
    </c:forEach>

    <%-- Other links or metadata --%>
</head>
<body>
<header>
    <jsp:include page="/views/layouts/partials/header.jsp"/>
</header>
<main>
    <jsp:include page="${contents}"/>

</main>

<footer>
    <jsp:include page="/views/layouts/partials/footer.jsp"/>
</footer>


<%-- Scripts --%>
<script type="module" src="/static/js/main.js"></script>
<c:forEach var="script" items="${scripts}">
    <script src="${script}"></script>
</c:forEach>
</body>
</html>