package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

// Responsável por Instanciar os DAOs
public class DaoFactory {
	// Forma de Esconder a implementação
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.estabelecerConexao());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC();
	}
	
}
