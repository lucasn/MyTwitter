package com.lucasnoronha.mytwitter.repositorio;

import com.lucasnoronha.mytwitter.excecao.UJCException;
import com.lucasnoronha.mytwitter.excecao.UNCException;
import com.lucasnoronha.mytwitter.usuario.Perfil;

public interface IRepositorioUsuario {

    Perfil buscar(String usuario);
    void cadastrar(Perfil usuario) throws UJCException;
    void atualizar(Perfil usuario) throws UNCException;

}
