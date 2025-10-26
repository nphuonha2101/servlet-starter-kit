<jsp:include page="/views/layouts/partials/view-meta.jsp"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>${title}</title>
    
    <%-- Include meta tags and head content --%>
    <jsp:include page="/views/layouts/partials/view-meta.jsp"/>
    <%-- Tailwind CSS (Built from npm) --%>
    <link rel="stylesheet" href="/static/css/main.css">
    
    <%-- Font Awesome Icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <%-- jQuery and jQuery Validation --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.5/dist/jquery.validate.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.5/dist/additional-methods.min.js"></script>
    
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
<script src="/static/js/alert.js"></script>
<script src="/static/js/validation-rules.js"></script>
<c:forEach var="script" items="${scripts}">
    <script src="${script}"></script>
</c:forEach>
</body>
</html>