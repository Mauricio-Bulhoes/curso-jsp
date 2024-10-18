package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;

public class DAOVersionadorBanco implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	
	public DAOVersionadorBanco() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public void gravaArquivoSqlRodado (String nomeFile) throws Exception{
		
		String sql = "INSERT INTO versionadorbanco(arquivo_sql) VALUES (?);";
		
		PreparedStatement prSt = connection.prepareStatement(sql);
		prSt.setString(1, nomeFile);
		prSt.execute();
		
	}
	
	public boolean arquivoSqlRodado (String nomeDoArquivo) throws Exception {
		
		String sql = "select count(1) > 0 as rodado from versionadorbanco where arquivo_sql = ?";
		
		PreparedStatement prSt = connection.prepareStatement(sql);
		prSt.setString(1, nomeDoArquivo);
		
		ResultSet rs = prSt.executeQuery();
		rs.next();
		
		return rs.getBoolean("rodado");
		
	}

}
