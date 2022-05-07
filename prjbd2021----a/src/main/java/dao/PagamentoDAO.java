/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Pagamento;

/**
 *
 * @author joao
 */
public interface PagamentoDAO extends DAO<Pagamento> {
    public List<Pagamento> readByProductId(int id) throws SQLException;
}
