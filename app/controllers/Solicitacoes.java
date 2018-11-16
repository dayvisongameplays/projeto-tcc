package controllers;

import java.util.List;

import models.Solicitacao;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Solicitacoes extends Controller{

	public static void listar() {
		List<Solicitacao> solicitacaos = Solicitacao.findAll();
		render(solicitacaos);
	}

}
