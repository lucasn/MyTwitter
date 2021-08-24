package com.lucasnoronha.mytwitter.usuario;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Vector;

public abstract class Perfil {

    private String usuario;
    private Vector<Perfil> seguidos;
    private Vector<Perfil> seguidores;
    private Vector<Tweet> timeline;
    private boolean ativo;

    public Perfil(String usuario){
        this.usuario = usuario;
        this.seguidos = new Vector<>();
        this.seguidores = new Vector<>();
        this.timeline = new Vector<>();
        this.ativo = true;
    }

    public void addSeguido(Perfil usuario){
        this.seguidos.add(usuario);
    }

    public void addSeguidor(Perfil usuario){
        this.seguidores.add(usuario);
    }

    public void addTweet(Tweet tweet){
        this.timeline.add(tweet);
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public Vector<Perfil> getSeguidos() {
        return this.seguidos;
    }

    public Vector<Perfil> getSeguidores() {
        return this.seguidores;
    }

    public Vector<Tweet> getTimeline() {
        return this.timeline;
    }

    public void setAtivo(boolean valor) {
        this.ativo = valor;
    }

    public boolean isAtivo() {
        return this.ativo;
    }
}
