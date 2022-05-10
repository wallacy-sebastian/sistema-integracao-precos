/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonObject;
import dao.AvaliacaoDAO;
import dao.DAO;
import dao.DAOFactory;
import dao.EntregaDAO;
import dao.PagamentoDAO;
import dao.ProductDAO;
import java.time.Month;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
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
            "/product/checkString",
            "/product/show",
            "/product/show/view",
            "/product/show/search"
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
            if(name.equals("produto")){
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
                prod.setNome(reader.nextString().trim());
            } else if (name.equals("secao")){
                prod.setSecao(reader.nextInt());
            } else if (name.equals("urlImg")){
                prod.setUrlImg(reader.nextString());
            } else if (name.equals("descricao")){
                prod.setDescricao(reader.nextString());
            } else if (name.equals("modelo")){
                prod.setModelo(reader.nextString().trim());
            } else if (name.equals("marca")){
                prod.setMarca(reader.nextString().trim());
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
   
//    FUNÇÃO VERIFICA SE O PRODUTO JÁ ESTÁ PRESENTE NO BANCO, SE SIM INTEGRAM OS PRODUTOS 
    public int integracaoProduto(Product prod) throws ClassNotFoundException, IOException, SQLException{
        ProductDAO dao;
        List<Product> pList;
        double med = 0, s1 = 0, s2 = 0, s3 = 0;
        final double PORCENTAGEM_SIMILARIDADE = 0.60000;
        
        if(prod.getNome().isEmpty())
            prod.setNome("@@@@@@@");
        if (prod.getMarca().isEmpty())
            prod.setMarca("@@@@@@@");
        if(prod.getModelo().isEmpty())
            prod.setModelo("@@@@@@@");
        
        try(DAOFactory daoFactory = DAOFactory.getInstance()){
            dao = daoFactory.getProductDAO();
            pList = dao.all();
            for(Product prodDAO : pList){
                med = 0;
                s1 = 0;
                s2 = 0;
                s3 = 0;
                if(!prodDAO.getNome().isEmpty()){
                    s1 = StringParecida.compareStrings(prod.getNome(), prodDAO.getNome());
                }
                if(!prodDAO.getMarca().isEmpty()){
//                    System.out.println(prodDAO.getMarca()+" "+prod.getMarca());
                    s2 = StringParecida.compareStrings(prod.getMarca(), prodDAO.getMarca());
                }
                
                if(!prodDAO.getModelo().isEmpty()){
                    s3 = StringParecida.compareStrings(prod.getModelo(), prodDAO.getModelo());
                }
                
//                System.out.println("s1: "+s1+" s2: "+s2+" s3: "+s3);
                
                if(s1 > PORCENTAGEM_SIMILARIDADE || (s2 > PORCENTAGEM_SIMILARIDADE && s3 > PORCENTAGEM_SIMILARIDADE)){
                    return prodDAO.getId();
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(prod.getNome().equals("@@@@@@@"))
            prod.setNome("");
        if (prod.getMarca().equals("@@@@@@@"))
            prod.setMarca("");
        if(prod.getModelo().equals("@@@@@@@"))
            prod.setModelo("");
       
        return -1;
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
        ProductDAO prdDao;
        AvaliacaoDAO avalDao;
        Product prod;
        Avaliacao ava;
        Entrega etg;
        Pagamento pag;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/product": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    prdDao = daoFactory.getProductDAO();

                    List<Product> productList = prdDao.allMaster();
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
                ProductDAO prodDAO;
                try(DAOFactory daoFactory = DAOFactory.getInstance()) {
                    prodDAO = daoFactory.getProductDAO();
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
                        prodDAO.deleteIntegracaoprodutoId(id);
                        prodDAO.delete(id);
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
                    ProductDAO prDao = daoFactory.getProductDAO();
                    AvaliacaoDAO aDao = daoFactory.getAvaliacaoDAO();
                    EntregaDAO eDao = daoFactory.getEntregaDAO();
                    PagamentoDAO gDao = daoFactory.getPagamentoDAO();
                    int id = Integer.parseInt(request.getParameter("id"));
                    prod = prDao.read(id);
                    List<Avaliacao> aList = aDao.readByProductId(id);
                    List<Entrega> eList = eDao.readByProductId(id);
                    List<Pagamento> gList = gDao.readByProductId(id);
                    ProductJson pj = new ProductJson();
                    pj.setProduto(prod);
                    pj.setAvaliacoas(aList);
                    pj.setEntregas(eList);
                    pj.setPagamentos(gList);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(pj);
                    if(prod.getIntegracaoNumero() != null){
                        List<Integer> idList = prDao.getIntegracaoProdutoAll(prod.getIntegracaoNumero());
                        JsonElement je = gson.toJsonTree(pj);
                        JsonElement je2 = gson.toJsonTree(idList);
                        je.getAsJsonObject().add("produtosIntegrados", je2);
                        json = gson.toJson(je);
                    }
//                    System.out.println(json);
//                    response.getOutputStream().print(json);
                    response.getWriter().print(json);

                } catch (Exception ex) {
                    System.out.println("id:"+Integer.parseInt(request.getParameter("id")));
                    request.getSession().setAttribute("error", ex.getMessage());
//                    response.sendRedirect(request.getContextPath() + "/product");
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
            case "/product/show":{
                String rSearch = request.getParameter("searchInput");
                String rCat = request.getParameter("categoria");
                String rSort = request.getParameter("sort");
                String rLj = request.getParameter("loja");
//                System.out.println(rSearch+rSort+rLj+rCat);
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    prdDao = daoFactory.getProductDAO();
                    List<Product> pList;
                    if(rSearch == null){
                        rSearch = "";
                    }if(rCat == null){
                        rCat = "0";
                    } if (rSort == null){
                        rSort = "";
                    } else {
                        if(rSort.equals("1")){
                            rSort = "nome";
                        } else if (rSort.equals("2")){
                            rSort = "valor";
                        } else {
                            rSort = "id";
                        }
                    } if (rLj == null){
                        rLj = "0";
                    }
//                    System.out.println(rSearch+rSort+rLj+rCat);
                    pList = prdDao.search(rSearch, Integer.parseInt(rCat), rSort, Integer.parseInt(rLj));
                    
                    request.setAttribute("productList", pList);
                    request.setAttribute("MOUSE", Product.MOUSE);
                    request.setAttribute("MONITOR", Product.MONITOR);
                    request.setAttribute("KABUM", Product.KABUM);
                    request.setAttribute("AMERICANAS", Product.AMERICANAS);
                    request.setAttribute("LONDRITECH", Product.LONDRITECH);
                    //request.setAttribute("PONTOFRIO", Product.MONITOR); // A DEFINIR
                    dispatcher = request.getRequestDispatcher("/view/product/showList.jsp");
                    dispatcher.forward(request, response);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
                break;
            }
            case "/product/show/view":{
                List<Product> pList = null;
                List<Avaliacao> aList = null;
                List<Integer> meses = new ArrayList<Integer>();
                prod = null;
                String str = request.getParameter("id");
                String dataString;
                Integer iId;
                double soma = 0;
                int mes = 0, qtd = 0;
                int somaAval[] = new int[5];
                boolean isEmpty = false;
                boolean existe = false;
                Gson gsonObj = new Gson();
                Map<Object,Object> map = null;
                List<Map<Object,Object>> precoList = new ArrayList<Map<Object,Object>>();
                List<Map<Object,Object>> avalList = new ArrayList<Map<Object,Object>>();
                SimpleDateFormat simpleDateFormat = null;
                
                for(int i = 0; i < somaAval.length; i++) {
                    somaAval[i] = 0;
                }
                
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    prdDao = daoFactory.getProductDAO();
                    avalDao = daoFactory.getAvaliacaoDAO();
                    if(str != null){
                        Integer id = Integer.parseInt(str);
                        iId = prdDao.getIntegracaoProduto(id);
                        pList = prdDao.productMasterAndIntegracao(iId);
                        for(Product p: pList){
                            if(p.isMaster()){
                                prod = p;
                                soma += p.getValor();
                                qtd++;
                                aList = avalDao.readByProductId(p.getId());
                                for(Avaliacao a: aList) {
                                    somaAval[a.getEstrelas()-1]++;
                                }
                                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                dataString = simpleDateFormat.format(p.getCreatedAt());
                                LocalDate data
                                    = LocalDate.parse(dataString);
                                for(int m: meses) {
                                    if(data.getMonthValue() == m) {
                                        existe = true;
                                        break;
                                    }
                                }
                                
                                if(!existe) {
                                    mes = data.getMonthValue();
                                    map = new HashMap<Object,Object>();
                                    map.put("x", mes);
                                    map.put("y", soma/qtd);
                                    precoList.add(map);
                                    
                                    soma = 0;
                                    qtd = 0;
                                    meses.add(mes);
                                }
                                existe = false;
                            }
                        }
                    } else {
                        isEmpty = true;
                    }
                    for(int i = 0; i < somaAval.length; i++) {
                        map = new HashMap<Object,Object>();

                        map.put("y", somaAval[i]);
                        map.put("label", i+1);
                        avalList.add(map);
                    }
                    request.setAttribute("isEmpty", isEmpty);
                    request.setAttribute("pMaster", prod);
                    request.setAttribute("pHistorico", gsonObj.toJson(precoList));
                    request.setAttribute("pAvaliacoes", gsonObj.toJson(avalList));
                    request.setAttribute("productList", pList);
                    dispatcher = request.getRequestDispatcher("/view/product/showProduct.jsp");
                    dispatcher.forward(request, response);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
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
                String paramData = request.getParameter("loja-data");
                Product p = new Product();
                InputStream in = null;
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                java.lang.reflect.Type type = new TypeToken<List<ProductJson>>() {}.getType();
//                BufferedReader monitor = new BufferedReader( new FileReader("../../assets/json/americanas_monitor.jl"));
//                System.out.println(param);
                String appPath = request.getServletContext().getRealPath("");
                if(Integer.parseInt(param) == p.AMERICANAS){
                    in = new FileInputStream(appPath+"/assets/json/americanas-" + paramData + ".jl");
//                    in = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_monitor.jl");
                }else if(Integer.parseInt(param) == p.KABUM){
                    in = new FileInputStream(appPath+"/assets/json/kabum-" + paramData + ".jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/kabum_monitor.jl");
                }else if(Integer.parseInt(param) == p.LONDRITECH){
                    in = new FileInputStream(appPath+"/assets/json/londritech-" + paramData + ".jl");
//                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/londritech_monitor.jl");
                }else if(Integer.parseInt(param) == p.COLOMBO){
                    in = new FileInputStream(appPath+"/assets/json/colombo-" + paramData + ".jl");
    //                        InputStream in2 = new FileInputStream("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/londritech_monitor.jl");
                }
//                FileReader fr = new FileReader("/home/joao/Documentos/computacao/bd/prjbd2021/src/main/webapp/assets/json/americanas_mouse.jl");
//                JsonReader r = new JsonReader(fr);

                
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    List<ProductJson> pList = readJsonStream(in);
                    System.out.println(pList);
                    
                    
//                        r.setLenient(true);
//                        List<ProductJson> pList = gson.fromJson(fr, type);


                    pDao = daoFactory.getProductDAO();
                    aDao = daoFactory.getAvaliacaoDAO();
                    eDao = daoFactory.getEntregaDAO();
                    gDao = daoFactory.getPagamentoDAO();

                    Product prod, prod2;
                    Avaliacao ava;
                    Entrega etg;
                    Pagamento pag;
                    Integer integPid, integNum, pId;

                    Iterator<ProductJson> itJ = pList.iterator();
                    while(itJ.hasNext()){
                        
                        ProductJson pr = itJ.next();
                        prod = pr.getProduto();
                        integPid = integracaoProduto(prod);
//                        System.out.println("Inpid: "+integPid);
                    //  EXISTE PRODUTO SIMILAR A SER INTEGRADO
                        if(integPid > 0){
                            ProductDAO prodDAO = daoFactory.getProductDAO();
                            integNum = prodDAO.getIntegracaoProduto(integPid);
//                            System.out.println("In: "+integNum);
                        //  PRODUTO SIMILAR JA FOI INTEGRADO ALGUMA VEZ, LOGO JA POSSUI INTEGRACAO_ID
                            if(integNum > 0){
                                prod.setIntegracaoNumero(integNum);
                                pId = pDao.create(prod);
                                prodDAO.createIntegracaoProduto(integNum, pId);
                        //  PRODUTO SIMILAR NÃO FOI INTEGRADO NENHUMA VEZ, LOGO CRIA-SE INTEGRAÇÃO ENTRE OS PRODUTOS
                            } else {
                                integNum = prodDAO.getLastIntegracaoProdutoId();
                                integNum++;
//                                System.out.println("Last in: "+integNum);
                                prod.setIntegracaoNumero(integNum);
                                pId = pDao.create(prod);
                                prodDAO.createIntegracaoProduto(integNum, pId);
                                
                                prod2 = prodDAO.read(integPid);
                                prod2.setIntegracaoNumero(integNum);
                                prodDAO.updateIntegracaoprodutoId(prod2);
                                prodDAO.createIntegracaoProduto(integNum, prod2.getId());
                            }
                    //  NÃO EXISTE PRODUTO SIMILAR A SER INTEGRADO
                        } else {
                            prod.setIntegracaoNumero(-1);
                            pId = pDao.create(prod);
                        }

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
                    ProductDAO prDao = daoFactory.getProductDAO();

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
                            prDao.deleteIntegracaoprodutoId(id);
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
//            case "/product/show":{
//                ProductDAO prDao;
//                String str = request.getParameter("searchInput");
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    prDao = daoFactory.getProductDAO();
//                    List<Product> pList = prDao.search(str);
//                } catch (ClassNotFoundException | IOException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", ex.getMessage());
//                } catch (SQLException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("rollbackError", ex.getMessage());
//                }
//                
//                response.sendRedirect(request.getContextPath() + "/product/show/search");
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
