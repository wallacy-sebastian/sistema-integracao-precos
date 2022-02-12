/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    private final Connection connection;
    
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.produto(nome, secao, url_imagem, descricao, modelo, marca, ficha_tecnica, valor, created_at) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
    
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
                                "SELECT id, nome, valor " +
                                "FROM integracao_precos.produto " +
                                "ORDER BY id;";    
    
    public PgProductDAO(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public int create(Product prod) throws SQLException {
        PgDAOFactory df = new PgDAOFactory(this.connection);
        int id = -1;
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, prod.getNome());
            statement.setInt(2, prod.getSecao());
            statement.setString(3, prod.getUrlImg());
            statement.setString(4, prod.getDescricao());
            statement.setString(5, prod.getModelo());
            statement.setString(6, prod.getMarca());
            statement.setString(7, prod.getFichaTecnica());
            statement.setDouble(8, prod.getValor());
            statement.setDate(9, prod.getCreatedAt());
            
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
                } else {
                    throw new SQLException("Erro ao visualizar: produto não encontrado.");
                }
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
                prod.setValor(result.getDouble("valor"));

                prodList.add(prod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgProductDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar produtos.");
        }

        return prodList;
    }
}
