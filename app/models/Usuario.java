package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Usuario extends Model {

	public String nome;
	public String matricula;
	public String tipoVinculo;
	public String url_foto_75x100;
	public String email;

	@OneToMany(mappedBy="usuario")
	public List<Frequencia> frequencias;
	
	@ManyToMany
	@JoinTable(name="usuarios_atividades")
	public List<Atividade> atividades;
	
	@OneToMany(mappedBy="usuario")
	public List<Solicitacao> solicitacoes;
	
}
