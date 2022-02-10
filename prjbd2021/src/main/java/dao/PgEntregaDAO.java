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
import model.Entrega;

/**
 *
 * @author joao
 */
public class PgEntregaDAO implements EntregaDAO{
    
    private final Connection connection;
    
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.entrega(id_produto, nome_transportadora, valor) " +
                                "VALUES(?, ?, ?) RETURNING id;";
    
    private static final String READ_QUERY =
                                "SELECT * " +
                                "FROM integracao_precos.entrega " +
                                "WHERE id = ?;";     
    private static final String READ_QUERY_BY_PRODUCT_ID =
                                "SELECT * " +
                                "FROM integracao_precos.entrega " +
                                "WHERE id_produto = ?;"; 
    
    private static final String UPDATE_QUERY =
                                "UPDATE integracao_precos.entrega " +
                                "SET id_produto = ?, nome_transportadora = ?, valor = ? " +
                                "WHERE id = ?;";

    private static final String DELETE_QUERY =
                                "DELETE FROM integracao_precos.entrega " +
                                "WHERE id = ?;";
    
    private static final String ALL_QUERY =
                                "SELECT id, nome_transportadora, valor " +
                                "FROM integracao_precos.entrega " +
                                "ORDER BY id;";
    
    public PgEntregaDAO(Connection connection){
        this.connection = connection;
    }
    
    @Override
    public List<Entrega> readByProductId(int id) throws SQLException{
        List<Entrega> etgList = new ArrayList<Entrega>();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_BY_PRODUCT_ID)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Entrega etg = new Entrega();
                    etg.setId(result.getInt("id"));
                    etg.setNome(result.getString("nome_transportadora"));
                    etg.setValor(result.getInt("valor"));
                    etg.setProductId(id);

                    etgList.add(etg);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar entrega.");    
        }
        return etgList;
    }

    @Override
    public int create(Entrega t) throws SQLException {
        int id = -1;
        PgDAOFactory df = new PgDAOFactory(this.connection);
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, t.getProductId());
            statement.setString(2, t.getNome());
            statement.setDouble(3, t.getValor());
            
            df.beginTransaction();
            statement.execute();
            ResultSet result = statement.getResultSet();
            if(result.next()){
                id = result.getInt(1);
            }
            df.commitTransaction();
            df.endTransaction();
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir entrega: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir entrega.");
            }        
        }
        return id;
    }

    @Override
    public Entrega read(Integer id) throws SQLException {
        Entrega etg = new Entrega();
        
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    etg.setId(id);
                    etg.setProductId(result.getInt("id_produto"));
                    etg.setNome(result.getString("nome"));
                    etg.setValor(result.getDouble("valor"));
                } else {
                    throw new SQLException("Erro ao visualizar: avaliacao não encontrada.");
                }
            } 
          
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao visualizar: entrega não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar entrega.");
            }    
        }
        return etg;
    }

    @Override
    public void update(Entrega t) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, t.getProductId());
            statement.setString(2, t.getNome());
            statement.setDouble(3, t.getValor());
            
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: entrega não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao editar: entrega não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar entrega: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar entrega.");
            }     
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: entrega não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao excluir: entrega não encontrado.");
            } else {
                throw new SQLException("Erro ao excluir entrega.");
            }        
        }
    }

    @Override
    public List<Entrega> all() throws SQLException {
        List<Entrega> etgList = new ArrayList<Entrega>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery()) {
            
            while(result.next()){
                Entrega etg = new Entrega();
                etg.setId(result.getInt("id"));
                etg.setNome(result.getString("nome"));
                etg.setValor(result.getInt("valor"));
                
                etgList.add(etg);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgEntregaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar entrega.");    
        }
        return etgList;
    }
    
}
