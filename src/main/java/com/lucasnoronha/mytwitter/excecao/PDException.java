package com.lucasnoronha.mytwitter.excecao;

public class PDException extends Exception{

    private String usuario;

    public PDException(String usuario){
        super("Perfil Desativado");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
