/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;
//import java.sql.SQLException;
import java.sql.SQLException;
import java.util.List;
import model.Product;

/**
 *
 * @author joao
 */
public interface ProductDAO  extends DAO<Product> {
     public List<Product> allMaster() throws SQLException;
     public Integer getIntegracaoProduto(Integer productId) throws SQLException;
     public void createIntegracaoProduto(Integer integId, Integer productId) throws SQLException;
     public Integer getLastIntegracaoProdutoId() throws SQLException;
     public void updateIntegracaoprodutoId(Product p) throws SQLException;
     public void deleteIntegracaoprodutoId(Integer id) throws SQLException;
     public List<Integer> getIntegracaoProdutoAll(Integer id) throws SQLException;
     public List<Product> search(String search, Integer cat, String sort, Integer loja) throws SQLException;
     public List<Product> productMasterAndIntegracao(Integer id) throws SQLException;
}
