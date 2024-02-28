package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) { // Injeção de Dependência (objeto conn: criado e fechado em Program)
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller seller) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("Insert into seller \r\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)\r\n"
					+ "Values\r\n"
					+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDp().getId());
			
			int rowsAffected = st.executeUpdate();	
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
					
				} 
				DB.fecharResultSet(rs);
				System.out.println("Registro inserido com sucesso!");
			} else {
				throw new DbException("Erro inesperado, nenhuma linha afetada");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
		}
		
	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select s.*, d.Name \r\n"
					+ "from seller s \r\n"
					+ "Inner join department d \r\n"
					+ "on s.DepartmentId = d.Id\r\n"
					+ "Where s.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			// Se houver resultado, cria instâncias do Seller e Department específicos
			if (rs.next()) {
				Department dep = instanciarDepartment(rs);
				Seller seller = intanciarSeller(rs, dep);
				
				return seller; // Se houver resultados, retorna o Seller Específico
			}
			
			return null; // Se não houver resultados, retorna null
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
		
	}
	
	// Método de instanciar um Seller a partir de um ResultSet
	private Seller intanciarSeller(ResultSet rs, Department dep) throws SQLException{
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDp(dep);
		return seller;
	}

	// Método de instanciação de um Department a partir de um ResultSet
	private Department instanciarDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("Select s.*, d.name \r\n"
					+ "from seller s \r\n"
					+ "Inner join department d\r\n"
					+ "on s.DepartmentId = d.Id\r\n"
					+ "Order By s.name;");
			
			rs = st.executeQuery();
			// Enquanto houver resultado, cria instâncias do Seller e Department específicos
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // para ñ repetir Department
			while (rs.next()) {
				// Verifica no map se há um department com um id específico, se ñ existir retorna null
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				if (dep == null) { // Se não houver o departamento, inclua um no map
					dep = instanciarDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = intanciarSeller(rs, dep);
				list.add(seller);				
			}
			
			return list; // Se houver resultados, retorna a Lista de Sellers
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("Select s.*, d.name \r\n"
					+ "from seller s \r\n"
					+ "Inner join department d\r\n"
					+ "on s.DepartmentId = d.Id\r\n"
					+ "where s.DepartmentId = ?\r\n"
					+ "Order By s.name;");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			// Enquanto houver resultado, cria instâncias do Seller e Department específicos
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // para ñ repetir Department
			while (rs.next()) {
				// Verifica no map se há um department com um id específico, se ñ existir retorna null
				Department dep = map.get(rs.getInt("DepartmentId")); 
				
				if (dep == null) { // Se não houver o departamento, inclua um no map
					dep = instanciarDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = intanciarSeller(rs, dep);
				list.add(seller);				
			}
			
			return list; // Se houver resultados, retorna a Lista de Sellers
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

}
