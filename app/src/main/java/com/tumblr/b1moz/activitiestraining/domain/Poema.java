package com.tumblr.b1moz.activitiestraining.domain;

public class Poema {
    
    private int id;
    private String titulo;
    private String nomeAutor;
    private String data;
    private String categoria;
    private String conteudo;
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getNomeAutor() {
        return nomeAutor;
    }
    
    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getConteudo() {
        return conteudo;
    }
    
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    
}