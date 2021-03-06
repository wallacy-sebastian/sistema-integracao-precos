<%-- 
    Document   : update
    Created on : 19 de jan de 2022, 14:29:45
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/view/include/head.jsp"  %>
        <title>[User App] Usuários: atualização</title>
    </head>
    <body>
        <div class="container">
            <h2 class="text-center">Edição do usuário <c:out value="${user.nome}"/></h2>

            <form
                class="form"
                action="${pageContext.servletContext.contextPath}/user/update"
                enctype="multipart/form-data"
                method="POST">
                
                <input type="hidden" name="id" value="${user.id}">

                <div class="form-group">
                    <label class="control-label" for="usuario-login">Login</label>
                    <input id="usuario-login" class="form-control" type="text" name="login" value="${user.login}" data-value="${user.login}" required autofocus/>

                    <p class="help-block"></p>
                </div>


                <div class="form-group">
                    <label class="control-label">Senha</label>
                    <input class="form-control password-input"
                           type="password" name="senha"
                           pattern=".{4,}" title="Pelo menos 4 caracteres."/>
                </div>

                <div class="form-group pwd-confirm">
                    <label class="control-label">Confirmar senha</label>
                    <input class="form-control password-confirm"
                           type="password" name="senha-confirmacao"
                           pattern=".{4,}" title="Pelo menos 4 caracteres."/>
                    <p class="help-block"></p>
                </div>

                <div class="form-group">
                    <label for="usuario-nome" class="control-label">Nome</label>
                    <input id="usuario-nome" class="form-control" type="text" name="nome" value="${user.nome}" required/>
                </div>


                <div class="form-group">
                    <label for="usuario-nasc" class="control-label">Data de nascimento</label>
                    <input id="usuario-nasc" class="form-control datepicker" type="date" name="nascimento"
                           placeholder="dd/mm/yyyy" value="${user.nascimento}"
                           pattern="\d{2}/\d{2}/\d{4}" required/>
                </div>
                           
                <div class="form-group">
                    <label for="cep" class="control-label">CEP</label>
                    <input id="usuario-cep" class="form-control" type="text" name="cep" value = "${user.cep}" required/>
                </div>

                <div class="form-group">
                    <label for="usuario-avatar">Foto do perfil</label>
                    <input type="file"
                           class="form-control" id="usuario-avatar"
                           name="avatar"
                           accept="image/*"/>
                </div>

                <div class="text-center">
                    <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
                </div>
            </form>
        </div>

        <%@include file="/view/include/scripts.jsp"%>
        <script src="${pageContext.servletContext.contextPath}/assets/js/user.js"></script>
    </body>
</html>