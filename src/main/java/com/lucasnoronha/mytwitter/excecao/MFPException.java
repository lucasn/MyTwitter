package com.lucasnoronha.mytwitter.excecao;

public class MFPException extends Exception{

    private String usuario;
    private String texto;

    public MFPException(String usuario, String texto){
        super("Número máximo de caracteres excedido");
        this.usuario = usuario;
        this.texto = texto;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTexto() {
        return texto;
    }
}
