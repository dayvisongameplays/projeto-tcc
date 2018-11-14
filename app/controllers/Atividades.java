package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import com.google.zxing.qrcode.encoder.QRCode;
import com.mysql.fabric.xmlrpc.base.Array;

import QRCode.CreateQR;
import models.Atividade;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Atividades extends Controller{
	
	public static void formAtividade() {
		render();
	}
	
	public static void academia() {
		render();
	}
	
	public static void salvar(@play.data.validation.Valid Atividade atividade) {
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			formAtividade();
		}
	
		boolean novaAtividade = atividade.id == null;
		atividade.save();
		if(novaAtividade) {
			String conteudoQRCode = "atividadeID="+atividade.id;
			atividade.qrCode = CreateQR.generateQrCodeBlob(conteudoQRCode);
			atividade.save();
		}
		detalhes(atividade.id);
	}
	
	public static void listar() {
		List<Atividade> atividades = Atividade.findAll();
		render(atividades);
	}
	
	public static void listarJson(){
		List<Atividade> atividades = Atividade.findAll();
		renderJSON(atividades);
	}
	
	public static void listarBase() {
		List<Atividade> atividades = Atividade.findAll();
		render(atividades);
	}
	
	public static void deletar(Long id) {
		Atividade atividade = Atividade.findById(id);
		atividade.delete();
		listar();
	}

	public static void editar(Long id) {
		Atividade atividade = Atividade.findById(id);
		renderTemplate("Atividades/formAtividade.html", atividade);
	}
	
	public static void detalhes(Long id) {
		Atividade atividade = Atividade.findById(id);
		render(atividade);
	}
	
	public  static  void qrCodeAtividade(Long  id) {
	    Atividade atividade = Atividade.findById(id);
	    notFoundIfNull(atividade);
	    response.setContentTypeIfNotSet(atividade.qrCode.type());
	    renderBinary(atividade.qrCode.get());
	}
	
	public static void gerarQRCodeAtividade(Long id) {
		Atividade atividade = Atividade.findById(id);
		String conteudoQRCode = "atividadeID="+atividade.id;
		atividade.qrCode = CreateQR.generateQrCodeBlob(conteudoQRCode);
		atividade.save();
		renderTemplate("Atividades/detalhes.html", atividade);
	}
}
