package com.lucasnoronha.mytwitter.repositorio;

import com.lucasnoronha.mytwitter.excecao.UJCException;
import com.lucasnoronha.mytwitter.excecao.UNCException;
import com.lucasnoronha.mytwitter.usuario.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositorioUsuario implements IRepositorioUsuario{

    private List<Perfil> usuarios;

    public RepositorioUsuario(List<Perfil> usuarios){
        this.usuarios = usuarios;
    }

    @Override
    public Perfil buscar(String usuario) {
        for (Perfil perfil : usuarios)
            if (perfil.getUsuario().equals(usuario)) return perfil;
        return null;
    }

    @Override
    public void cadastrar(Perfil usuario) throws UJCException {
        if (buscar(usuario.getUsuario()) != null)
            throw new UJCException(usuario.getUsuario());
        usuarios.add(usuario);
    }

    @Override
    public void atualizar(Perfil usuario) throws UNCException {
        Perfil tmp = buscar(usuario.getUsuario());
        if (tmp == null)
            throw new UNCException(usuario.getUsuario());
        else {
            usuarios.remove(tmp);
            usuarios.add(usuario);
        }
    }
}
