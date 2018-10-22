package jobs;

import models.Administrador;
import models.Usuario;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Inicializador extends Job {

	@Override
	public void doJob() throws Exception {
	
		if(Administrador.count() == 0) {
			
			Administrador adm1 = new Administrador();
			adm1.email = "dayvisonmsilva@hotmail.com";
			adm1.senha = "123";
			adm1.save();
			
			Administrador adm2 = new Administrador();
			adm2.email = "alef@gmail.com";
			adm2.senha = "123";
			adm2.save();
		}
	}
}
