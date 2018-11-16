package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Secure extends Controller {

	@Before
	static void checkAutenticated() {
		if (session.get("usuarioEmail") == null) {
			if(session.get("matriculaAdmin") == null){
				Logins.login();
			}
		} else {
			// System.out.println("Logado");
		}
	}
}
