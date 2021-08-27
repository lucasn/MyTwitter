package com.lucasnoronha.mytwitter.servico;

import com.lucasnoronha.mytwitter.excecao.*;
import com.lucasnoronha.mytwitter.usuario.Perfil;
import com.lucasnoronha.mytwitter.usuario.Tweet;

import java.util.List;


public interface ITwitter {

    void criarPerfil(Perfil usuario) throws PEException;
    void cancelarPerfil(String usuario) throws PIException, PDException;
    void tweetar(String usuario, String mensagem) throws PIException, MFPException;
    List<Tweet> timeline(String usuario) throws PIException, PDException;
    List<Tweet> tweets(String usuario) throws PIException, PDException;
    void seguir(String seguidor, String seguido) throws PIException, PDException, SIException;
    int numeroSeguidores(String usuario) throws PIException, PDException;
    int numeroSeguidos(String usuario) throws PIException, PDException;
    List<Perfil> seguidores(String usuario) throws PIException, PDException;
    List<Perfil> seguidos(String usuario) throws PIException, PDException;
    boolean estaSeguindo(String seguidor, String seguido) throws PIException, PDException, SIException;
}
