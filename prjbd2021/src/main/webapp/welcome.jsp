<%-- 
    Document   : welcome
    Created on : 19 de jan de 2022, 15:21:00
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/view/include/head.jsp"  %>
        <title>[User App] Início</title>
    </head>
    <body>
        <div class="container">
            
            <div class="jumbotron">
                <h1>Bem-vindo,
                <c:out value="${usuario.nome}"/>!</h1>
                
                <p>
                    <h3>Opções:</h3>
                    <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/user">
                        Cadastro de usuários
                    </a>    
                    <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/product">
                        Cadastro de Produtos
                    </a>
                    <a class="btn btn-default"
                       href="${pageContext.servletContext.contextPath}/logout"
                       data-toggle="tooltip"
                       data-original-title="Logout">
                        <i class="fa fa-sign-out"></i>
                    </a>
                </p>
            </div>
        </div>

        <%@include file="/view/include/scripts.jsp"%>
    </body>
</html>

