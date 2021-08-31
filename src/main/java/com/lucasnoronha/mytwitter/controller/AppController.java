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
        //TODO: remover testes
        PessoaFisica p1 = new PessoaFisica("lucas");
        p1.setCpf(123L);
        try {
            this.application.criarPerfil(p1);
        } catch (PEException e){

        }
        PessoaFisica p2 = new PessoaFisica("cleber");
        p2.setCpf(123L);
        try {
            this.application.criarPerfil(p2);
        } catch (PEException e){

        }
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
                model.addAttribute("quanttweets", timeline.size());
            } catch (PIException e){
                model.addAttribute("erro", e.getMessage());
                return "error";
            } catch (PDException e){}
            model.addAttribute("usuario", usuarioAtual);
            return "timeline";
        }
    }

    @GetMapping("/cadastrar")
    public String paginaCadastro(){
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String realizarCadastro(@RequestParam String nome, String identificador, String tipo, Model model){
        if (tipo.equals("pessoafisica")){
            try {
                PessoaFisica novoUsuario = new PessoaFisica(nome);
                novoUsuario.setCpf(Long.parseLong(identificador));
                application.criarPerfil(novoUsuario);
            } catch (PEException e){
                model.addAttribute("erro", e.getMessage());
                return "error";
            }
        }
        else if (tipo.equals("pessoajuridica")){
            try {
                PessoaJuridica novoUsuario = new PessoaJuridica(nome);
                novoUsuario.setCnpj(Long.parseLong(identificador));
                application.criarPerfil(novoUsuario);
            } catch (PEException e){
                model.addAttribute("erro", e.getMessage());
                return "error";
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
    public String tweetar(Model model){
        if (usuarioAtual == null){
            model.addAttribute("erro", "Não está logado");
            return "error";
        }
        model.addAttribute("usuario", usuarioAtual);
        return "tweetar";
    }

    @PostMapping("/tweetar")
    public String tweetar(@RequestParam String mensagem, Model model){
        if (usuarioAtual == null){
            model.addAttribute("erro", "Não está logado");
            return "error";
        }
        try {
            application.tweetar(usuarioAtual.getUsuario(), mensagem);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (MFPException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        }

        return "redirect:/";
    }

    @GetMapping("/perfil")
    public String paginaPerfil(@RequestParam String usuario, Model model){
        Perfil tmpUsuario = repositorio.buscar(usuario);
        if (tmpUsuario == null){
            model.addAttribute("erro", "Usuário não encontrado");
            return "error";
        }
        try {
            boolean estaSeguindo = application.estaSeguindo(usuarioAtual.getUsuario(), usuario);
            model.addAttribute("estaseguindo", estaSeguindo);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e){}
          catch (SIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        }
        model.addAttribute("usuario", usuarioAtual);
        model.addAttribute("usuarioperfil", usuario);
        model.addAttribute("isAtual", (usuarioAtual.getUsuario().equals(usuario)));
        model.addAttribute("isativo", tmpUsuario.isAtivo());
        try {
            List<Tweet> tmpTweets = application.tweets(usuario);
            model.addAttribute("tweets", tmpTweets);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e){}

        try {
            int numeroSeguidores = application.numeroSeguidores(usuario);
            int numeroSeguidos = application.numeroSeguidos(usuario);
            model.addAttribute("numeroseguidores", numeroSeguidores);
            model.addAttribute("numeroseguidos", numeroSeguidos);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e){}
        return "perfil";
    }

    @GetMapping("/editarperfil")
    public String editarPerfil(Model model){
        model.addAttribute("usuario", usuarioAtual);
        model.addAttribute("isativo", usuarioAtual.isAtivo());
        if (usuarioAtual instanceof PessoaFisica) model.addAttribute("id", ((PessoaFisica) usuarioAtual).getCpf());
        else model.addAttribute("id", ((PessoaJuridica) usuarioAtual).getCnpj());
        return "editarperfil";
    }

    @PostMapping("/atualizarperfil")
    public String atualizarPerfil(@RequestParam String identificador, String usuario, Model model){
        if (!usuario.equals(usuarioAtual.getUsuario())){
            model.addAttribute("erro", "Acesso Negado");
            return "error";
        }
        if (usuarioAtual instanceof PessoaFisica){
            ((PessoaFisica) usuarioAtual).setCpf(Long.parseLong(identificador));
        }
        else {
            ((PessoaJuridica) usuarioAtual).setCnpj(Long.parseLong(identificador));
        }
        try {
            repositorio.atualizar(usuarioAtual);
        } catch (UNCException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        }
        return "redirect:/perfil?usuario=" + usuario;
    }

    @GetMapping("/desativarconta")
    public String desativarConta(@RequestParam String usuario, Model model){
        if (!usuario.equals(usuarioAtual.getUsuario())){
            model.addAttribute("erro", "Acesso Negado");
            return "error";
        }
        Perfil tmp = repositorio.buscar(usuario);
        if (tmp == null){
            model.addAttribute("erro", "Perfil Inexistente");
            return "error";
        }
        tmp.setAtivo(false);
        usuarioAtual.setAtivo(false);
        return "redirect:/perfil?usuario=" + usuario;
    }

    @GetMapping("/ativarconta")
    public String ativarConta(@RequestParam String usuario, Model model){
        if (!usuario.equals(usuarioAtual.getUsuario())){
            model.addAttribute("erro", "Acesso Negado");
            return "error";
        }
        Perfil tmp = repositorio.buscar(usuario);
        if (tmp == null){
            model.addAttribute("erro", "Perfil Inexistente");
            return "error";
        }
        tmp.setAtivo(true);
        usuarioAtual.setAtivo(true);
        return "redirect:/perfil?usuario=" + usuario;
    }

    @GetMapping("/buscarusuario")
    public String buscarUsuario(@RequestParam(required = false) String usuario, Model model){
        if (usuario == null){
            model.addAttribute("erro", "Usuário não encontrado");
            return "error";
        }
        else {
            Perfil tmpUsuario = repositorio.buscar(usuario);
            if (tmpUsuario == null){
                model.addAttribute("erro", "Usuário não encontrado");
                return "error";
            }
            else {
                return "redirect:/perfil?usuario=" + usuario;
            }
        }
    }

    @PostMapping("/seguir")
    public String seguir(@RequestParam String usuario, Model model){
        try{
            application.seguir(usuarioAtual.getUsuario(), usuario);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e){
            model.addAttribute("erro","Impossível Seguir Perfil Desativado");
            return "error";
        } catch (SIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        }
        return "redirect:/perfil?usuario=" + usuario;
    }

    @GetMapping("/seguidores")
    public String seguidores(@RequestParam(required = false) String usuario, Model model){
        try{
            List<Perfil> tmpSeguidores =  application.seguidores(usuario);
            int quantSeguidores =  application.numeroSeguidores(usuario);
            model.addAttribute("seguidores", tmpSeguidores);
            model.addAttribute("quantseguidores", quantSeguidores);
            model.addAttribute("usuarioatual", usuario);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e){}
        model.addAttribute("usuario", usuarioAtual);
        return "seguidores";
    }

    @GetMapping("/seguidos")
    public String seguidos(@RequestParam(required = false) String usuario, Model model){
        try {
            List<Perfil> tmpSeguidos = application.seguidos(usuario);
            int quantSeguidos = application.numeroSeguidos(usuario);
            model.addAttribute("seguidos", tmpSeguidos);
            model.addAttribute("quantseguidos", quantSeguidos);
            model.addAttribute("usuarioatual", usuario);
        } catch (PIException e){
            model.addAttribute("erro", e.getMessage());
            return "error";
        } catch (PDException e) {}
        model.addAttribute("usuario", usuarioAtual);
        return "seguidos";
    }
}
