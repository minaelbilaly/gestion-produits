# TP : Spring Boot, Spring Data JPA, H2 et Lombok

**Gestion de produits : application CRUD avec persistance JPA**

| Informations | Détails |
|---|---|
| **Module** | Java avancé |
| **Réalisé par** | Mina ELBILALY |
| **Date** | Novembre 2026 |
| **Environnement** | Visual Studio Code, JDK 17, Maven, Git / GitHub |
| **Technologies** | Spring Boot 3.5, Spring Data JPA, Hibernate, H2, Lombok |

---

# Sommaire

1. [Objectif du TP](#1-objectif-du-tp)
2. [Outils et technologies utilisés](#2-outils-et-technologies-utilisés)
3. [Création du projet](#3-création-du-projet)
4. [Architecture du projet](#4-architecture-du-projet)
5. [Entité JPA Product](#5-entité-jpa-product)
6. [Configuration de la persistance](#6-configuration-de-la-persistance-applicationproperties)
7. [Interface ProductRepository](#7-interface-productrepository)
8. [Tests des opérations CRUD](#8-tests-des-opérations-crud)
9. [Consultation de la base H2](#9-consultation-de-la-base-h2-bonus)
10. [Difficultés rencontrées](#10-difficultés-rencontrées)
11. [Conclusion](#11-conclusion)

---
# 1. Objectif du TP

L'objectif de ce TP est de développer une application avec **Spring Boot** permettant de gérer des produits dans une base de données.

Cette application permet de réaliser les principales opérations de gestion : ajouter, consulter, rechercher, modifier et supprimer des produits.

À travers ce travail, j'ai appris à utiliser **Spring Data JPA** pour faciliter la communication entre l'application Java et la base de données, sans avoir à écrire manuellement les requêtes SQL.


# 2. Outils et technologies utilisés

Pour réaliser ce TP, j'ai utilisé les outils et technologies suivants :

| Outil / Technologie | Rôle |
|---|---|
| **Visual Studio Code** | Écrire et organiser le code Java. |
| **JDK 17** | Compiler et exécuter le programme Java. |
| **Maven** | Gérer les bibliothèques et les dépendances du projet. |
| **Spring Boot 3.5** | Faciliter la création et le démarrage de l'application. |
| **Spring Data JPA** | Simplifier les opérations sur la base de données. |
| **Hibernate** | Assurer la communication entre les objets Java et les tables SQL. |
| **H2 Database** | Stocker les données dans une base de données en mémoire. |
| **Lombok** | Réduire le code répétitif dans les classes Java. |
| **Git / GitHub** | Enregistrer et partager le code du projet. |

# 3. Création du projet

J'ai créé un projet Maven nommé `gestion-produits`, configuré comme un projet Spring Boot grâce à son fichier `pom.xml`.

J'ai choisi les quatre dépendances nécessaires :

| Dépendance | Utilité |
|---|---|
| `spring-boot-starter-web` | Permet de développer une application Web. |
| `spring-boot-starter-data-jpa` | Permet de manipuler les données avec JPA. |
| `h2` | Fournit la base de données H2. |
| `lombok` | Génère automatiquement certains éléments du code Java. |

### Fichier pom.xml

Le fichier `pom.xml` contient les informations du projet et les dépendances nécessaires à son fonctionnement.

Voici un extrait du fichier :
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>

**Explication :** Maven utilise ce fichier pour télécharger et gérer automatiquement les bibliothèques nécessaires au projet.

<img width="1413" height="1428" alt="pom-xml-extrait" src="https://github.com/user-attachments/assets/1ab0d605-1375-46db-8bc9-5787ae295a0a" />


  Figure 1 : Les quatre dépendances du projet.




---

# 4. Architecture du projet

J'ai organisé le projet en plusieurs fichiers et dossiers afin de faciliter la gestion du code.

Voici sa structure :

```text
gestion-produits/
│
├── pom.xml
├── README.md
│
└── src/
    └── main/
        ├── java/
        │   └── ma/tp/gestionproduits/
        │       ├── GestionProduitsApplication.java
        │       ├── entities/
        │       │   └── Product.java
        │       └── repository/
        │           └── ProductRepository.java
        │
        └── resources/
            └── application.properties
```

### Rôle des fichiers

| Fichier | Rôle |
|---|---|
| `GestionProduitsApplication.java` | Démarre l'application et exécute les tests. |
| `Product.java` | Représente un produit dans la base de données. |
| `ProductRepository.java` | Fournit les méthodes pour manipuler les produits. |
| `application.properties` | Contient la configuration de la base de données. |
| `pom.xml` | Contient les dépendances du projet. |

**Explication :** Cette organisation permet de séparer les différentes responsabilités et de rendre le projet plus facile à comprendre et à maintenir.
<img width="295" height="319" alt="image" src="https://github.com/user-attachments/assets/57262ab3-5c1e-4124-931b-00d157b92365" />

*Figure 2 : Arborescence du projet dans Visual Studio Code.*

---

# 5. Entité JPA Product

### 5.1. Présentation

J'ai créé une classe appelée `Product` qui représente un produit dans mon application.

Grâce à l'annotation `@Entity`, cette classe sera associée à une table dans la base de données.

Elle contient quatre attributs :

| Attribut | Type | Description |
|---|---|---|
| `id` | Long | Identifiant unique du produit. |
| `name` | String | Nom du produit. |
| `price` | double | Prix du produit. |
| `quantity` | int | Quantité disponible. |

### 5.2. Code Product.java

```java
package ma.tp.gestionproduits.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int quantity;
}
```

### 5.3. Explication des annotations

| Annotation | Explication |
|---|---|
| `@Entity` | Indique que la classe représente une entité de la base de données. |
| `@Id` | Définit l'identifiant comme clé primaire. |
| `@GeneratedValue` | Permet de générer automatiquement l'identifiant. |
| `@Data` | Génère les getters, setters et autres méthodes utiles. |
| `@NoArgsConstructor` | Génère un constructeur sans paramètres. |
| `@AllArgsConstructor` | Génère un constructeur avec tous les attributs. |
| `@Builder` | Facilite la création des objets avec le modèle Builder. |

**Explication :** Lombok m'évite d'écrire manuellement les getters, les setters et les constructeurs. Cela rend le code plus court et plus lisible.
<img width="568" height="568" alt="Capture d&#39;écran 2026-11-08 232209" src="https://github.com/user-attachments/assets/5226f290-2fe6-4e4c-8deb-b9894dee7006" />


*Figure 3 : Classe Product avec les annotations JPA et Lombok.*

---

# 6. Configuration de la persistance (application.properties)

### 6.1. Présentation

Après avoir créé l'entité Product, j'ai configuré la connexion à la base de données.

Pour cela, j'ai utilisé le fichier `application.properties`, situé dans le dossier `resources`.

Ce fichier contient les paramètres nécessaires pour connecter Spring Boot à H2 et configurer JPA.

### 6.2. Code application.properties

```properties
spring.application.name=gestion-produits

# Base H2 en mémoire
spring.datasource.url=jdbc:h2:mem:products-db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Console H2
spring.h2.console.enabled=true

# Configuration JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

### 6.3. Explication des propriétés

| Propriété | Explication |
|---|---|
| `spring.datasource.url` | Définit l'adresse de la base de données H2. |
| `driver-class-name` | Indique le pilote utilisé pour communiquer avec H2. |
| `username` et `password` | Définissent les informations de connexion. |
| `spring.h2.console.enabled=true` | Active la console Web de H2. |
| `spring.jpa.hibernate.ddl-auto=update` | Permet à Hibernate de créer ou de mettre à jour les tables. |
| `spring.jpa.show-sql=true` | Affiche les requêtes SQL dans le terminal. |
| `server.port=8080` | Définit le port utilisé par l'application. |

**Explication importante :** H2 fonctionne ici en mémoire. Les données sont conservées pendant l'exécution de l'application, mais elles disparaissent lorsque celle-ci s'arrête.

<img width="483" height="387" alt="image" src="https://github.com/user-attachments/assets/08e5ba37-0710-43a3-8c15-5a8b345059a3" />


*Figure 4 : Fichier application.properties.*

---

# 7. Interface ProductRepository

### 7.1. Présentation

Après la création de l'entité Product, j'ai créé une interface appelée `ProductRepository`.

Cette interface permet de communiquer avec la base de données et d'effectuer les opérations sur les produits.

Elle hérite de `JpaRepository`, fourni par Spring Data JPA.

### 7.2. Code ProductRepository.java

```java
package ma.tp.gestionproduits.repository;

import ma.tp.gestionproduits.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // Recherche par nom
    List<Product> findByNameContains(String keyword);

    // Recherche par prix
    List<Product> findByPriceGreaterThan(double price);
}
```

### 7.3. Explication

Dans `JpaRepository<Product, Long>` :

- `Product` représente l'entité que je veux manipuler.
- `Long` représente le type de son identifiant.

Spring Data JPA fournit automatiquement plusieurs méthodes :

| Méthode | Rôle |
|---|---|
| `save()` | Ajouter ou modifier un produit. |
| `findAll()` | Consulter tous les produits. |
| `findById()` | Consulter un produit par son identifiant. |
| `deleteById()` | Supprimer un produit par son identifiant. |

J'ai également ajouté deux méthodes de recherche :

- `findByNameContains()` : recherche les produits dont le nom contient un mot donné.
- `findByPriceGreaterThan()` : recherche les produits dont le prix est supérieur à une valeur donnée.

**Explication :** Le principal avantage de Spring Data JPA est qu'il génère automatiquement les requêtes nécessaires à partir du nom des méthodes. Je n'ai donc pas besoin d'écrire leur implémentation.

<img width="748" height="389" alt="image" src="https://github.com/user-attachments/assets/29e7a23a-d67e-4409-8c02-74ea90381bb1" />


*Figure 5 : Interface ProductRepository.*

---

# 8. Tests des opérations CRUD

### 8.1. Présentation

Dans cette partie, j'ai testé les différentes opérations de gestion des produits.

Pour cela, j'ai utilisé `CommandLineRunner`, qui permet d'exécuter automatiquement du code après le démarrage de l'application Spring Boot.

Les résultats des opérations sont affichés dans le terminal de Visual Studio Code.

### 8.2. Code GestionProduitsApplication.java

```java
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
        SpringApplication.run(
            GestionProduitsApplication.class, args
        );
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {

            // 1. AJOUTER des produits
            System.out.println("===== 1. AJOUT =====");

            productRepository.save(
                new Product(null, "Ordinateur", 8000, 10)
            );

            productRepository.save(
                new Product(null, "Imprimante", 1500, 25)
            );

            productRepository.save(
                new Product(null, "Souris", 100, 80)
            );

            productRepository.save(
                Product.builder()
                    .name("Smartphone")
                    .price(5000)
                    .quantity(30)
                    .build()
            );

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

            productRepository.findByNameContains("o")
                .forEach(p -> System.out.println(p));

            System.out.println("===== 4. RECHERCHE (prix > 1000) =====");

            productRepository.findByPriceGreaterThan(1000)
                .forEach(p -> System.out.println(p));

            // 5. METTRE À JOUR un produit
            System.out.println("===== 5. MISE A JOUR (id = 1) =====");

            Product toUpdate = productRepository.findById(1L).orElse(null);

            if (toUpdate != null) {
                toUpdate.setPrice(7500);
                toUpdate.setQuantity(15);

                productRepository.save(toUpdate);

                System.out.println(productRepository.findById(1L).get());
            }

            // 6. SUPPRIMER un produit
            System.out.println("===== 6. SUPPRESSION (id = 2) =====");

            productRepository.deleteById(2L);

            System.out.println("Produits restants :");

            productRepository.findAll()
                .forEach(p -> System.out.println(p));
        };
    }
}
```

**Explication :** Cette classe contient les différentes opérations demandées dans le TP. Chaque opération utilise une méthode fournie par `ProductRepository`.

### 8.3. Lancement de l'application

Pour lancer l'application, j'ouvre le terminal de Visual Studio Code dans le dossier du projet et j'exécute la commande suivante :

```bash
mvn spring-boot:run
```

Cette commande permet de compiler et de démarrer l'application Spring Boot.

Lorsque le démarrage réussit, le message `Started GestionProduitsApplication` apparaît dans le terminal.

<img width="896" height="664" alt="image" src="https://github.com/user-attachments/assets/6cfb3e94-0f34-489d-b12b-ba5b22ffe1e9" />


*Figure 6 : Démarrage de l'application Spring Boot.*

### 8.4. Ajout des produits (Create)

Pour ajouter des produits, j'utilise la méthode `save()`.

Dans mon exemple, j'ajoute quatre produits : Ordinateur,  Souris et Smartphone.

L'identifiant est généré automatiquement par la base de données.

**Résultat :** Les trois produits sont enregistrés dans la base H2.

<img width="688" height="452" alt="image" src="https://github.com/user-attachments/assets/13b14683-03b4-4d51-afc6-d4975ea19b21" />


*Figure 7 : Insertion de 3 produits.*

### 8.5. Consultation de tous les produits (Read)

Pour afficher tous les produits, j'utilise la méthode `findAll()`.

Cette méthode récupère la liste complète des produits enregistrés dans la base de données.

**Résultat :** Les quatre produits sont affichés dans le terminal.

<img width="720" height="111" alt="image" src="https://github.com/user-attachments/assets/115e4b5c-6706-4300-b9d4-3c1a701667f1" />


*Figure 8 : Consultation de tous les produits.*

### 8.6. Consultation d'un produit (Read)

Pour consulter un seul produit, j'utilise `findById(1L)`.

Cette méthode permet de rechercher un produit à partir de son identifiant.

Dans mon exemple, je recherche le produit ayant l'identifiant 1.

**Résultat :** Les informations du produit Ordinateur sont affichées : identifiant, nom, prix et quantité.

<img width="856" height="101" alt="image" src="https://github.com/user-attachments/assets/8ec0a317-0b22-4aa2-ab29-c9d3274f33f2" />


*Figure 9 : Consultation du produit n° 1.*

### 8.7. Recherche des produits (Search)

J'ai réalisé deux recherches à l'aide des méthodes personnalisées du Repository.

**Première recherche :**

```java
findByNameContains("o")
```

Cette méthode retourne les produits dont le nom contient la lettre « o ».

**Résultat :** Souris et Smartphone.

**Deuxième recherche :**

```java
findByPriceGreaterThan(1000)
```

Cette méthode retourne les produits dont le prix est supérieur à 1000.

**Résultat :** Ordinateur, Imprimante et Smartphone.

**Explication :** Ces deux méthodes montrent que Spring Data JPA peut créer des requêtes automatiquement à partir du nom des méthodes.

<img width="887" height="179" alt="image" src="https://github.com/user-attachments/assets/4f4d4790-43d6-4e04-853d-a14e197803d5" />


*Figure 10 : Résultats des deux recherches.*

### 8.8. Mise à jour d'un produit (Update)

Pour modifier un produit, je commence par le rechercher avec `findById()`.

Ensuite, je modifie son prix et sa quantité à l'aide des setters, puis j'enregistre les changements avec `save()`.

Dans mon exemple :

- Ancien prix : 8000.
- Nouveau prix : 7500.
- Ancienne quantité : 10.
- Nouvelle quantité : 15.

**Résultat :** Le produit ayant l'identifiant 1 est mis à jour dans la base de données.

<img width="822" height="107" alt="image" src="https://github.com/user-attachments/assets/4d82921a-eabb-412e-b611-93f35b3933ea" />

*Figure 11 : Produit n° 1 après modification.*

### 8.9. Suppression d'un produit (Delete)

Pour supprimer un produit, j'utilise la méthode `deleteById()`.

Dans mon exemple, je supprime le produit ayant l'identifiant 2, qui correspond à Imprimante.

Après la suppression, j'utilise `findAll()` pour afficher les produits restants.

**Résultat :** Le produit Imprimante est supprimé de la base de données.

<img width="934" height="250" alt="image" src="https://github.com/user-attachments/assets/cb82cf29-8371-465f-b690-c2b6d1dae3d8" />


*Figure 12 : Liste des produits après suppression.*

### 8.10. Récapitulatif des opérations

| Opération | Méthode utilisée | Résultat |
|---|---|---|
| Ajout | `save()` | 4 produits ajoutés. |
| Consultation de tous | `findAll()` | Affichage des 4 produits. |
| Consultation d'un produit | `findById()` | Affichage du produit n° 1. |
| Recherche par nom | `findByNameContains()` | Souris et Smartphone. |
| Recherche par prix | `findByPriceGreaterThan()` | Ordinateur, Imprimante et Smartphone. |
| Mise à jour | `save()` | Prix et quantité du produit n° 1 modifiés. |
| Suppression | `deleteById()` | Produit n° 2 supprimé. |

---

# 9. Consultation de la base H2 (Bonus)

En plus des tests effectués dans le terminal, je peux consulter directement les données dans la base H2.

Pour cela, j'ouvre un navigateur et j'accède à l'adresse suivante :

`http://localhost:8081/h2-console`

Je renseigne les informations suivantes :

| Champ | Valeur |
|---|---|
| JDBC URL | `jdbc:h2:mem:products-db` |
| User Name | `sa` |
| Password | Vide |

Après la connexion, j'exécute la requête SQL suivante :

```sql
SELECT * FROM PRODUCT;
```

Cette requête permet d'afficher tous les produits enregistrés dans la table PRODUCT.

**Explication :** La console H2 me permet de vérifier directement que les opérations réalisées dans mon application ont bien modifié les données de la base.

<img width="777" height="402" alt="image" src="https://github.com/user-attachments/assets/1830d139-da83-4b09-8aac-5968a86f3e14" />


*Figure 13 : Contenu de la table PRODUCT dans la console H2.*

---

# 10. Difficultés rencontrées

Pendant la réalisation du TP, j'ai rencontré quelques difficultés techniques.

| Difficulté | Solution |
|---|---|
| Erreur `SpringApplication cannot be resolved` | Le fichier `pom.xml` était absent : ajout du fichier, puis rechargement du projet. |
| Commande `mvn` non reconnue | Maven n'était pas installé : installation et ajout au `PATH`. |
| Conflit entre deux extensions Java dans VS Code | Désactivation de l'extension qui faisait doublon avec *Extension Pack for Java*. |

Ces difficultés m'ont permis de mieux comprendre la configuration de l'environnement Java et la gestion des dépendances avec Maven.

---

# 11. Conclusion

Ce TP m'a permis de découvrir et de mettre en pratique plusieurs technologies utilisées dans le développement d'applications Java avec Spring Boot.

J'ai appris à créer une entité JPA, à configurer une base de données H2 et à utiliser Spring Data JPA pour effectuer les opérations CRUD.

J'ai également compris l'utilité de Lombok pour réduire le code répétitif et faciliter le développement.

Enfin, les différents tests réalisés m'ont permis de vérifier le fonctionnement de l'application.

**En conclusion, Spring Data JPA simplifie la gestion des données grâce à JpaRepository, qui fournit automatiquement les méthodes nécessaires pour communiquer avec la base de données, sans devoir écrire manuellement leur implémentation.**

---

# Dépôt du travail

**Dépôt GitHub :** https://github.com/minaelbilaly/gestion-produits
