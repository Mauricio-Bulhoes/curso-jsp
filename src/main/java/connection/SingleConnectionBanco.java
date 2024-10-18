package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp?autoReconnect=true";
	private static String user = "postgres";
	private static String senha = "admin";
	
	//private static String banco = "jdbc:postgresql://localhost:5432/mauri4629_curso_jsp?autoReconnect=true";
	//private static String user = "mauri4629";
	//private static String senha = "ncOxmz0GxjdoZo4";
	
	
	
	private static Connection connection = null;
	
	
	public static Connection getConnection() {
		return connection;
	}
	
	
	static {
		conectar();
	}
	
	
	public SingleConnectionBanco() { /* quando tiver uma instância vai conectar */
		conectar();
	}
	
	
	private static void conectar() {
		
		try {
			
			if(connection == null) {
				Class.forName("org.postgresql.Driver"); /* Crrega o driver de conexão do banco */
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false); /* para não efetuar alterações no banco sem nosso comando */
			}
			
		} catch (Exception e) {
			e.printStackTrace(); /* Mostrar qualquer erro no momento de conectar */
		}
		
	}

}
