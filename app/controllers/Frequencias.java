package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import enums.TipoFreq;
import models.Atividade;
import models.Frequencia;
import models.PegarData;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.With;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.joda.time.Years;

@With(Secure.class)
public class Frequencias extends Controller {

	public static void formFrequencia() {
		List<Atividade> atividades = Atividade.findAll();
		List<Usuario> usuarios = Usuario.findAll();
		render(atividades, usuarios);
	}

	@SuppressWarnings("deprecation")
	public static void salvar(Frequencia frequencia) throws ParseException {
		if (frequencia.atividade == null || frequencia.usuario == null) {
			flash.error("Erro ao cadastrar! Existem dependências a serem resolvidas. "
					+ "Provavelmente recadastramento de outras entidades.");
			formFrequencia();
		}
		
		Frequencia f = null;

		//PONTO DE PARTIDA
		frequencia.data = pegarData();
		frequencia.hora = pegarHora();

		List<Frequencia> frequencia2 = Frequencia.find("usuario = ?", frequencia.usuario).fetch();
		int cont = frequencia2.size();

		if (cont > 0) {
			f = frequencia2.get(cont - 1);
		}

		if (f == null || f.tipoFreq == null) {
			frequencia.tipoFreq = TipoFreq.ENTRADA;
		} else if (f.tipoFreq.equals(TipoFreq.SAIDA)) {
			frequencia.tipoFreq = TipoFreq.ENTRADA;
		} else {
			if (frequencia.data.getDate() != f.data.getDate()) {
				frequencia.tipoFreq = TipoFreq.ENTRADA;
			} else {
				if (f.atividade != frequencia.atividade || f.atividade == null) {
					flash.error("Você não fechou a atividade anterior!");
					formFrequencia();
				}
				frequencia.tipoFreq = TipoFreq.SAIDA;
			}
		}

		frequencia.save();
		flash.success("Frequência cadastrada com sucesso!");
		listar();
	}

	@SuppressWarnings("deprecation")
	public static void contarHoras(Long id) {
		int horasCalc = 0;
		// Salva todas as ocorrências de Frequência com determinado usuário
		Frequencia f = Frequencia.findById(id);
		List<Frequencia> frequencias = Frequencia.find("usuario = ?", f.usuario).fetch();
		int cont = frequencias.size();

		if (cont % 2 == 0) {
			for (int i = 0; i < frequencias.size(); i = i + 2) {
				// Pega uma Frequência (ENTRADA) e a sua próxima (SAIDA)
				Frequencia freqTemp = frequencias.get(i);
				Frequencia freqTemp2 = frequencias.get(i + 1);

				DateTime start = new DateTime(freqTemp.data.getYear(), freqTemp.data.getMonth(),
						freqTemp.data.getDate(), freqTemp.hora.getHours(), freqTemp.hora.getMinutes());
				DateTime end = new DateTime(freqTemp2.data.getYear(), freqTemp2.data.getMonth(),
						freqTemp2.data.getDate(), freqTemp2.hora.getHours(), freqTemp2.hora.getMinutes());
				Duration dur = new Duration(start, end);

				horasCalc = horasCalc + (int) dur.getStandardHours();
				// System.out.printf("DURAÇÃO = %s", dur.getStandardHours());
			}
		} else {
			frequencias.remove(cont - 1);
			for (int i = 0; i < frequencias.size(); i = i + 2) {
				Frequencia freqTemp = frequencias.get(i);
				Frequencia freqTemp2 = frequencias.get(i + 1);

				DateTime start = new DateTime(freqTemp.data.getYear(), freqTemp.data.getMonth(),
						freqTemp.data.getDate(), freqTemp.hora.getHours(), freqTemp.hora.getMinutes());
				DateTime end = new DateTime(freqTemp2.data.getYear(), freqTemp2.data.getMonth(),
						freqTemp2.data.getDate(), freqTemp2.hora.getHours(), freqTemp2.hora.getMinutes());
				Duration dur = new Duration(start, end);

				horasCalc = horasCalc + (int) dur.getStandardHours();
			}
		}
		render(f, horasCalc);
	}
	
	public static void contarHorasIntervalo(PegarData pegarData, Long id) throws ParseException {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		
		String tini = formato.format(pegarData.dataInicio);
		String tfim = formato.format(pegarData.dataFim);
		Date dateI = null;
		Date dateF = null;
		
		try {
			dateI = formato.parse(tini);
			dateF = formato.parse(tfim);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Busca no banco frequencias com aquele intervalo de tempo
		Frequencia f = Frequencia.findById(id);
		List<Frequencia> frequencias = Frequencia.find("data >= ? and data <= ? and usuario = ?", dateI, dateF, f.usuario).fetch();
		int cont = frequencias.size();

		int horasCalc = 0;

		if (cont % 2 == 0) {
			for (int i = 0; i < frequencias.size(); i = i + 2) {
				// Pega uma Frequência (ENTRADA) e a sua próxima (SAIDA)
				Frequencia freqTemp = frequencias.get(i);
				Frequencia freqTemp2 = frequencias.get(i + 1);

				DateTime start = new DateTime(freqTemp.data.getYear(), freqTemp.data.getMonth(),
						freqTemp.data.getDate(), freqTemp.hora.getHours(), freqTemp.hora.getMinutes());
				DateTime end = new DateTime(freqTemp2.data.getYear(), freqTemp2.data.getMonth(),
						freqTemp2.data.getDate(), freqTemp2.hora.getHours(), freqTemp2.hora.getMinutes());
				Duration dur = new Duration(start, end);

				horasCalc = horasCalc + (int) dur.getStandardHours();
				// System.out.printf("DURAÇÃO = %s", dur.getStandardHours());
			}
		} else {
			frequencias.remove(cont - 1);
			for (int i = 0; i < frequencias.size(); i = i + 2) {
				Frequencia freqTemp = frequencias.get(i);
				Frequencia freqTemp2 = frequencias.get(i + 1);

				DateTime start = new DateTime(freqTemp.data.getYear(), freqTemp.data.getMonth(),
						freqTemp.data.getDate(), freqTemp.hora.getHours(), freqTemp.hora.getMinutes());
				DateTime end = new DateTime(freqTemp2.data.getYear(), freqTemp2.data.getMonth(),
						freqTemp2.data.getDate(), freqTemp2.hora.getHours(), freqTemp2.hora.getMinutes());
				Duration dur = new Duration(start, end);

				horasCalc = horasCalc + (int) dur.getStandardHours();
			}
		}
		
		render(f, horasCalc, frequencias);
	}
	
	public static void formIntervalo(Long id) {
		Frequencia usuarioFrequencia = Frequencia.findById(id);
		render(usuarioFrequencia);
	}

	public static void opcoesHora(Long id) {
		Frequencia usuarioFrequencia = Frequencia.findById(id);
		render(usuarioFrequencia);
	}
	
	public static void listar() {
		List<Frequencia> frequencias = Frequencia.findAll();
		render(frequencias);
	}
	
	public static void listarFreqUsuario(Long id) {
		Usuario usuario = Usuario.findById(id);
		List<Frequencia> frequencias = Frequencia.find("usuario = ?", usuario).fetch();
		render(frequencias);
	}
	
	//mod dia 13/10/2018
	public static Date pegarData() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();
		try {
			data = dateFormat.parse(dateFormat.format(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	//mod dia 13/10/2018
	public static Date pegarHora() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date hora = new Date();
		try {	
			hora = dateFormat.parse(dateFormat.format(hora));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hora;
	}
	
	@SuppressWarnings("deprecation")
	public static void salvarApp(String idAtividade, String matricula) throws ParseException {		
		Frequencia frequencia = new Frequencia();
		Atividade atividade = Atividade.find("id = ?", Long.parseLong(idAtividade)).first();
		Usuario usuario = Usuario.find("matricula = ?", matricula).first();
		
		frequencia.atividade = atividade;
		frequencia.usuario = usuario;
		
		if (frequencia.atividade == null || frequencia.usuario == null) {
			flash.error("Erro ao cadastrar! Existem dependências a serem resolvidas. "
					+ "Provavelmente recadastramento de outras entidades.");
			formFrequencia();
		}

		Frequencia f = null;

		frequencia.data = pegarData();
		frequencia.hora = pegarHora();

		List<Frequencia> frequencia2 = Frequencia.find("usuario = ?", frequencia.usuario).fetch();
		int cont = frequencia2.size();

		if (cont > 0) {
			f = frequencia2.get(cont - 1);
		}
		
		if (f == null || f.tipoFreq == null) {
			frequencia.tipoFreq = TipoFreq.ENTRADA;
		} else if (f.tipoFreq.equals(TipoFreq.SAIDA)) {
			frequencia.tipoFreq = TipoFreq.ENTRADA;
		} else {
			if (frequencia.data.getDate() != f.data.getDate()) {
				frequencia.tipoFreq = TipoFreq.ENTRADA;
			} else {
				if (f.atividade != frequencia.atividade || f.atividade == null) {
					renderText("existente");
				}
				frequencia.tipoFreq = TipoFreq.SAIDA;
			}
		}
		frequencia.save();	
		renderText("true");
	}

}
