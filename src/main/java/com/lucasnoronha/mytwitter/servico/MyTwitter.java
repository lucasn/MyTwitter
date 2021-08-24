package com.lucasnoronha.mytwitter.servico;

import com.lucasnoronha.mytwitter.excecao.*;
import com.lucasnoronha.mytwitter.repositorio.IRepositorioUsuario;
import com.lucasnoronha.mytwitter.usuario.Perfil;
import com.lucasnoronha.mytwitter.usuario.Tweet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class MyTwitter implements ITwitter{

    private IRepositorioUsuario repositorio;

    public MyTwitter(IRepositorioUsuario repositorio){
        this.repositorio = repositorio;
    }

    @Override
    public void criarPerfil(Perfil usuario) throws PEException{
        try{
            repositorio.cadastrar(usuario);
        } catch (UJCException e){
            throw new PEException(usuario.getUsuario());
        }
    }

    @Override
    public void cancelarPerfil(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);
        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);
        else
            tmpUsuario.setAtivo(false);
    }

    @Override
    public void tweetar(String usuario, String mensagem) throws PIException, MFPException{
        if (mensagem.length() < 1 || mensagem.length() > 140)
            throw new MFPException(usuario, mensagem);

        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);

        Tweet tweet = new Tweet(usuario, mensagem);
        tmpUsuario.addTweet(tweet);

        List<Perfil> seguidores = tmpUsuario.getSeguidores();
        seguidores.forEach(seguidor -> seguidor.addTweet(tweet));
    }

    @Override
    public List<Tweet> timeline(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);

        return tmpUsuario.getTimeline();
    }

    @Override
    public List<Tweet> tweets(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);

        return tmpUsuario.getTimeline().stream().filter(tweet -> tweet.getUsuario().equals(usuario)).collect(Collectors.toCollection(Vector::new));
    }

    @Override
    public void seguir(String seguidor, String seguido) throws PIException, PDException, SIException{
        Perfil tmpSeguidor = repositorio.buscar(seguidor);
        Perfil tmpSeguido = repositorio.buscar(seguido);

        if (tmpSeguidor == null)
            throw new PIException(seguidor);
        else if (!tmpSeguidor.isAtivo())
            throw new PDException(seguidor);
        else if (tmpSeguido == null)
            throw new PIException(seguido);
        else if (!tmpSeguido.isAtivo())
            throw new PDException(seguido);
        else if (seguidor.equals(seguido))
            throw new SIException(seguidor);

        tmpSeguido.addSeguidor(tmpSeguidor);
        tmpSeguidor.addSeguido(tmpSeguido);
    }

    @Override
    public int numeroSeguidores(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);

        return tmpUsuario.getSeguidores().size();
    }

    @Override
    public List<Perfil> seguidores(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);

        return tmpUsuario.getSeguidores();
    }

    @Override
    public List<Perfil> seguidos(String usuario) throws PIException, PDException{
        Perfil tmpUsuario = repositorio.buscar(usuario);

        if (tmpUsuario == null)
            throw new PIException(usuario);
        else if (!tmpUsuario.isAtivo())
            throw new PDException(usuario);

        return tmpUsuario.getSeguidos();
    }

}
