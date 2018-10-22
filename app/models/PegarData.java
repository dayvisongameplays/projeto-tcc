package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class PegarData extends Model{

	@Temporal(TemporalType.DATE)
	public Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	public Date dataFim;

}
