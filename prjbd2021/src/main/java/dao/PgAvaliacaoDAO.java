/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Avaliacao;

/**
 *
 * @author joao
 */
public class PgAvaliacaoDAO implements AvaliacaoDAO{
    
    private final Connection connection;
        
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.avaliacao(id_produto, nome, comentario, data, qtd_estrelas) " +
                                "VALUES(?, ?, ?, ?, ?) RETURNING id;";
    
    private static final String READ_QUERY =
                                "SELECT * " +
                                "FROM integracao_precos.avaliacao " +
                                "WHERE id = ?;";
    private static final String READ_QUERY_BY_PRODUCT_ID =
                                "SELECT * " +
                                "FROM integracao_precos.avaliacao " +
                                "WHERE id_produto = ?;";   
    
    private static final String UPDATE_QUERY =
                                "UPDATE integracao_precos.avaliacao " +
                                "SET nome = ?, comentario = ?, qtd_estrelas = ? " +
                                "WHERE id = ?;";

    private static final String DELETE_QUERY =
                                "DELETE FROM integracao_precos.avaliacao " +
                                "WHERE id = ?;";
    
    private static final String ALL_QUERY =
                                "SELECT id, nome, qtd_estrelas " +
                                "FROM integracao_precos.avaliacao " +
                                "ORDER BY id;";    
    
    public PgAvaliacaoDAO(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public List<Avaliacao> readByProductId(int id) throws SQLException{
        List<Avaliacao> avaList = new ArrayList<Avaliacao>();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_BY_PRODUCT_ID)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                while(result.next()){
                    Avaliacao ava = new Avaliacao();
                    ava.setId(result.getInt("id"));
                    ava.setNome(result.getString("nome"));
                    ava.setEstrelas(result.getInt("qtd_estrelas"));
                    ava.setComentario(result.getString("comentario"));
                    ava.setProductId(id);
                    avaList.add(ava);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar Avaliação.");    
        }
        return avaList;
    }

    @Override
    public int create(Avaliacao t) throws SQLException {
        PgDAOFactory df = new PgDAOFactory(this.connection);
        int id = -1;
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, t.getProductId());
            statement.setString(2, t.getNome());
            statement.setString(3, t.getComentario());
            statement.setDate(4, t.getData());
            statement.setInt(5, t.getEstrelas());
            
            df.beginTransaction();
            statement.execute();
            ResultSet result = statement.getResultSet();
            if(result.next()){
                id = result.getInt(1);
            }
            df.commitTransaction();
            df.endTransaction();
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            df.rollbackTransaction();
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir avaliacao: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir avaliacao.");
            }        
        }
        return id;
    }

    @Override
    public Avaliacao read(Integer id) throws SQLException {
        Avaliacao ava = new Avaliacao();
        
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()){
                if(result.next()){
                    ava.setId(id);
                    ava.setProductId(result.getInt("id_produto"));
                    ava.setNome(result.getString("nome"));
                    ava.setComentario(result.getString("comentario"));
                    ava.setData(result.getDate("data"));
                    ava.setEstrelas(result.getInt("qtd_estrelas"));
                } else {
                    throw new SQLException("Erro ao visualizar: avaliacao não encontrada.");
                }
            } 
          
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao visualizar: produto não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar produto.");
            }    
        }
        return ava;
    }

    @Override
    public void update(Avaliacao t) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, t.getNome());
            statement.setString(2, t.getComentario());
            statement.setInt(3, t.getEstrelas());
            statement.setInt(4, t.getId());
            
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: avaliacao não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().equals("Erro ao editar: avaliacao não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar avaliacao: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar avaliacao.");
            }     
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: avaliacao não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao excluir: avaliacao não encontrado.");
            } else {
                throw new SQLException("Erro ao excluir avaliacao.");
            }        
        }
    }

    @Override
    public List<Avaliacao> all() throws SQLException {
        List<Avaliacao> avaList = new ArrayList<Avaliacao>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery()) {
            
            while(result.next()){
                Avaliacao ava = new Avaliacao();
                ava.setId(result.getInt("id"));
                ava.setNome(result.getString("nome"));
                ava.setEstrelas(result.getInt("qtd_estrelas"));
                
                avaList.add(ava);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PgAvaliacaoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar Avaliação.");    
        }
        return avaList;
    }
    
}
