/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author joao
 */
public class ProductsJson {
    
    private List<ProductJson> products;
    
    public ProductsJson(){
        this.products = new ArrayList<>();
    }

    public List<ProductJson> getProducts() {
        return products;
    }

    public void setProducts(List<ProductJson> products) {
        this.products = products;
    }
}
