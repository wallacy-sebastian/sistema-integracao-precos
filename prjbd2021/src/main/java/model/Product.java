/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author joao
 */
public class Product {
    
    public static final int MOUSE = 1;
    public static final int MONITOR = 2;
    
    public static final int AMERICANAS = 1;
    public static final int KABUM = 2;
    public static final int LONDRITECH = 3;
    
    private Integer id;
    private String nome;
    private Integer secao;
    private String urlImg;
    private String descricao;
    private String modelo;
    private String marca;
    private String fichaTecnica;
    private double valor;
    private Date createdAt;
    private Integer loja;
    private Integer integracaoNumero;
    private boolean isMaster;

    public boolean isMaster() {
        return this.isMaster;
    }

    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    public Integer getIntegracaoNumero() {
        return integracaoNumero;
    }

    public void setIntegracaoNumero(Integer integracaoNumero) {
        this.integracaoNumero = integracaoNumero;
    }

    public Integer getLoja() {
        return loja;
    }

    public void setLoja(Integer loja) {
        this.loja = loja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSecao() {
        return secao;
    }

    public void setSecao(Integer secao) {
        this.secao = secao;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String url_img) {
        this.urlImg = url_img;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(String ficha_tecnica) {
        this.fichaTecnica = ficha_tecnica;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getLojaFromValue(int loja){
        if(this.AMERICANAS == loja){
            return "Americanas";
        } else if (this.KABUM == loja){
            return "Kabum";
        } else if(this.LONDRITECH == loja){
            return "Londritech";
        }
        return null;
    }
    
    public String getSecaoFromValue(int secao){
        if(this.MOUSE == secao){
            return "Mouse";
        } else {
            return "Monitor";
        }
    }
    
    public String getDataFromValue(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        String str = format.format(this.getCreatedAt());
        return str;
    }
    
    public String getPreco(){
        String preco = new DecimalFormat("¤ ###,###,##0.00").format(this.getValor());
        return preco;
    }
    
    public List<Integer> getLojaAll(){
       List lojas = new ArrayList<>();
       lojas.add(this.AMERICANAS);
       lojas.add(this.KABUM);
       lojas.add(this.LONDRITECH);
       return lojas;
    }
}
