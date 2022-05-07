<%-- 
    Document   : produtosIntegrados
    Created on : 12 de fev de 2022, 20:29:07
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Produtos Integrados</title>
    </head>
    <body>
        <div class="container d-flex flex-wrap">
            <div class="row">
                <c:forEach var="produto" items="${requestScope.productList}">
                    <div class="col-md-12 d-flex">
                        
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
