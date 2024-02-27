package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	
	// Método 1: Conexão ao BD (Fábrica de Conexão)
	public static Connection estabelecerConexao() {
		if (conn == null) {
			try {
				Properties props = carregarPropriedades(); 
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		
		return conn;
	}
	

	// Método 2: Ler as propriedades de db.properties
	private static Properties carregarPropriedades() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
			
		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	// Métodos 3: Fechar Conexão
	public static void fecharConexao() {
		if (conn != null) {
			try {
				conn.close();
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	// Método 4: Fechar Objetos Statement (gera exceção, se fechar no programa vai ficar verboso)
	public static void fecharStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
				
			} catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	// Método 5: Fechar Objetos ResultSet (gera exceção, se fechar no programa vai ficar verboso)
	public static void fecharResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
				
			} catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
