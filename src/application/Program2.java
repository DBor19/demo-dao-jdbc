package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
	public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);	
		
	System.out.println("=== Teste 1: Department findById ===");	
	DepartmentDao depDao = DaoFactory.createDepartmentDao(); // Objeto depDao p acessar os métodos
	Department dep = depDao.findById(2); // Seleciona a id e retorna Department
	System.out.println(dep);
	
	
	System.out.println("\n=== Teste 2: Department findAll ===");
	List<Department> list = depDao.findAll();
	for (Department d : list) {
		System.out.println(d);
	}
	
	System.out.println("\n=== Teste 3: Department insert ===");
	Department newDep = new Department(null, "Movies"); // null vai ser preenchido pelo método insert() 
	depDao.insert(newDep);
	System.out.println("Chave gerada: " + newDep.getId());
	
	System.out.println("\n=== Teste 4: Department update ===");
	dep = depDao.findById(6);
	dep.setName("Toys");
	depDao.update(dep);
	System.out.println("Registro atualizado.");
	
	System.out.println("\n=== Teste 5: Department delete ===");
	System.out.print("Informe o ID para exclusão: ");
	int id = sc.nextInt();
	depDao.deleteById(id);
	System.out.println("Registro deletado.");
	
	sc.close();
	}
}
