package br.edu.opet.projetointregador;

import androidx.annotation.NonNull;

public class Produto implements Comparable<Produto>{

    private String id;
    private String nome;
    private String preco;
    private String quantidade;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreço() {
        return preco;
    }

    public void setPreço(String preço) {
        this.preco = preço;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    @NonNull
    @Override
    public String toString(){
        return this.getNome()+"     Quantidade :"+quantidade+"     Preço: R$"+preco;
    }

    @Override
    public int compareTo(Produto produto) {
        return this.getNome().compareTo(produto.getNome());
    }

}
