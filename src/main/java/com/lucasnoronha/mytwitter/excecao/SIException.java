package com.lucasnoronha.mytwitter.excecao;

public class SIException extends Exception{

    String usuario;

    public SIException(String usuario){
        super("Seguidor Inválido");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
