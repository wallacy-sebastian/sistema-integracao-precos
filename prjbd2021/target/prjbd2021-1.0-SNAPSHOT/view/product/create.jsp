<%-- 
    Document   : create
    Created on : 6 de fev de 2022, 10:26:49
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.ArrayList" %>
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
                    <select name="loja" class="form-control" onchange="val()" required>
                        <option value="" disabled selected>Escolha...</option>
                        <c:forEach var="loja" items="${requestScope.product.getLojaAll()}">
                            <option value="${loja}">${requestScope.product.getLojaFromValue(loja)}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <select name="loja-data" class="form-control">
                    </select>
                </div>
                <div class="text-center">
                    <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
                </div>
                    <script>
                        function val() {
                            var lojaValor = document.querySelector("select[name=loja]").value;
                            switch(lojaValor) {
                                case '1':
                                    loja = "americanas";
                                    break;
                                case '2':
                                    loja = "kabum";
                                    break;
                                case '3':
                                    loja = "londritech";
                                    break;
                                case '4':
                                    loja = "colombo";
                                    break;
                                default:
                                    alert("Loja desconhecida");
                                    break;
                            }
                            <%
                                String path = getServletContext().getRealPath("/") + "/assets/json";
                                File a[] = new File(path).listFiles();
                                ArrayList<String> aFiles = new ArrayList<String>();
                                for(int i = 0; i < a.length; i++) {
                                    if(a[i].isFile()) {
                                        aFiles.add(a[i].getName());
                                    }
                                }
                            %>
                                    
                            const aList = [];
                            const datasLoja = [];
                            
                            <% for(int i = 0; i < aFiles.size(); i++) { %>
                                aList.push("<%= aFiles.get(i) %>");
                            <% } %>

                            console.log(aList);
                            for(let i = 1; i < aList.length; i++) {
                                let nomeLoja = aList[i].substring(0, loja.length);
                                if(nomeLoja === loja) {
                                    if(aList[i].length === loja.length+21) {
                                        let categoria = aList[i].substring(loja.length, loja.length+8);
                                        if(categoria === "-mouses-") {
                                            let data = aList[i].substring(loja.length+8, loja.length+18);
                                            datasLoja.push(categoria.substring(1,8) + data);
                                        }
                                    }
                                    else if(aList[i].length === loja.length+24) {
                                        let categoria = aList[i].substring(loja.length, loja.length+11);
                                        if(categoria === "-monitores-") {
                                            let data = aList[i].substring(loja.length+11, loja.length+21);
                                            datasLoja.push(categoria.substring(1,11) + data);
                                        }
                                    }
                                    else continue;
                                }
                            }
                            console.log(datasLoja);
                            
                            var select = document.querySelector("select[name=loja-data]");

                            var options = select.querySelectorAll(":scope > option");
                            options.forEach(option => document.querySelector("select[name=loja-data]").removeChild(option));

                            for (let i = 0; i < datasLoja.length; i++){
                                var opt = document.createElement('option');
                                opt.value = datasLoja[i];
                                opt.innerHTML = datasLoja[i];
                                select.appendChild(opt);
                            }
                        }
                    </script>
            </form>
        </div>
    </body>
    <%@include file="/view/include/scripts.jsp"%>
</html>
