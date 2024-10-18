package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRepository {

	private Connection connection;
	
	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {
		
		String sql = "select * from model_login where upper(login) = upper(?) and upper(senha) = upper(?)";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		prepSt.setString(1, modelLogin.getLogin());
		prepSt.setString(2, modelLogin.getSenha());
		
		ResultSet rs = prepSt.executeQuery();
		
		if(rs.next()) {
			return true; /* autenticado */
		} else {
			return false; /* não autenticado */
		}
		
	}
	
}
