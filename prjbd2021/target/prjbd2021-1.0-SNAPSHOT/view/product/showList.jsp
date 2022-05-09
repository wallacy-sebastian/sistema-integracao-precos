<%-- 
    Document   : show
    Created on : 7 de mai de 2022, 12:58:25
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/view/include/head.jsp"  %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de produto</title>
    </head>
    <body>
        <input id="contextPath" type="hidden" value="${pageContext.servletContext.contextPath}/product/show"/>
        <div class = "container d-flex flex-wrap">
            <a class="col-12 text-center" href="${pageContext.servletContext.contextPath}/product/show">
                <h1>Lista de produtos</h1>
            </a>
            <form class="form_p_show col-12" action="${pageContext.servletContext.contextPath}/product/show?searchInput=" method="POST">
                <p>O que voce esta procurando? </p>
                <input class="form-control" id="search-input" type="text">
                <button class="btn btn-primary btn-func" style="float: right; margin: 15px 0">Pesquisar</button>
            </form>
            <div class = "col-12 d-flex flex-wrap">
                <c:forEach var="produto" items="${requestScope.productList}">
                    <div class = "col-3 product-thumb" style="heigth: 350px">
                        <a href="${pageContext.servletContext.contextPath}/product/show/view?id=${produto.id}">
                            <img src="${produto.urlImg}" alt="${produto.nome}" width="100%">
                            <h3>${produto.nome}</h3>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <%@include file="/view/include/scripts.jsp"%>
        <script src="${pageContext.servletContext.contextPath}/assets/js/product.js"></script>
    </body>
</html>
