package com.example.appdelivery.Model;

public class Produto {
    //antes de pegar do servidor firebase, devemos validar, nisso, vamos primeiro pegar as fotos do android

    private String foto;// esta como int primeiro, porque a foto sera pega do android, se for para o servidor será string, referente a foto
    private String nome;
    private String preco;
    private String descricao;

    //como estamos fazendo de forma estatica pegar fotos do android e nao do servidor, vamos usar construtor, construir

    //--------------------------------------------------
   // public Produto(int foto, String nome, String preco) { //irei apenas comentar, mas tambem faz parte da fake lista
    //    this.foto = foto;
     //   this.nome = nome;
      //  this.preco = preco;
  //}
    //---------------------------------------------------

   // public int getFoto() { //assim que troquei o int foto por String foto, esses dois blocos get e set ficaram vermelho, então, nao ire excluir apenas fazer outro
    //    return foto;
   // }

    // public void setFoto(int foto) {
    //    this.foto = foto;
   // }



    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
