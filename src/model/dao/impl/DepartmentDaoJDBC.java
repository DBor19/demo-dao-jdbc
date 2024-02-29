package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	// Cria-se um objeto Department (com id = null e o resto preenchido) no Program e aplica o insert
	public void insert(Department dep) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into department \r\n"
					+ "(Name)\r\n"
					+ "Values (?); ", Statement.RETURN_GENERATED_KEYS);
			
			// Substitui os placeholders pelo que foi passado no construtor do objeto seller em Program
			st.setString(1, dep.getName());
			
			int linhasAfetadas = st.executeUpdate(); // retorna nº de linhas afetadas
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys(); // obtém ResultSet das chaves autocincrementadas geradas
				System.out.println("Linhas afetadas: " + linhasAfetadas);
				
				if(rs.next()) {
					dep.setId(rs.getInt(1)); // Atribui à Id de seler o valor da 1ª linha da coluna 1 das chaves geradas
				}
				DB.fecharResultSet(rs);
			} else {
				throw new DbException("Erro: nenhuma linha afetada.");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			
		}
		
	}

	@Override
	public void update(Department dep) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("Update department \r\n"
					+ "Set name = ?\r\n"
					+ "Where id = ?;");
			
			st.setString(1, dep.getName());
			st.setInt(2, dep.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("Delete from department\r\n"
					+ "where id = ?;");
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas < 0) {
				throw new DbException("ID não encontrado.");
			}
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * \r\n"
					+ "from department\r\n"
					+ "where id = ?;");
			
			st.setInt(1, id); // Subsritui o placeholder pelo argumento passado p id
			
			rs = st.executeQuery(); // Executa a query e retorna um Resultset
			
			if(rs.next()) {
				Department dep = instanciarDepartment(rs);
				return dep;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	private Department instanciarDepartment(ResultSet rs) throws SQLException { // Já está tratado nos métodos q o chamam
		Department dep = new Department(); // Mapeamento Objeto-Relacional (instanciando um Objeto da query
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("Select * from Department;");
			
			List<Department> list = new ArrayList<>();
			while (rs.next()) {
				Department dep = instanciarDepartment(rs);
				list.add(dep);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

}
