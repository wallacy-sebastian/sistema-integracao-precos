/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dao.AvaliacaoDAO;
import dao.DAO;
import dao.DAOFactory;
import dao.EntregaDAO;
import dao.PagamentoDAO;
import dao.UserDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import model.User;
import model.ProductsJson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import model.ProductJson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import model.Avaliacao;
import model.Entrega;
import model.Pagamento;
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
            "/product/delete"
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
                    
                    System.out.println(prod.getId());
                    
                    AvaliacaoDAO aDao = daoFactory.getAvaliacaoDAO();
                    List<Avaliacao> aList = aDao.readByProductId(prod.getId());
                    request.setAttribute("aList", aList);
                    
                    EntregaDAO eDao = daoFactory.getEntregaDAO();
                    List<Entrega> eList = eDao.readByProductId(prod.getId());
                    request.setAttribute("eList", eList);
                    
                    PagamentoDAO pDao = daoFactory.getPagamentoDAO();
                    List<Pagamento> pList = pDao.readByProductId(prod.getId());
                    request.setAttribute("pList", pList);
                    System.out.println(pList);

                    dispatcher = request.getRequestDispatcher("/view/product/update.jsp");
                    dispatcher.forward(request, response);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/product");
                }
                break;
            }     
//            case "/user/delete": {
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    dao = daoFactory.getUserDAO();
//                    dao.delete(Integer.parseInt(request.getParameter("id")));
//                } catch (ClassNotFoundException | IOException | SQLException ex) {
//                    request.getSession().setAttribute("error", ex.getMessage());
//                }
//
//                response.sendRedirect(request.getContextPath() + "/user");
//                break;
//            }            
//            case "/user/read": {
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    dao = daoFactory.getUserDAO();
//
//                    user = dao.read(Integer.parseInt(request.getParameter("id")));
//
//                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
//                    String json = gson.toJson(user);
//
//                    response.getOutputStream().print(json);
//                } catch (ClassNotFoundException | IOException | SQLException ex) {
//                    request.getSession().setAttribute("error", ex.getMessage());
//                    response.sendRedirect(request.getContextPath() + "/user");
//                }
//                break;
//            }
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
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                java.lang.reflect.Type type = new TypeToken<List<ProductJson>>() {}.getType();
//                BufferedReader monitor = new BufferedReader( new FileReader("../../assets/json/americanas_monitor.jl"));
                if(Integer.parseInt(param) == p.AMERICANAS){
                    try(DAOFactory daoFactory = DAOFactory.getInstance()){
//                        FileReader fr = new FileReader("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                        JsonReader r = new JsonReader(fr);
                        InputStream in = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_monitor.jl");
//                        List<ProductJson> p2List = readJsonStream(in2);
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
                                System.out.println(pag.getValor());
                                pag.setProductId(pId);
                                gDao.create(pag);
                            }
                            System.out.println("@@@@@@@@@@@@@@");
                            
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(Integer.parseInt(param) == p.KABUM){
                
                }else if(Integer.parseInt(param) == p.LONDRITECH){
                
                }
                response.sendRedirect(request.getContextPath() + "/product");
            }
//            case "/product/update": {
//                // Se fosse um form simples, usaria request.getParameter()
//                // String login = request.getParameter("login");
//
//                // Manipulação de form com enctype="multipart/form-data"
//                // Create a factory for disk-based file items
//                DiskFileItemFactory factory = new DiskFileItemFactory();
//                // Set factory constraints
//                factory.setSizeThreshold(MAX_FILE_SIZE);
//                // Set the directory used to temporarily store files that are larger than the configured size threshold
//                factory.setRepository(new File("/tmp"));
//                // Create a new file upload handler
//                ServletFileUpload upload = new ServletFileUpload(factory);
//                // Set overall request size constraint
//                upload.setSizeMax(MAX_FILE_SIZE);
//
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    // Parse the request
//                    List<FileItem> items = upload.parseRequest(request);
//
//                    // Process the uploaded items
//                    Iterator<FileItem> iter = items.iterator();
//                    while (iter.hasNext()) {
//                        FileItem item = iter.next();
//
//                        // Process a regular form field
//                        if (item.isFormField()) {
//                            String fieldName = item.getFieldName();
//                            String fieldValue = item.getString();
//
//                            switch (fieldName) {
//                                case "login":
//                                    user.setLogin(fieldValue);
//                                    break;
//                                case "senha":
//                                    user.setSenha(fieldValue);
//                                    break;
//                                case "nome":
//                                    user.setNome(fieldValue);
//                                    break;
//                                case "nascimento":
//                                    java.util.Date dataNascimento = new SimpleDateFormat("yyyy-mm-dd").parse(fieldValue);
//                                    user.setNascimento(new Date(dataNascimento.getTime()));
//                                    break;
//                                case "id":
//                                    user.setId(Integer.parseInt(fieldValue));
//                            }
//                        } else {
//                            String fieldName = item.getFieldName();
//                            String fileName = item.getName();
//                            if (fieldName.equals("avatar") && !fileName.isBlank()) {
//                                // Dados adicionais (não usado nesta aplicação)
//                                String contentType = item.getContentType();
//                                boolean isInMemory = item.isInMemory();
//                                long sizeInBytes = item.getSize();
//
//                                // Pega o caminho absoluto da aplicação
//                                String appPath = request.getServletContext().getRealPath("");
//                                // Grava novo arquivo na pasta img no caminho absoluto
//                                String savePath = appPath + File.separator + SAVE_DIR + File.separator + fileName;
//                                File uploadedFile = new File(savePath);
//                                item.write(uploadedFile);
//
//                                user.setAvatar(fileName);
//                            }
//                        }
//                    }
//
//                    dao = daoFactory.getUserDAO();
//
//                    if (servletPath.equals("/user/create")) {
//                        dao.create(user);
//                    } else {
//                        servletPath += "?id=" + String.valueOf(user.getId());
//                        dao.update(user);
//                    }
//
//                    response.sendRedirect(request.getContextPath() + "/user");
//
//                } catch (ParseException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", "O formato de data não é válido. Por favor entre data no formato dd/mm/aaaa");
//                    response.sendRedirect(request.getContextPath() + servletPath);
//                } catch (FileUploadException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", "Erro ao fazer upload do arquivo.");
//                    response.sendRedirect(request.getContextPath() + servletPath);
//                } catch (ClassNotFoundException | IOException | SQLException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", ex.getMessage());
//                    response.sendRedirect(request.getContextPath() + servletPath);
//                } catch (Exception ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", "Erro ao gravar arquivo no servidor.");
//                    response.sendRedirect(request.getContextPath() + servletPath);
//                }
//                break;
//            }
//
//            case "/user/delete": {
//                String[] users = request.getParameterValues("delete");
//
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    dao = daoFactory.getUserDAO();
//
//                    try {
//                        daoFactory.beginTransaction();
//
//                        for (String userId : users) {
//                            dao.delete(Integer.parseInt(userId));
//                        }
//
//                        daoFactory.commitTransaction();
//                        daoFactory.endTransaction();
//                    } catch (SQLException ex) {
//                        session.setAttribute("error", ex.getMessage());
//                        daoFactory.rollbackTransaction();
//                    }
//                } catch (ClassNotFoundException | IOException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", ex.getMessage());
//                } catch (SQLException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("rollbackError", ex.getMessage());
//                }
//
//                response.sendRedirect(request.getContextPath() + "/user");
//                break;
//            }       
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
