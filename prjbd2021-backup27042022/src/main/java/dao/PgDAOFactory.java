/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;

/**
 *
 * @author joao
 */
public class PgDAOFactory extends DAOFactory {

    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserDAO getUserDAO() {
        return new PgUserDAO(this.connection);
    }
    
    @Override
    public ProductDAO getProductDAO(){
        return new PgProductDAO(this.connection);
    }
    
    @Override
    public AvaliacaoDAO getAvaliacaoDAO(){
        return new PgAvaliacaoDAO(this.connection);
    }
    
    @Override
    public EntregaDAO getEntregaDAO(){
        return new PgEntregaDAO(this.connection);
    }
    
    @Override
    public PagamentoDAO getPagamentoDAO(){
        return new PgPagamentoDAO(this.connection);
    }
    
}
