/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Pagamento;

/**
 *
 * @author joao
 */
public class PgPagamentoDAO implements PagamentoDAO {
    
    private final Connection connection;
    
    
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.pagamento(id_produto, tipo, vezes, valor) " +
                                "VALUES(?, ?, ?, ?) RETURNING id;";
    
    private static final String READ_QUERY =
                                "SELECT * " +
                                "FROM integracao_precos.pagamento " +
                                "WHERE id = ?;";      
    private static final String READ_QUERY_BY_PRODUCTO_ID =
                                "SELECT * " +
                                "FROM integracao_precos.pagamento " +
                                "WHERE id_produto = ?;";  
    
    private static final String UPDATE_QUERY =
                                "UPDATE integracao_precos.pagamento " +
                                "SET vezes = ?, valor = ? " +
                                "WHERE id = ?;";

    private static final String DELETE_QUERY =
                                "DELETE FROM integracao_precos.pagamento " +
                                "WHERE id = ?;";
    
    private static final String ALL_QUERY =
                                "SELECT id, tipo, vezes, valor " +
                                "FROM integracao_precos.pagamento " +
                                "ORDER BY id;";
    
    public PgPagamentoDAO(Connection connection){
        this.connection = connection;
    }
    
    @Override
    public List<Pagamento> readByProductId(int id) throws SQLException {
        List<Pagamento> pagList = new ArrayList<Pagamento>();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_BY_PRODUCTO_ID)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Pagamento pag = new Pagamento();
                    pag.setId(result.getInt("id"));
                    pag.setTipo(result.getInt("tipo"));
                    pag.setVezes(result.getInt("vezes"));
                    pag.setValor(result.getDouble("valor"));
                    pag.setProductId(id);

                    pagList.add(pag);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar pagamento.");    
        }
        return pagList;
    }

    @Override
    public int create(Pagamento t) throws SQLException {
        PgDAOFactory df = new PgDAOFactory(this.connection);
        int id = -1;
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, t.getProductId());
            statement.setInt(2, t.getTipo());
            statement.setInt(3, t.getVezes());
            statement.setDouble(4, t.getValor());
            
            df.beginTransaction();
            statement.execute();
            ResultSet result = statement.getResultSet();
            if(result.next()){
                id = result.getInt(1);
            }
            df.commitTransaction();
            df.endTransaction();
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            df.rollbackTransaction();
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir pagamento: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir pagamento.");
            }        
        }
        return id;
    }

    @Override
    public Pagamento read(Integer id) throws SQLException {
        Pagamento pag = new Pagamento();
        
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    pag.setId(id);
                    pag.setProductId(result.getInt("id_produto"));
                    pag.setTipo(result.getInt("tipo"));
                    pag.setValor(result.getDouble("valor"));
                } else {
                    throw new SQLException("Erro ao visualizar: avaliacao não encontrada.");
                }
            } 
          
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao visualizar: pagamento não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar pagamento.");
            }    
        }
        return pag;
    }

    @Override
    public void update(Pagamento t) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, t.getVezes());
            statement.setDouble(2, t.getValor());
            statement.setInt(3, t.getId());
            
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: pagamento não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao editar: pagamento não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar pagamento: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar pagamento.");
            }     
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
//            if (statement.executeUpdate() < 1) {
//                throw new SQLException("Erro ao excluir: pagamento não encontrada.");
//            }
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao excluir: pagamento não encontrado.");
            } else {
                throw new SQLException("Erro ao excluir pagamento.");
            }        
        }
    }

    @Override
    public List<Pagamento> all() throws SQLException {
        List<Pagamento> pagList = new ArrayList<Pagamento>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery()) {
            
            while(result.next()){
                Pagamento pag = new Pagamento();
                pag.setId(result.getInt("id"));
                pag.setTipo(result.getInt("tipo"));
                pag.setVezes(result.getInt("vezes"));
                pag.setValor(result.getDouble("valor"));
                
                pagList.add(pag);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgPagamentoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar pagamento.");    
        }
        return pagList;
    }
    
}
