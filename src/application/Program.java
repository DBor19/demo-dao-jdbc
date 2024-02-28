package application;



import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
			
		// Cria uma instância de SellerDao sem mostrar sua implementação
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("===Teste 1: seller findById===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
	}

}
