<%-- 
    Document   : index
    Created on : 20 de jan de 2022, 17:02:19
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/view/include/head.jsp"  %>
        <title>Produtos</title>
    </head>
    <body>
        <div class="container">
            <div class="text-center div_inserir_excluir">
                <div class = "col-md-12">
                    <div class="form-group col-md-4">
                        <label class="control-label">String 1</label>
                        <input id="string1" class="form-control" type="text" name="string1"/>
                    </div>

                    <div class="form-group col-md-4">
                        <label class="control-label">String 2</label>
                        <input id="string2" class="form-control" type="text" name="string2"/>
                    </div>

                    <button class="btn btn-lg btn-warning" id = "btn-ver-str" data-href="${pageContext.servletContext.contextPath}/product/checkString">
                        verifica similar
                    </button>
                    <h3 id = "resultado-string">121</h3>
                </div>
                <a href="${pageContext.servletContext.contextPath}" class="btn btn-lg btn-success">Voltar</a>
                <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/product/create">
                    Inserir novo produto
                </a>

                <button class="btn btn-lg btn-warning" data-toggle="modal" data-target=".modal_excluir_usuarios">
                    Excluir múltiplos produtos
                </button>
                    
                <button class="btn btn-lg btn-warning selecionar_todos" data-toggle="modal" selec="false">
                    Selecionar tudos produtos
                </button>
                <a class="btn btn-default"
                   href="${pageContext.servletContext.contextPath}/logout"
                   data-toggle="tooltip"
                   data-original-title="Logout">
                    <i class="fa fa-sign-out"></i>
                </a>
            </div>
            
            <form class="form_excluir_usuarios" action="${pageContext.servletContext.contextPath}/product/delete" method="POST">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="col-lg-2 h4">ID</th>
                            <th class="col-lg-5 h4">Nome</th>
                            <th class="col-lg-5 h4">Valor</th>
                            <th class="col-lg-4 h4 text-center">Ação</th>
                            <!--<th class="col-lg-1 h4 text-center">Excluir?</th>-->
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="produto" items="${requestScope.productList}">
                            <tr>
                                <td>
                                    <span class="h4"><c:out value="${produto.id}"/></span>
                                </td>
                                <td>
                                    <a class="link_visualizar_produto" href="#" data-href="${pageContext.servletContext.contextPath}/product/read?id=${produto.id}">
                                        <span class="h4"><c:out value="${produto.nome}"/></span>
                                    </a>
                                </td>
                                <td>
                                    <span class="h4">R$ <c:out value="${produto.valor}"/></span>
                                </td>
                                <td class="text-center">
                                    <a class="btn btn-default"
                                       href="${pageContext.servletContext.contextPath}/product/update?id=${produto.id}"
                                       data-toggle="tooltip"
                                       data-original-title="Editar">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a class="btn btn-default link_excluir_usuario"
                                       href="#"
                                       data-href="${pageContext.servletContext.contextPath}/product/delete?id=${produto.id}"
                                       data-toggle="tooltip"
                                       data-original-title="Excluir">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </td>
                                <td class="text-center">
                                    <input class="checkbox-inline" type="checkbox" name="delete" value="${produto.id}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>

            <div class="modal fade modal_excluir_usuario">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Confirmação</h4>
                            <button class="close" type="button" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <p>Tem certeza de que deseja excluir este usuário?</p>
                        </div>
                        <div class="modal-footer">
                            <a class="btn btn-danger link_confirmacao_excluir_usuario">Sim</a>
                            <button class="btn btn-primary" type="button" data-dismiss="modal">Não</button>
                        </div>
                    </div>
                </div>
            </div>                

            <div class="modal modal-visualizar-produto">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Detalhes</h4>
                            <button class="close" type="button" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-md-12 d-flex flex-wrap">
                                        <div class = "col-md-8">
                                            <h3>Produto</h3>
                                            <p class="p_id"></p>
                                            <p class="p_nome"></p>
                                            <p class="p_secao"></p>
                                            <p class="p_descricao"></p>
                                            <p class="p_modelo"></p>
                                            <p class="p_marca"></p>
                                            <p class="p_fichaTecnica"></p>
                                            <p class="p_valor"></p>
                                            <p class="p_createdAt datepicker"></p>
                                            <p class="p_loja"></p>
                                        </div>
                                        <div class="col-md-4">
                                            <a href="#" class="thumbnail">
                                                <img class="produto-img"
                                                     src="${pageContext.request.contextPath}/img/default_avatar.png"
                                                     height="160" width="120"/>
                                            </a>
                                        </div>
                                        <div class="col-md-12 produto-avaliacao">
                                            
                                        </div>
                                        <div class="col-md-12 produto-entrega">
                                            
                                        </div>
                                        <div class="col-md-12 produto-pagamento">
                                            
                                        </div>
                                        <div class="col-md-12 produto-integrados d-flex flex-wrap">
                                            
                                        </div>
                                        <input type="hidden" id="a-prod-i" value="${pageContext.servletContext.contextPath}">
                                        <h3 class="d-none"id="error-msg">Error ao carregar produto</h3>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-primary" type="button" data-dismiss="modal">Fechar</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade modal_excluir_usuarios">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Confirmação</h4>
                            <button class="close" type="button" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <p>Tem certeza de que deseja excluir os usuários selecionados?</p>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-danger button_confirmacao_excluir_usuarios" type="button">Sim</button>
                            <button class="btn btn-primary" type="button" data-dismiss="modal">Não</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <%@include file="/view/include/scripts.jsp"%>
        <script src="${pageContext.servletContext.contextPath}/assets/js/user.js"></script>
    </body>
</html>
