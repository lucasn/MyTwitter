package com.lucasnoronha.mytwitter.excecao;

public class UNCException extends Exception{

    private String usuario;

    public UNCException(String usuario){
        super("Usuário não cadastrado");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
