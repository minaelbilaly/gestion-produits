package ma.tp.gestionproduits;

import ma.tp.gestionproduits.entities.Product;
import ma.tp.gestionproduits.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GestionProduitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionProduitsApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {

            // 1. AJOUTER des produits
            System.out.println("===== 1. AJOUT =====");
            productRepository.save(new Product(null, "Ordinateur", 8000, 10));
            productRepository.save(new Product(null, "Imprimante", 1500, 25));
            productRepository.save(new Product(null, "Souris", 100, 80));
            productRepository.save(Product.builder()
                    .name("Smartphone")
                    .price(5000)
                    .quantity(30)
                    .build());

            // 2. CONSULTER tous les produits
            System.out.println("===== 2. TOUS LES PRODUITS =====");
            List<Product> products = productRepository.findAll();
            products.forEach(p -> System.out.println(p));

            // 3. CONSULTER un produit
            System.out.println("===== 3. UN PRODUIT (id = 1) =====");
            Product product = productRepository.findById(1L).orElse(null);
            if (product != null) {
                System.out.println("Id       : " + product.getId());
                System.out.println("Nom      : " + product.getName());
                System.out.println("Prix     : " + product.getPrice());
                System.out.println("Quantité : " + product.getQuantity());
            }

            // 4. CHERCHER des produits
            System.out.println("===== 4. RECHERCHE (nom contient 'o') =====");
            productRepository.findByNameContains("o").forEach(p -> System.out.println(p));

            System.out.println("===== 4. RECHERCHE (prix > 1000) =====");
            productRepository.findByPriceGreaterThan(1000).forEach(p -> System.out.println(p));

            // 5. METTRE À JOUR un produit
            System.out.println("===== 5. MISE A JOUR (id = 1) =====");
            Product toUpdate = productRepository.findById(1L).orElse(null);
            if (toUpdate != null) {
                toUpdate.setPrice(7500);
                toUpdate.setQuantity(15);
                productRepository.save(toUpdate);   // met à jour car l'id existe déjà
                System.out.println(productRepository.findById(1L).get());
            }

            // 6. SUPPRIMER un produit
            System.out.println("===== 6. SUPPRESSION (id = 2) =====");
            productRepository.deleteById(2L);
            System.out.println("Produits restants :");
            productRepository.findAll().forEach(p -> System.out.println(p));
        };
    }
}