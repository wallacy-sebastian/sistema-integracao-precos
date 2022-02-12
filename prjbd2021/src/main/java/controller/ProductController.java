/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dao.AvaliacaoDAO;
import dao.DAO;
import dao.DAOFactory;
import dao.EntregaDAO;
import dao.PagamentoDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Product;
import java.io.IOException;
import java.util.Iterator;
import model.ProductJson;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import model.Avaliacao;
import model.Entrega;
import model.Pagamento;
import model.StringParecida;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author joao
 */
@WebServlet(
        name = "ProductController",
        urlPatterns = {
            "/product",
            "/product/create",
            "/product/read",
            "/product/update",
            "/product/delete",
            "/product/checkString"
        }
)
public class ProductController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    public List<ProductJson> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
          return readProdutosJsonArray(reader);
        } finally {
          reader.close();
        }
    }
    
    public List<ProductJson> readProdutosJsonArray(JsonReader reader) throws IOException {
        List<ProductJson> prodList = new ArrayList<ProductJson>();

        reader.beginArray();
        while (reader.hasNext()) {
          prodList.add(readProductJson(reader));
        }
        reader.endArray();
        return prodList;
    }
    
    public ProductJson readProductJson(JsonReader reader) throws IOException {
        ProductJson pj = new ProductJson();

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("product")){
                pj.setProduto(readProduct(reader));
            } else if(name.equals("avaliacao")){
                pj.setAvaliacoas(readAvaliacaoArray(reader));
            }  else if(name.equals("entrega")){
                pj.setEntregas(readEntregasArray(reader));
            }  else if(name.equals("pagamento")){
                pj.setPagamentos(readPagamentosArray(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return pj;
    }
   
    public Product readProduct(JsonReader reader) throws IOException {
   
        Product prod = new Product();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("nome")){
                prod.setNome(reader.nextString());
            } else if (name.equals("secao")){
                prod.setSecao(reader.nextInt());
            } else if (name.equals("urlImg")){
                prod.setUrlImg(reader.nextString());
            } else if (name.equals("descricao")){
                prod.setDescricao(reader.nextString());
            } else if (name.equals("modelo")){
                prod.setModelo(reader.nextString());
            } else if (name.equals("marca")){
                prod.setMarca(reader.nextString());
            } else if (name.equals("fichaTecnica")){
                prod.setFichaTecnica(reader.nextString());
            } else if (name.equals("valor")){
                prod.setValor(reader.nextDouble());
            } else if (name.equals("createdAt")){
                String str = reader.nextString();
//                System.out.println(str.substring(0, 10));
                Date dt = Date.valueOf(str.substring(0, 10));
                prod.setCreatedAt(dt);
            } else if (name.equals("loja")){
                prod.setLoja(reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return prod;
    }
    
    public List<Avaliacao> readAvaliacaoArray (JsonReader reader) throws IOException {
        List<Avaliacao> avaList = new ArrayList<Avaliacao>();

        reader.beginArray();
        while (reader.hasNext()) {
            avaList.add(readAvaliacao(reader));
        }
        reader.endArray();
        return avaList;
    }
    
    public Avaliacao readAvaliacao(JsonReader reader) throws IOException {
   
        Avaliacao ava = new Avaliacao();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("nome")){
                ava.setNome(reader.nextString());
            } else if (name.equals("comentario")){
                ava.setComentario(reader.nextString());
            } else if (name.equals("data")){
                String str = reader.nextString();
//                System.out.println(str.substring(0, 9));
                Date dt = new Date(2021, 12, 1);
                ava.setData(dt);
            } else if (name.equals("estrelas")){
                ava.setEstrelas(reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return ava;
    }
    
    public List<Entrega> readEntregasArray (JsonReader reader) throws IOException {
        List<Entrega> etgList = new ArrayList<Entrega>();

        reader.beginArray();
        while (reader.hasNext()) {
          etgList.add(readEntrega(reader));
        }
        reader.endArray();
        return etgList;
    }
    
    public Entrega readEntrega(JsonReader reader) throws IOException {
   
        Entrega etg = new Entrega();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("nome")){
                etg.setNome(reader.nextString());
            } else if (name.equals("valor")){
                etg.setValor(reader.nextDouble());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return etg;
    }
    
    public List<Pagamento> readPagamentosArray (JsonReader reader) throws IOException {
        List<Pagamento> pagList = new ArrayList<Pagamento>();

        reader.beginArray();
        while (reader.hasNext()) {
            pagList.add(readPagamentos(reader));
        }
        reader.endArray();
        return pagList;
    }
    
    public Pagamento readPagamentos(JsonReader reader) throws IOException {
   
        Pagamento pag = new Pagamento();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("tipo")){
                pag.setTipo(reader.nextInt());
            } else if (name.equals("vezes")){
                pag.setVezes(reader.nextInt());
            } else if (name.equals("valor")){
                pag.setValor(reader.nextDouble());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return pag;
    }
   

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO<Product> dao;
        Product prod;
        Avaliacao ava;
        Entrega etg;
        Pagamento pag;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/product": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getProductDAO();

                    List<Product> productList = dao.all();
                    request.setAttribute("productList", productList);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/product/index.jsp");
                dispatcher.forward(request, response);
                break;
            }
            
            case "/product/create": {
                request.setAttribute("product",new  Product());
                dispatcher = request.getRequestDispatcher("/view/product/create.jsp");
                dispatcher.forward(request, response);
                break;
            }
            
            case "/product/update": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getProductDAO();

                    prod = dao.read(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("product", prod);
                    
                    AvaliacaoDAO aDao = daoFactory.getAvaliacaoDAO();
                    List<Avaliacao> aList = aDao.readByProductId(prod.getId());
                    request.setAttribute("aList", aList);
                    
                    EntregaDAO eDao = daoFactory.getEntregaDAO();
                    List<Entrega> eList = eDao.readByProductId(prod.getId());
                    request.setAttribute("eList", eList);
                    
                    PagamentoDAO pDao = daoFactory.getPagamentoDAO();
                    List<Pagamento> pList = pDao.readByProductId(prod.getId());
                    request.setAttribute("pList", pList);

                    dispatcher = request.getRequestDispatcher("/view/product/update.jsp");
                    dispatcher.forward(request, response);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/product");
                }
                break;
            }     
            case "/product/delete": {
                try(DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getProductDAO();
                    daoFactory.beginTransaction();
                    AvaliacaoDAO adDao = daoFactory.getAvaliacaoDAO();
                    EntregaDAO edDao = daoFactory.getEntregaDAO();
                    PagamentoDAO pdDao = daoFactory.getPagamentoDAO();
                    String prodId = request.getParameter("id");
                    int id = Integer.parseInt(prodId);
                    List<Avaliacao> aList = adDao.readByProductId(id);
                    List<Entrega> eList = edDao.readByProductId(id);
                    List<Pagamento> gList = pdDao.readByProductId(id);
                    try {
                        for(Avaliacao l: aList){
                            adDao.delete(l.getId());
                        }
                        for(Entrega l: eList){
                            edDao.delete(l.getId());
                        }
                        for(Pagamento l: gList){
                            pdDao.delete(l.getId());
                        }
                        dao.delete(id);
                        daoFactory.commitTransaction();
                        daoFactory.endTransaction();
                    } catch (SQLException ex) {
                        daoFactory.rollbackTransaction();
                        Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                }

                response.sendRedirect(request.getContextPath() + "/product");
                break;
            }            
            case "/product/read": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getProductDAO();
                    AvaliacaoDAO aDao = daoFactory.getAvaliacaoDAO();
                    EntregaDAO eDao = daoFactory.getEntregaDAO();
                    PagamentoDAO gDao = daoFactory.getPagamentoDAO();
                    int id = Integer.parseInt(request.getParameter("id"));
                    prod = dao.read(id);
                    List<Avaliacao> aList = aDao.readByProductId(id);
                    List<Entrega> eList = eDao.readByProductId(id);
                    List<Pagamento> gList = gDao.readByProductId(id);
                    ProductJson pj = new ProductJson();
                    pj.setProduto(prod);
                    pj.setAvaliacoas(aList);
                    pj.setEntregas(eList);
                    pj.setPagamentos(gList);

                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                    String json = gson.toJson(pj);

                    response.getOutputStream().print(json);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/product");
                }
                break;
            }
            case "/product/checkString":{
                String str1 = request.getParameter("string1");
                String str2 = request.getParameter("string2");
                String str;
                if(!str1.isEmpty() && !str2.isEmpty()){
                    double r = StringParecida.compareStrings(str1, str2);
                    str = "{\"str\":"+r+"}";
                } else {
                    str = "{\"str\": 0}";
                }
                response.getOutputStream().print(str);
//                System.out.println("{\"str\":"+r+"}");
                
                break;
            }
        }
        
    }


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAO<Product> pDao;
        DAO<Avaliacao> aDao;
        DAO<Entrega> eDao;
        DAO<Pagamento> gDao;
        HttpSession session = request.getSession();

        String servletPath = request.getServletPath();        
        
        switch (request.getServletPath()) {

            case "/product/create":{
                String param = request.getParameter("loja");
                Product p = new Product();
                InputStream in = null;
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                java.lang.reflect.Type type = new TypeToken<List<ProductJson>>() {}.getType();
//                BufferedReader monitor = new BufferedReader( new FileReader("../../assets/json/americanas_monitor.jl"));
                String appPath = request.getServletContext().getRealPath("");
                if(Integer.parseInt(param) == p.AMERICANAS){
                    in = new FileInputStream(appPath+"/assets/json/americanas_mouse.jl");
//                    in = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_monitor.jl");
                }else if(Integer.parseInt(param) == p.KABUM){
                    in = new FileInputStream(appPath+"/assets/json/kabum_mouse.jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/kabum_monitor.jl");
                }else if(Integer.parseInt(param) == p.LONDRITECH){
                    in = new FileInputStream(appPath+"/assets/json/londritech_mouse.jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/londritech_monitor.jl");
                }
//                FileReader fr = new FileReader("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                JsonReader r = new JsonReader(fr);

                
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
//                    List<ProductJson> p2List = readJsonStream(in2);
                        List<ProductJson> pList = readJsonStream(in);
//                        pList.addAll(p2List);
//                        r.setLenient(true);
//                        List<ProductJson> pList = gson.fromJson(fr, type);


                    pDao = daoFactory.getProductDAO();
                    aDao = daoFactory.getAvaliacaoDAO();
                    eDao = daoFactory.getEntregaDAO();
                    gDao = daoFactory.getPagamentoDAO();

                    Product prod;
                    Avaliacao ava;
                    Entrega etg;
                    Pagamento pag;
                    int pId;

                    Iterator<ProductJson> itJ = pList.iterator();
                    while(itJ.hasNext()){
                        ProductJson pr = itJ.next();
                        prod = pr.getProduto();
                        pId = pDao.create(prod);

                        List<Avaliacao> aList = pr.getAvaliacoas();
                        Iterator<Avaliacao> itA = aList.iterator();
                        while(itA.hasNext()){
                            ava = itA.next();
                            ava.setProductId(pId);
                            aDao.create(ava);
                        }

                        List<Entrega> eList = pr.getEntregas();
                        Iterator<Entrega> itE = eList.iterator();
                        while(itE.hasNext()){
                            etg = itE.next();
                            etg.setProductId(pId);
                            eDao.create(etg);
                        }

                        List<Pagamento> gList = pr.getPagamentos();
                        Iterator<Pagamento> itG = gList.iterator();
                        while(itG.hasNext()){
                            pag = itG.next();
//                            System.out.println(pag.getValor());
                            pag.setProductId(pId);
                            gDao.create(pag);
                        }

                    }
                } catch(IOException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                }

                response.sendRedirect(request.getContextPath() + "/product");
                break;
            }
            case "/product/update": {
                Product prod;
                Avaliacao ava;
                Entrega etg;
                Pagamento pag;

                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    // Parse the request
                    pDao = daoFactory.getProductDAO();
                    aDao = daoFactory.getAvaliacaoDAO();
                    eDao = daoFactory.getEntregaDAO();
                    gDao = daoFactory.getPagamentoDAO();
                    
                    prod = new Product();
                    Integer id;
                    String nome;
                    String parame;
//                    String urlImg;
//                    String descricao;
//                    String modelo;
//                    String marca;
//                    String fichaTecnica;
//                    double valor;
                    
                    id = Integer.parseInt(request.getParameter("produtoId"));
                    if(id != null){
                        prod.setId(id);
                    }
                    nome = request.getParameter("produtoNome");
                    if(nome != null){
                        prod.setNome(nome);
                    }
                    prod.setUrlImg(request.getParameter("produtoUrl"));
                    prod.setDescricao(request.getParameter("produtoDescricao"));
                    prod.setModelo(request.getParameter("produtoModelo"));
                    prod.setMarca(request.getParameter("produtoMarca"));
                    prod.setFichaTecnica(request.getParameter("produtoFicha"));
                    prod.setValor(Double.parseDouble(request.getParameter("produtoValor")));
                    
                    pDao.update(prod);
                    
                    for(int i = 0; i < 200; i++){
                        ava = new Avaliacao();
                        parame = request.getParameter("avaliacaoId["+i+"]");
                        if (parame  != null){
                            id = Integer.parseInt(parame);
                            ava.setId(id);
                            ava.setNome(request.getParameter("avaliacaoNome["+id+"]"));
                            ava.setComentario(request.getParameter("avaliacaoComentario["+id+"]"));
                            ava.setEstrelas(Integer.parseInt(request.getParameter("avaliacaoEstrela["+id+"]")));
                            aDao.update(ava);
//                            System.out.println("ai: "+i);
                        } else {
                            break;
                        }
                    }
                    
                    for(int i = 0; i < 200; i++){
                        etg = new Entrega();
                        parame = request.getParameter("entregaId["+i+"]");
//                        System.out.println(parame);
                        if(parame != null){
                            id = Integer.parseInt(parame);
                            etg.setId(id);
                            etg.setNome(request.getParameter("entregaNome["+id+"]"));
                            etg.setValor(Double.parseDouble(request.getParameter("entregaValor["+id+"]")));
                            eDao.update(etg);
//                            System.out.println("ei: "+i);
                        } else {
                            break;
                        }
                    }
                    
                    for(int i = 0; i < 200; i++){
                        pag = new Pagamento();
                        parame = request.getParameter("pagamentoId["+i+"]");
//                        System.out.println(parame);
                        if(parame != null){
                            id = Integer.parseInt(parame);
                            pag.setId(id);
                            pag.setVezes(Integer.parseInt(request.getParameter("pagamentoVezes["+id+"]")));
                            pag.setValor(Double.parseDouble(request.getParameter("pagamentoValor["+id+"]")));
                            gDao.update(pag);
//                            System.out.println("pi: "+i);
                        } else {
                            break;
                        }
                    }

                    response.sendRedirect(request.getContextPath() + "/product");
                    
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
                    session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (Exception ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
                    session.setAttribute("error", "Erro ao gravar arquivo no servidor.");
                }
                break;
            }
            case "/product/delete": {
                String[] prods = request.getParameterValues("delete");

                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    pDao = daoFactory.getProductDAO();

                    try {
                        daoFactory.beginTransaction();
                        AvaliacaoDAO adDao = daoFactory.getAvaliacaoDAO();
                        EntregaDAO edDao = daoFactory.getEntregaDAO();
                        PagamentoDAO pdDao = daoFactory.getPagamentoDAO();
                        int id;
                        for (String prodId : prods) {
                            id = Integer.parseInt(prodId);
                            List<Avaliacao> aList = adDao.readByProductId(id);
                            List<Entrega> eList = edDao.readByProductId(id);
                            List<Pagamento> gList = pdDao.readByProductId(id);
                            for(Avaliacao l: aList){
                                adDao.delete(l.getId());
                            }
                            for(Entrega l: eList){
                                edDao.delete(l.getId());
                            }
                            for(Pagamento l: gList){
                                pdDao.delete(l.getId());
                            }
                            pDao.delete(id);
                        }

                        daoFactory.commitTransaction();
                        daoFactory.endTransaction();
                    } catch (SQLException ex) {
                        session.setAttribute("error", ex.getMessage());
                        daoFactory.rollbackTransaction();
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
                    session.setAttribute("error", ex.getMessage());
                } catch (SQLException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
                    session.setAttribute("rollbackError", ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/product");
                break;
            }       
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
