package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import QRCode.CreateQR;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Atividade extends Model{
	
	@Required
	public String nome;
	
	@Required
	public String descricao;
	
	public Blob qrCode;
	
	@OneToMany(mappedBy="atividade")
	public List<Frequencia> frequencias;
	
	@ManyToMany(mappedBy="atividades")
	public List<Usuario> usuarios;
	
	@OneToMany(mappedBy="atividade")
	public List<Solicitacao> solicitacoes;
	
	public Atividade() {
		this.usuarios = new ArrayList<Usuario>();
	}
}
