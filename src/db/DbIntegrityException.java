package db;

public class DbIntegrityException extends RuntimeException{
	// Exceção criada para lidar com problemas de Integridade Referencial 
	//(deletar um departamento cujo o id está ligado a um vendedor, vendedor ñ estará associado a nenhum departamento)
	private static final long serialVersionUID = 1L;
	
	public DbIntegrityException(String msg) {
		super(msg);
	}
}
