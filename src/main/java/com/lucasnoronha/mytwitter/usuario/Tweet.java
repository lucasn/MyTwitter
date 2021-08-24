package com.lucasnoronha.mytwitter.usuario;

public class Tweet {

    String usuario;
    String mensagem;

    public Tweet(String usuario, String mensagem){
        this.usuario = usuario;
        this.mensagem = mensagem;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

}
