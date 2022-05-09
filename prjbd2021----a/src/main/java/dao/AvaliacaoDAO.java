/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Avaliacao;

/**
 *
 * @author joao
 */
public interface AvaliacaoDAO extends DAO<Avaliacao>{
    public List<Avaliacao> readByProductId(int id) throws SQLException;
}
