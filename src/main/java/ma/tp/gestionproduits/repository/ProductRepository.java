package ma.tp.gestionproduits.repository;

import ma.tp.gestionproduits.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Produits dont le nom contient un mot-clé
    List<Product> findByNameContains(String keyword);

    // Produits dont le prix est supérieur à une valeur
    List<Product> findByPriceGreaterThan(double price);
}