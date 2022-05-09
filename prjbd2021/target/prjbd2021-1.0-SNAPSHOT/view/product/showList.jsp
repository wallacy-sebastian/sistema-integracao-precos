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
            <a href="${pageContext.servletContext.contextPath}" class="btn btn-lg btn-success">Voltar</a>
            <a class="col-12 text-center" href="${pageContext.servletContext.contextPath}/product/show">
                <h1>Lista de produtos</h1>
            </a>
            <form class="form_p_show col-12 d-flex flex-wrap form-group" action="${pageContext.servletContext.contextPath}/product/show?searchInput=" method="POST">
                <p>O que voce esta procurando? </p>
                <input class="form-control" id="search-input" type="text">
                <label class="form-control col-3 mt-2" style="border: 0">Categoria: </label>
                <select class="form-control col-9 mt-2" id="categoria">
                    <option value="0">Selecione uma opcao</option>
                    <option value="${MONITOR}">Monitor</option>
                    <option value="${MOUSE}">Mouse</option>
                </select>
                <label class="form-control col-3" style="border: 0">Ordenar por: </label>
                <select class="form-control col-9" id="sort">
                    <option value="0">Selecione uma opcao</option>
                    <option value="1">Nome</option>
                    <option value="2">Preco</option>
                </select>
                <label class="form-control col-3" style="border: 0">Qual loja vc busca: </label>
                <select class="form-control col-9" id="loja">
                    <option value="0">Selecione uma opcao</option>
                    <option value="${KABUM}">Kabum</option>
                    <option value="${AMERICANAS}">Americanas</option>
                    <option value="${LONDRITECH}">Londritech</option>
                </select>
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
