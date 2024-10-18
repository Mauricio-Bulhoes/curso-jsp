package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import beandto.BeanDtoGraficoSalarioUser;
import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {

	private Connection connection;
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado) throws Exception {
		
		String sql = "select avg(rendamensal)::numeric(15,2) as media_salarial, perfil from model_login where usuario_id = ? group by perfil";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setLong(1, userLogado);
		
		ResultSet resultado = prepSt.executeQuery();
		
		List<String> perfis = new ArrayList<String>();
		List<Double> salarios = new ArrayList<Double>();
		
		BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();
		
		while (resultado.next()) {
			Double mediaSalarial = resultado.getDouble("media_salarial");
			String perfil = resultado.getString("perfil");
			
			perfis.add(perfil);
			salarios.add(mediaSalarial);
		}
		
		beanDtoGraficoSalarioUser.setPerfis(perfis);
		beanDtoGraficoSalarioUser.setSalarios(salarios);
		
		return beanDtoGraficoSalarioUser;
	}

	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado, String dataInicial, String dataFinal) throws Exception{
		
		String sql = "select avg(rendamensal)::numeric(15,2) as media_salarial, perfil from model_login where usuario_id = ? and datanascimento >= ? and datanascimento <= ? group by perfil";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setLong(1, userLogado);
		prepSt.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
		prepSt.setDate(3, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
		
		ResultSet resultado = prepSt.executeQuery();
		
		List<String> perfis = new ArrayList<String>();
		List<Double> salarios = new ArrayList<Double>();
		
		BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();
		
		while (resultado.next()) {
			Double mediaSalarial = resultado.getDouble("media_salarial");
			String perfil = resultado.getString("perfil");
			
			perfis.add(perfil);
			salarios.add(mediaSalarial);
		}
		
		beanDtoGraficoSalarioUser.setPerfis(perfis);
		beanDtoGraficoSalarioUser.setSalarios(salarios);
		
		return beanDtoGraficoSalarioUser;
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception{
		
		if (objeto.isNovo()) {	
			String sql = "INSERT INTO model_login (login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement prepSt = connection.prepareStatement(sql);
			
			prepSt.setString(1, objeto.getLogin());
			prepSt.setString(2, objeto.getSenha());
			prepSt.setString(3, objeto.getNome());
			prepSt.setString(4, objeto.getEmail());
			prepSt.setLong(5, userLogado);
			prepSt.setString(6, objeto.getPerfil());
			prepSt.setString(7, objeto.getSexo());
			prepSt.setString(8, objeto.getCep());
			prepSt.setString(9, objeto.getLogradouro());
			prepSt.setString(10, objeto.getBairro());
			prepSt.setString(11, objeto.getLocalidade());
			prepSt.setString(12, objeto.getUf());
			prepSt.setString(13, objeto.getNumero());
			prepSt.setDate(14, objeto.getDataNascimento());
			prepSt.setDouble(15, objeto.getRendaMensal());
			
			prepSt.execute();
			
			connection.commit();
			
			if(objeto.getFotouser() != null && objeto.getFotouser().isEmpty()) {
				sql = "update model_login set fotouser = ?, extensaofotouser = ? where login = ?";
				
				prepSt = connection.prepareStatement(sql);
				
				prepSt.setString(1, objeto.getFotouser());
				prepSt.setString(2, objeto.getExtensaofotouser());
				prepSt.setString(3, objeto.getLogin());
				
				prepSt.execute();
				
				connection.commit();
			}
			
		} else {
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, datanascimento=?, rendamensal=? WHERE id=" + objeto.getId() + ";";
			PreparedStatement prepSt = connection.prepareStatement(sql);
			
			prepSt.setString(1, objeto.getLogin());
			prepSt.setString(2, objeto.getSenha());
			prepSt.setString(3, objeto.getNome());
			prepSt.setString(4, objeto.getEmail());
			prepSt.setString(5, objeto.getPerfil());
			prepSt.setString(6, objeto.getSexo());
			prepSt.setString(7, objeto.getCep());
			prepSt.setString(8, objeto.getLogradouro());
			prepSt.setString(9, objeto.getBairro());
			prepSt.setString(10, objeto.getLocalidade());
			prepSt.setString(11, objeto.getUf());
			prepSt.setString(12, objeto.getNumero());
			prepSt.setDate(13, objeto.getDataNascimento());
			prepSt.setDouble(14, objeto.getRendaMensal());
			
			prepSt.executeUpdate();
			
			connection.commit();
			
			if(objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {
				sql = "update model_login set fotouser = ?, extensaofotouser = ? where id = ?";
				
				prepSt = connection.prepareStatement(sql);
				
				prepSt.setString(1, objeto.getFotouser());
				prepSt.setString(2, objeto.getExtensaofotouser());
				prepSt.setLong(3, objeto.getId());
				
				prepSt.execute();
				
				connection.commit();
			}
			
		}
		
		return this.consultaUsuario(objeto.getLogin(), userLogado);
	}
	
	
	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " order by nome offset " + offset + " limit 5;";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public int totalPaginas(Long userLogado) throws Exception {
		
		String sql = "select count(1) as total from model_login where usuario_id = " + userLogado;
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		Double porpagina = 5.0;
		Double pagina = cadastros / porpagina;
		Double resto = pagina % 2;
		
		if (resto > 0) {
			pagina++;
		}
		
		return pagina.intValue();
	}
	
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " and datanascimento >= ? and datanascimento <= ?";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setDate(1, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
		prepSt.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " limit 5 ";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public int consultaUsuarioListTotalPaginaPaginacao(String nome, Long userLogado) throws Exception{
		
		String sql = "select count(1) as total from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? ";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setString(1, "%" + nome + "%");
		prepSt.setLong(2, userLogado);
		
		ResultSet resultado = prepSt.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		Double porpagina = 5.0;
		Double pagina = cadastros / porpagina;
		Double resto = pagina % 2;
		
		if (resto > 0) {
			pagina++;
		}
		
		return pagina.intValue();
	}
	
	
	public List<ModelLogin> consultaUsuarioListOffset(String nome, Long userLogado, String offset) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "+ offset +" limit 5;";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setString(1, "%" + nome + "%");
		prepSt.setLong(2, userLogado);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5;";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setString(1, "%" + nome + "%");
		prepSt.setLong(2, userLogado);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	
	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE upper(login) = upper('" + login + "') and useradmin is false and usuario_id = " + userLogado + ";";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
	}
	
	
	public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE upper(login) = upper('" + login + "');";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
	}
	
	
	public ModelLogin consultaUsuario(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE upper(login) = upper('" + login + "') and useradmin is false;";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
	}
	
	
	public ModelLogin consultaUsuarioID(Long id) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE id = ? and useradmin is false";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		prepSt.setLong(1, id);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
	}
	
	
	public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE id = ? and useradmin is false and usuario_id = ?;";
		
		PreparedStatement prepSt = connection.prepareStatement(sql);
		prepSt.setLong(1, Long.parseLong(id));
		prepSt.setLong(2, userLogado);
		
		ResultSet resultado = prepSt.executeQuery();
		
		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotouser(resultado.getString("fotouser"));
			modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		return modelLogin;
	}
	
	
	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "');";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		ResultSet resultado = prepSt.executeQuery();
		
		resultado.next();	/* Para ele entrar nos resultados do sql */
		
		return resultado.getBoolean("existe");
	}
	
	
	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login WHERE id = ? and useradmin is false;";
		PreparedStatement prepSt = connection.prepareStatement(sql);
		
		prepSt.setLong(1, Long.parseLong(idUser));
		
		prepSt.executeUpdate();
		
		connection.commit();
	}
	
	
	public List<ModelTelefone> listFone(Long idUserPai) throws Exception{
		
		List<ModelTelefone> retorno = new ArrayList<ModelTelefone>();
		
		String sql = "select * from telefone where usuario_pai_id = ?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		preparedStatement.setLong(1, idUserPai);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while(rs.next()) {
			ModelTelefone modelTelefone = new ModelTelefone();
			
			modelTelefone.setId(rs.getLong("id"));
			modelTelefone.setNumero(rs.getString("numero"));
			modelTelefone.setUsuario_cad_id(this.consultaUsuarioID(rs.getLong("usuario_cad_id")));
			modelTelefone.setUsuario_pai_id(this.consultaUsuarioID(rs.getLong("usuario_pai_id")));
			
			retorno.add(modelTelefone);
		}
		
		return retorno;
	}
	
}
