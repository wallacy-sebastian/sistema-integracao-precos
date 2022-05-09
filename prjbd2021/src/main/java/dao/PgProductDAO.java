/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.sun.tools.javac.util.StringUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;

/**
 *
 * @author joao
 */
public class PgProductDAO implements ProductDAO{
    
    public static void dd(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i=0; i<fields.length; i++)
        {
            fields[i].setAccessible(true);
            System.out.println(fields[i].getName() + " - " + fields[i].get(obj));
        } 

    }
    
    private final int MAX_STR = 450;
    
    private final Connection connection;
    
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.produto(nome, secao, url_imagem, descricao, modelo, marca, ficha_tecnica, valor, created_at, loja, integracao_id) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
    
    private static final String READ_QUERY =
                                "SELECT * " +
                                "FROM integracao_precos.produto " +
                                "WHERE id = ?;";      
    
    private static final String UPDATE_QUERY =
                                "UPDATE integracao_precos.produto " +
                                "SET nome = ?, url_imagem = ?, descricao = ?, modelo = ?, marca = ?, ficha_tecnica = ?, valor = ? " +
                                "WHERE id = ?;";

    private static final String DELETE_QUERY =
                                "DELETE FROM integracao_precos.produto " +
                                "WHERE id = ?;";
    
    private static final String ALL_QUERY =
                                "SELECT id, nome, valor, marca, modelo " +
                                "FROM integracao_precos.produto " +
                                "ORDER BY id;"; 
    
    private static final String CHECK_INTEGRACAO_PRODUTO =
                                "SELECT id_integracao " +
                                "FROM integracao_precos.produto_integracao " +
                                "WHERE id_produto = ?;"; 
    
    private static final String CREATE_INTEGRACAO_PRODUTO =
                                "INSERT INTO integracao_precos.produto_integracao(id_integracao, id_produto) " +
                                "VALUES (?, ?);";
    
    private static final String LAST_INTEGRACAO_PRODUTO_ID =
                                "SELECT MAX(id_integracao) AS id " +
                                "FROM integracao_precos.produto_integracao;";
    
    private static final String UPDATE_INTEGRACAO_PRODUTO_ID =
                                "UPDATE integracao_precos.produto " +
                                "SET integracao_id = ? " +
                                "WHERE id = ?;";
    
    private static final String DELETE_INTEGRACAO_PRODUTO_ID =
                                "DELETE FROM integracao_precos.produto_integracao " +
                                "WHERE id_produto = ?;";
    
    private static final String ALL_INTEGRACAO_PRODUTO_ID =
                                "SELECT id_produto FROM integracao_precos.produto_integracao " +
                                "WHERE id_integracao = ?;";
    
    private static final String ALL_MASTER =
                                "SELECT id, nome, valor, marca, modelo, url_imagem " +
                                "FROM integracao_precos.produto " +
                                "WHERE is_master IS TRUE " +
                                "ORDER BY id;";
    private static final String SEARCH = 
                                "SELECT id, nome, valor, marca, modelo, url_imagem FROM integracao_precos.produto " +
                                "WHERE is_master IS TRUE AND " +
                                "(UPPER(nome) LIKE '%'||UPPER(?)||'%' " +
                                "OR UPPER(descricao) LIKE '%'||UPPER(?)||'%' " +
                                "OR UPPER(modelo) LIKE '%'||UPPER(?)||'%' " +
                                "OR UPPER(marca) LIKE '%'||UPPER(?)||'%' " +
                                "OR UPPER(ficha_tecnica) LIKE '%'||UPPER(?)||'%')";
    private static final String PRODUCT_AND_INTEG =
                                "SELECT id, nome, valor, marca, modelo, url_imagem, created_at, id_integracao, loja, secao, is_master, ficha_tecnica, descricao " +
                                "FROM integracao_precos.produto p " +
                                "JOIN integracao_precos.produto_integracao pi ON p.id = pi.id_produto " +
                                "WHERE pi.id_integracao = ?";

    
    
    public PgProductDAO(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public int create(Product prod) throws SQLException {
        PgDAOFactory df = new PgDAOFactory(this.connection);
        int id = -1, in;
        String s;
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            s = prod.getNome();
            statement.setString(1, s.substring(0, Math.min(s.length(), MAX_STR)));
            statement.setInt(2, prod.getSecao());
            s = prod.getUrlImg();
            statement.setString(3, s.substring(0, Math.min(s.length(), MAX_STR)));
            s = prod.getDescricao();
            statement.setString(4, s.substring(0, Math.min(s.length(), MAX_STR)));
            statement.setString(5, prod.getModelo());
            statement.setString(6, prod.getMarca());
            s = prod.getFichaTecnica();
            statement.setString(7, s.substring(0, Math.min(s.length(), MAX_STR)));
            statement.setDouble(8, prod.getValor());
            statement.setDate(9, prod.getCreatedAt());
            statement.setInt(10, prod.getLoja());
            in = prod.getIntegracaoNumero();
            if(in > 0){
                statement.setInt(11, in);
            } else {
                statement.setNull(11, Types.INTEGER);
            }
            
            df.beginTransaction();
            statement.execute();
            ResultSet result = statement.getResultSet();
            if(result.next()){
                id = result.getInt(1);
            }
            df.commitTransaction();
            df.endTransaction();
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            df.rollbackTransaction();

            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir produto: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir produto.");
            }
        }
        return id;
    }
    
    @Override
    public Product read(Integer id) throws SQLException {
        Product prod = new Product();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    prod.setId(id);
                    prod.setNome(result.getString("nome"));
                    prod.setSecao(result.getInt("secao"));
                    prod.setUrlImg(result.getString("url_imagem"));
                    prod.setDescricao(result.getString("descricao"));
                    prod.setModelo(result.getString("modelo"));
                    prod.setMarca(result.getString("marca"));
                    prod.setFichaTecnica(result.getString("ficha_tecnica"));
                    prod.setValor(result.getDouble("valor"));
                    prod.setCreatedAt(result.getDate("created_at"));
                    prod.setIntegracaoNumero(result.getInt("integracao_id"));
                } else {
                    throw new SQLException("Erro ao visualizar: produto não encontrado.");
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            
            if (ex.getMessage().equals("Erro ao visualizar: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar produto.");
            }
        }

        return prod;
    }
    
    @Override
    public void update(Product prod) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, prod.getNome());
            statement.setString(2, prod.getUrlImg());
            statement.setString(3, prod.getDescricao());
            statement.setString(4, prod.getModelo());
            statement.setString(5, prod.getMarca());
            statement.setString(6, prod.getFichaTecnica());
            statement.setDouble(7, prod.getValor());
            statement.setInt(8, prod.getId());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: produto não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: produto não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar produto: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar produto.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: produto não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir produto.");
            }
        }
    }

    @Override
    public List<Product> all() throws SQLException {
        List<Product> prodList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Product prod = new Product();
                prod.setId(result.getInt("id"));
                prod.setNome(result.getString("nome"));
                prod.setMarca(result.getString("marca"));
                prod.setModelo(result.getString("modelo"));
                prod.setValor(result.getDouble("valor"));

                prodList.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar produtos.");
        }

        return prodList;
    }
    
    @Override
    public List<Product> allMaster() throws SQLException{
        List<Product> prodList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_MASTER);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Product prod = new Product();
                prod.setId(result.getInt("id"));
                prod.setNome(result.getString("nome"));
                prod.setMarca(result.getString("marca"));
                prod.setModelo(result.getString("modelo"));
                prod.setValor(result.getDouble("valor"));
                prod.setUrlImg(result.getString("url_imagem"));

                prodList.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar produtos.");
        }

        return prodList;
    }
    
    @Override
    public Integer getIntegracaoProduto(Integer productIid) throws SQLException{
        
        try(PreparedStatement statement = connection.prepareStatement(CHECK_INTEGRACAO_PRODUTO)){
            statement.setInt(1, productIid);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    return result.getInt("id_integracao");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao pegar integração ID");
            }
        }
        
        return -1;
    }
    
    @Override
    public void createIntegracaoProduto(Integer integId, Integer productId) throws SQLException{
        
        try(PreparedStatement statement = connection.prepareStatement(CREATE_INTEGRACAO_PRODUTO)){
            statement.setInt(1, integId);
            statement.setInt(2, productId);
            statement.execute();
        } catch(SQLException ex){
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao integrar produto");
        }
    }
    
    @Override
    public Integer getLastIntegracaoProdutoId() throws SQLException{
        try(PreparedStatement statement = connection.prepareStatement(LAST_INTEGRACAO_PRODUTO_ID)){
            ResultSet result = statement.executeQuery();
            Integer id = null;
            if(result.next()){
                id = result.getInt("id");
                if(id != null){
                    return id;
                }else{
                    return 0;
                }
            }else
                return 0;
            
        } catch(SQLException ex){
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao integrar produto");
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    @Override
    public void updateIntegracaoprodutoId(Product prod) throws SQLException{
        try(PreparedStatement statement = connection.prepareStatement(UPDATE_INTEGRACAO_PRODUTO_ID)){
            statement.setInt(1, prod.getIntegracaoNumero());
            statement.setInt(2, prod.getId());
            statement.execute();
        } catch(SQLException ex){
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao integrar produto");
        }
    }
    
    @Override
    public void deleteIntegracaoprodutoId(Integer id) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(DELETE_INTEGRACAO_PRODUTO_ID)) {
            statement.setInt(1, id);
            statement.executeUpdate();
//            if (statement.executeUpdate() < 1) {
//                throw new SQLException("Erro ao excluir: produto_integracao não encontrado.");
//            } else {
//                System.out.println("aaaaaa");
//            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: produto_integracao não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir produto_integracao.");
            }
        }
    }
    
    @Override
    public List<Integer> getIntegracaoProdutoAll(Integer id) throws SQLException{
        List<Integer> idList = new ArrayList<Integer>();
        
        try(PreparedStatement statement = connection.prepareStatement(ALL_INTEGRACAO_PRODUTO_ID)){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    idList.add(result.getInt("id_produto"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao pegar lista de ID's do produto");
            }
        } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao pegar lista de ID's do produto");
        }
        return idList;
    }
    
    @Override
    public List<Product> search(String str) throws SQLException{
        List<Product> pList = new ArrayList<Product>();
        
        try(PreparedStatement statement = connection.prepareStatement(SEARCH)){
            statement.setString(1, str);
            statement.setString(2, str);
            statement.setString(3, str);
            statement.setString(4, str);
            statement.setString(5, str);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                Product prod = new Product();
                prod.setId(result.getInt("id"));
                prod.setNome(result.getString("nome"));
                prod.setMarca(result.getString("marca"));
                prod.setModelo(result.getString("modelo"));
                prod.setValor(result.getDouble("valor"));
                prod.setUrlImg(result.getString("url_imagem"));

                pList.add(prod);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao executar buscar produto.");
            }
        } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao preparar buscar produto.");
        }
        return pList;
    }
    @Override
    public List<Product> productMasterAndIntegracao(Integer id) throws SQLException{
        List<Product> pList = new ArrayList<Product>();
        //id, nome, valor, marca, modelo, url_imagem, created_at, id_integracao
        try(PreparedStatement statement = connection.prepareStatement(PRODUCT_AND_INTEG)){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Product prod = new Product();
                    prod.setId(result.getInt("id"));
                    prod.setNome(result.getString("nome"));
                    prod.setMarca(result.getString("marca"));
                    prod.setModelo(result.getString("modelo"));
                    prod.setValor(result.getDouble("valor"));
                    prod.setUrlImg(result.getString("url_imagem"));
                    prod.setCreatedAt(result.getDate("created_at"));
                    prod.setLoja(result.getInt("loja"));
                    prod.setIntegracaoNumero(result.getInt("id_integracao"));
                    prod.setSecao(result.getInt("secao"));
                    prod.setIsMaster(result.getBoolean("is_master"));
                    prod.setFichaTecnica(result.getString("ficha_tecnica"));
                    prod.setDescricao(result.getString("descricao"));

                    pList.add(prod);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao executar buscar produto.");
            }
        } catch (SQLException ex) {
                Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
                throw new SQLException("Erro ao preparar buscar produto.");
        }
        return pList;
    }
}

