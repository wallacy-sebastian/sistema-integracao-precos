<%-- 
    Document   : showProduct
    Created on : 7 de mai de 2022, 23:17:54
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/view/include/head.jsp"  %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Show produto</title>
    </head>
    <body>
        <div class="container">
            <div class="col-12">
            <c:choose>
               <c:when test="${isEmpty}">
                   <h1>produto nao encontrado.</h1>
               </c:when>
               <c:otherwise>
                   <div class = "container">
                       <a href="${pageContext.servletContext.contextPath}/product/show" class="btn btn-lg btn-success">Voltar</a>
                       <img src="${pMaster.getUrlImg()}" width="300px" alt="${pMaster.getNome()}" style="display: block; margin: 0 auto;"/>
                        <table>
                            <tbody>
                                <tr>
                                    <th>Id: &nbsp;</th>
                                    <td>${pMaster.getId()}</td>
                                </tr>
                                <tr>
                                    <th>Nome:&nbsp;</th>
                                    <td>${pMaster.getNome()}</td>
                                </tr>
                                <tr>
                                    <th>Loja:&nbsp;</th>
                                    <td>${pMaster.getLojaFromValue(pMaster.getLoja())}</td>
                                </tr>
                                <tr>
                                    <th>Marca:&nbsp;</th>
                                    <td>${pMaster.getMarca()}</td>
                                </tr>
                                <tr>
                                    <th>Modelo:&nbsp;</th>
                                    <td>${pMaster.getModelo()}</td>
                                </tr>
                                <tr>
                                    <th>Ficha tecnica:&nbsp;</th>
                                    <td>${pMaster.getFichaTecnica()}</td>
                                </tr>
                                <tr>
                                    <th>Secao:&nbsp;</th>
                                    <td>${pMaster.getSecaoFromValue(pMaster.getSecao())}</td>
                                </tr>
                                <tr>
                                    <th>Descricao:&nbsp;</th>
                                    <td>${pMaster.getDescricao()}</td>
                                </tr>
                                <tr>
                                    <th>Data de criacao:&nbsp;</th>
                                    <td>${pMaster.getDataFromValue()}</td>
                                </tr>
                                <tr>
                                    <th>Valor:&nbsp;</th>
                                    <td>${pMaster.getPreco()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <%
                            Gson gsonObj = new Gson();
                            Map<Object,Object> map = null;
                            List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

                            int count = 50;
                            int y = 100;
                            Random rand = new Random();

                            for(int i = 1; i < count; i++){
                                    y += rand.nextInt(11) - 5;
                                    map = new HashMap<Object,Object>();
                                    map.put("x", i);
                                    map.put("y", y);
                                    list.add(map);
                            }

                            String dataPoints = gsonObj.toJson(list);
                        %>
                        <script type="text/javascript">
                            window.onload = function () {

                            var data = [];
                            var dataSeries = { type: "line" };
                            var dataPoints = <%out.print(dataPoints);%>
                            dataSeries.dataPoints = dataPoints;
                            data.push(dataSeries);

                            //Better to construct options first and then pass it as a parameter
                            var options = {
                                    zoomEnabled: true,
                                    animationEnabled: true,
                                    title: {
                                            text: "Histórico de preços"
                                    },
                                    axisX: {
                                        title: "Data"
                                    },
                                    axisY: {
                                        title: "Preço"
                                    },
                                    data: data  // random data
                            };

                            var chart = new CanvasJS.Chart("chartContainer", options);
                            var startTime = new Date();
                            chart.render();
                            var endTime = new Date();
                            document.getElementById("timeToRender").innerHTML = "Time to Render: " + (endTime - startTime) + "ms";

                            };
                        </script>
                        <div id="chartContainer" style="height: 370px; width: 100%;"></div>
                        
                        <script src="${pageContext.servletContext.contextPath}/assets/js/canvasjs.min.js"></script>
                   </div>
               </c:otherwise>
            </c:choose>
            </div>
        </div>
    </body>
</html>
