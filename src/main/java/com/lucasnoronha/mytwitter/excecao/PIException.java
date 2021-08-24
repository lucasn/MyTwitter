package com.lucasnoronha.mytwitter.excecao;

public class PIException extends Exception{

    private String usuario;

    public PIException(String usuario){
        super("Perfil Inexistente");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
