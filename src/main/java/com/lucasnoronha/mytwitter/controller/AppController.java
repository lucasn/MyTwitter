package com.lucasnoronha.mytwitter.controller;

import com.lucasnoronha.mytwitter.excecao.*;
import com.lucasnoronha.mytwitter.repositorio.IRepositorioUsuario;
import com.lucasnoronha.mytwitter.servico.ITwitter;
import com.lucasnoronha.mytwitter.usuario.Perfil;
import com.lucasnoronha.mytwitter.usuario.PessoaFisica;
import com.lucasnoronha.mytwitter.usuario.PessoaJuridica;
import com.lucasnoronha.mytwitter.usuario.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AppController {

    private Perfil usuarioAtual = null;
    private final ITwitter application;
    private final IRepositorioUsuario repositorio;

    public AppController(ITwitter application, IRepositorioUsuario repositorio){
        this.application = application;
        this.repositorio = repositorio;
    }

    @GetMapping("/")
    public String home(Model model){
        if (usuarioAtual == null){
            return "index";
        }
        else{
            try {
                List<Tweet> timeline = application.timeline(usuarioAtual.getUsuario());
                model.addAttribute("timeline", timeline);
            } catch (PIException e){
                //TODO: tratar Exceção
            } catch (PDException e){
                //TODO: tratar Exceção
            }
            model.addAttribute("usuario", usuarioAtual);
            return "timeline";
        }
    }

    @GetMapping("/cadastrar")
    public String paginaCadastro(){
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String realizarCadastro(@RequestParam String nome, String identificador, String tipo){
        if (tipo.equals("pessoafisica")){
            try {
                PessoaFisica novoUsuario = new PessoaFisica(nome);
                novoUsuario.setCpf(Long.parseLong(identificador));
                application.criarPerfil(novoUsuario);
            } catch (PEException e){
                //TODO: tratar Exceção
            }
        }
        else if (tipo.equals("pessoajuridica")){
            try {
                PessoaJuridica novoUsuario = new PessoaJuridica(nome);
                novoUsuario.setCnpj(Long.parseLong(identificador));
                application.criarPerfil(novoUsuario);
            } catch (PEException e){
                //TODO: tratar Exceção
            }
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required=false) String usuario){
        if (usuario == null){
            return "login";
        }
        else {
            usuarioAtual = repositorio.buscar(usuario);
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(){
        usuarioAtual = null;
        return "redirect:/";
    }

    @GetMapping("/tweetar")
    public String tweetar(){
        if (usuarioAtual == null){
            return "redirect:/";
        }
        return "tweetar";
    }

    @PostMapping("/tweetar")
    public String tweetar(@RequestParam String mensagem){
        if (usuarioAtual == null){
            //TODO: tratar o caso de usuário nulo
            return "redirect:/";
        }
        try {
            application.tweetar(usuarioAtual.getUsuario(), mensagem);
        } catch (PIException e){
            //TODO: tratar Exceção
        } catch (MFPException e){
            //TODO: tratar Exceção
        }

        return "redirect:/";
    }

    @GetMapping("/perfil")
    public String paginaPerfil(@RequestParam String usuario, Model model){
        //TODO: tratar o caso de usuário nulo
        model.addAttribute("usuario", usuario);
        model.addAttribute("estaseguindo", estaSeguindo(usuario));
        try {
            List<Tweet> tmpTweets = application.tweets(usuario);
            model.addAttribute("tweets", tmpTweets);
        } catch (PIException e){
            //TODO: tratar Exceção
        } catch (PDException e){
            //TODO: tratar Exceção
        }
        return "usuario";
    }

    @GetMapping("/buscarusuario")
    public String buscarUsuario(@RequestParam(required = false) String usuario, Model model){
        if (usuario == null){
            return "buscarusuario";
        }
        else {
            Perfil tmpUsuario = repositorio.buscar(usuario);
            if (usuario == null){
                return "redirect:/";
            }
            else {
                return "redirect:/perfil?usuario=" + usuario;
            }
        }
    }

    @PostMapping("/seguir")
    public String seguir(@RequestParam String usuario){
        try{
            application.seguir(usuarioAtual.getUsuario(), usuario);
        } catch (PIException e){
            //TODO: tratar Exceção
        } catch (PDException e){
            //TODO: tratar Exceção
        } catch (SIException e){
            //TODO: tratar Exceção
        }
        return "redirect:/perfil?usuario=" + usuario;
    }

    public boolean estaSeguindo(String seguido){
        if (usuarioAtual == null){
            return false;
        }
        try {
            List<Perfil> seguindo =  application.seguidos(usuarioAtual.getUsuario());
            for (Perfil s : seguindo){
                if (s.getUsuario().equals(seguido)) return true;
            }
            return false;
        } catch (PIException e){
            //TODO: tratar Exceção
            return false;
        } catch (PDException e){
            //TODO: tratar Exceção
            return false;
        }
    }
}
