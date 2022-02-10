<%-- 
    Document   : update
    Created on : 9 de fev de 2022, 17:40:02
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/view/include/head.jsp"  %>
        <title>Produto: atualização</title>
    </head>
    <body>
        <div class="container">
            <h2 class="text-center">Edição do produto <c:out value="${product.nome}"/></h2>

            <form
                class="form"
                action="${pageContext.servletContext.contextPath}/product/update"
                method="POST">
                
                <input type="hidden" name="produtoId" value="${product.id}">
                
                <h2 class="text-center">Informações do Produto</h2>

                <div class="form-group">
                    <label class="control-label" for="usuario-login">Nome</label>
                    <input id="produtoNome" class="form-control" type="text" name="produtoNome" value="${product.nome}"
                           data-value="${product.nome}" required autofocus/>
                    <p class="help-block"></p>
                </div>


                <div class="form-group">
                    <label class="control-label">URL da imagem</label>
                    <input class="form-control" type="text" name="produtoUrl"
                           value="${product.urlImg}" data-value="${product.urlImg}" />
                </div>

                <div class="form-group">
                    <label for="usuario-nome" class="control-label">Descrição</label>
                    <input class="form-control" type="text" name="produtoDescricao" value="${product.descricao}"/>
                </div>

                <div class="form-group">
                    <label for="usuario-nasc" class="control-label">Modelo</label>
                    <input class="form-control datepicker" type="text" name="produtoModelo"
                           value="${product.modelo}"/>
                </div>
                
                <div class="form-group">
                    <label class="control-label">Marca</label>
                    <input class="form-control" type="text" name="produtoMarca" value = "${product.marca}" />
                </div>
                           
                <div class="form-group">
                    <label class="control-label">Ficha técnica</label>
                    <input class="form-control" type="text" name="produtoFicha" value = "${product.fichaTecnica}" />
                </div>
                
                <div class="form-group">
                    <label class="control-label">Valor</label>
                    <input class="form-control" type="number" name="produtoValor" value = "${product.valor}" />
                </div>
                
                <c:if test="${!requestScope.aList.isEmpty()}">
                    <h2 class="text-center">Informações de Avaliação</h2>
                    <div class = "container d-flex flex-wrap">
                        <c:forEach var="avaliacao" items="${requestScope.aList}">
                            <div class = "col-md-12 d-flex" style = "border: 1px solid #000">
                                <div class="form-group col-md-4">
                                    <label class="control-label">Nome</label>
                                    <input class="form-control" type="text" name="avaliacaoNome" value = "${avaliacao.nome}" />
                                </div>

                                <div class="form-group col-md-4">
                                    <label class="control-label">Comentário</label>
                                    <input class="form-control" type="text" name="avaliacaoComentario" value = "${avaliacao.comentario}" />
                                </div>

                                <div class="form-group col-md-4">
                                    <label class="control-label">Estrelas</label>
                                    <input class="form-control" type="number" name="avaliacaoEstrela" value = "${avaliacao.estrelas}" />
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                    
                <c:if test="${!requestScope.eList.isEmpty()}">
                    <h2 class="text-center">Informações de Entrega</h2>

                    <div class = "container d-flex flex-wrap">
                        <c:forEach var="entrega" items="${requestScope.eList}">
                            <div class = "col-md-12 d-flex" style = "border: 1px solid #000">
                                <div class="form-group col-md-4">
                                    <label class="control-label">Nome</label>
                                    <input class="form-control" type="text" name="entregaNome" value = "${entrega.nome}" />
                                </div>

                                <div class="form-group col-md-4">
                                    <label class="control-label">Valor</label>
                                    <input class="form-control" type="number" name="entregaValor" value = "${entrega.valor}" />
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                    
                <c:if test="${!requestScope.pList.isEmpty()}">
                    <h2 class="text-center">Informações de Pagamento</h2>
                    <div class = "container d-flex flex-wrap">
                        <c:forEach var="pagamento" items="${requestScope.pList}">
                            <div class = "col-md-12 d-flex" style = "border: 1px solid #000">
                                <div class="form-group col-md-4">
                                    <label class="control-label">Vezes</label>
                                    <input class="form-control" type="number" name="pagamentoVezes" value = "${pagamento.vezes}" />
                                </div>

                                <div class="form-group col-md-4">
                                    <label class="control-label">Valor</label>
                                    <input class="form-control" type="number" name="pagamentoValor" value = "${pagamento.valor}" />
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                <div class="text-center">
                    <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
                </div>
            </form>
        </div>

        <%@include file="/view/include/scripts.jsp"%>
    </body>
</html>
