package com.lucasnoronha.mytwitter.usuario;

public class PessoaJuridica extends Perfil{

    private long cnpj;

    public PessoaJuridica(String usuario){
        super(usuario);
    }

    public long getCnpj() {
        return cnpj;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }
}
