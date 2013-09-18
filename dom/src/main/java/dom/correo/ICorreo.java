package dom.correo;

import java.util.Properties;
import javax.mail.Session;

public interface ICorreo {
	
	void setSession(Properties propiedades);
	
	Session getSession();
	
	/*
	 * Propiedades para iniciar la sesión
	 * en donde por ejemplo se puede setear los puertos de los servidores
	 * POP y SMTP -> getProperties("mail.smtp.port","587");
	 */
	void setProperties();
	
	Properties getProperties();
	
	void accion();

}
