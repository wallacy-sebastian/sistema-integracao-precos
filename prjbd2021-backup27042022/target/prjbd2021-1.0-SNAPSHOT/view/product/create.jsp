<%-- 
    Document   : create
    Created on : 6 de fev de 2022, 10:26:49
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/view/include/head.jsp"  %>
        <title>Create</title>
    </head>
    <body>
        <div class="container">
            <h2 class="text-center">Escanear json para criar produto</h2>
                 
            <form
                class="form"
                action="${pageContext.servletContext.contextPath}/product/create"
                method="POST">
                <div class="form-group">
                    <select name="loja" class="form-control">
                        <c:forEach var="loja" items="${requestScope.product.getLojaAll()}">
                            <option value="${loja}">${requestScope.product.getLojaFromValue(loja)}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="text-center">
                    <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
                </div
            </form>
        </div>
    </body>
    <%@include file="/view/include/scripts.jsp"%>
</html>
