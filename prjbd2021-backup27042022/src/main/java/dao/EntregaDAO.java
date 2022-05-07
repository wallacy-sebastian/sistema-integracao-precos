/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Entrega;

/**
 *
 * @author joao
 */
public interface EntregaDAO extends DAO<Entrega> {
    public List<Entrega> readByProductId(int id) throws SQLException;
}
