package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import enums.TipoFreq;
import play.db.jpa.Model;

@Entity
public class Frequencia extends Model {

	@Temporal(TemporalType.TIME)
	public Date hora;
	
	@Temporal(TemporalType.DATE)
	public Date data;
	
	@Enumerated(EnumType.STRING)
	public TipoFreq tipoFreq;
	
	@ManyToOne
	@JoinColumn(name="usuario_id")
	public Usuario usuario;	
	
	@ManyToOne
	@JoinColumn(name="atividade_id")
	public Atividade atividade;	
	
}
