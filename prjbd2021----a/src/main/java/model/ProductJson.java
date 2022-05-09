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
public class ProductJson {
    
    public ProductJson(){
        this.avaliacoas = new ArrayList<>();
        this.entregas = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
    }

    public Product getProduto() {
        return produto;
    }

    public void setProduto(Product produto) {
        this.produto = produto;
    }

    public List<Avaliacao> getAvaliacoas() {
        return avaliacoas;
    }

    public void setAvaliacoas(List<Avaliacao> avaliacoas) {
        this.avaliacoas = avaliacoas;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<Entrega> entregas) {
        this.entregas = entregas;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
    private Product produto;
    private List<Avaliacao> avaliacoas;
    private List<Entrega> entregas;
    private List<Pagamento> pagamentos;
}
