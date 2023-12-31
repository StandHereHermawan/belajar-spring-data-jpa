package ariefbelajarteknologi.springdatajpa.repository;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import ariefbelajarteknologi.springdatajpa.entity.Product;
import ariefbelajarteknologi.springdatajpa.model.ProductPrice;
import ariefbelajarteknologi.springdatajpa.model.SimpleProduct;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionOperations transactionOperations;

    @Test
    void createProducts() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Xiaomi Redmi Note 10");
            product.setPrice(3_499_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("Xiaomi Redmi Note 10 Pro");
            product.setPrice(4_299_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Test
    void findByCategoryName() {
        List<Product> products = productRepository.findAllByCategory_Name("GADGET TERKINI");
        assertEquals(2, products.size());
        assertEquals("Xiaomi Redmi Note 10", products.get(0).getName());
        assertEquals("Xiaomi Redmi Note 10 Pro", products.get(1).getName());
    }

    @Test
    @Disabled
    void sort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));

        List<Product> products = productRepository.findAllByCategory_Name("GADGET TERKINI", sort);
        assertEquals(2, products.size());
        assertEquals("Xiaomi Redmi Note 10 Pro", products.get(0).getName());
        assertEquals("Xiaomi Redmi Note 10", products.get(1).getName());

    }

    @Test
    @Disabled
    void pageable() {
        // page 0
        Pageable pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findAllByCategory_Name("GADGET TERKINI", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Xiaomi Redmi Note 10", products.getContent().get(0).getName());

        // page 1
        pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("GADGET TERKINI", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Xiaomi Redmi Note 10 Pro", products.getContent().get(0).getName());
    }

    @Test
    @Disabled
    void count() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_name("GADGET TERKINI");
        assertEquals(2L,count);

        count = productRepository.countByCategory_name("GADGET GAK ADA");
        assertEquals(0L, count);
    }

    @Test
    void exist() {
        boolean exists = productRepository.existsByName("Xiaomi Redmi Note 10");
        assertTrue(exists);

        exists = productRepository.existsByName("iPhone HDC");
        assertFalse(exists);
    }

    @Test
    void deleteProgrammaticTransactions() {
        transactionOperations.executeWithoutResult(transactionStatus -> { // transaksi 1
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Itel S23");
            product.setPrice(1_599_000L);
            product.setCategory(category);
            productRepository.save(product); // transaksi 1

            int delete = productRepository.deleteByName("Itel S23"); // transaksi 1
            assertEquals(1, delete);

            delete = productRepository.deleteByName("Itel S23"); // transaksi 1
            assertEquals(0, delete);
        });
    }

    @Test
    void deleteDeclarativeTransactions() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("Itel S23");
        product.setPrice(1_599_000L);
        product.setCategory(category);
        productRepository.save(product); // transaksi 1

        int delete = productRepository.deleteByName("Itel S23"); // transaksi 2
        assertEquals(1, delete);

        delete = productRepository.deleteByName("Itel S23"); // transaksi 3
        assertEquals(0, delete);
    }

    @Test
    void namedQuery() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Product> products = productRepository.searchProductUsingName("Xiaomi Redmi Note 10 Pro", pageable);
        assertEquals(1, products.size());
        assertEquals("Xiaomi Redmi Note 10 Pro", products.get(0).getName());
    }

    @Test
    void searchProducts() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));

        Page<Product> products = productRepository.searchProduct("%Xiaomi%", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());

        products = productRepository.searchProduct("%GADGET%", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());
    }

    @Test
    void modifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            int totalAffected = productRepository.deleteProductUsingName("GAK ADA");
            assertEquals(0, totalAffected);

            totalAffected = productRepository.updateProductPriceToZero(1L);
            assertEquals(1, totalAffected);

            Product product = productRepository.findById(1L).orElse(null);
            assertNotNull(product);
            assertEquals(0L, product.getPrice());
        });
    }

    @Test
    void stream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Stream<Product> stream = productRepository.streamAllByCategory(category);
            stream.forEach(product -> System.out.println(product.getId() + " : " + product.getName()));

        });
    }

    @Test
    void slice() {
        Pageable firstPage = PageRequest.of(0, 1);

        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, firstPage);
        //  tampilkan konten produk
        while (slice.hasNext()) {
            slice = productRepository.findAllByCategory(category, slice.nextPageable());
            //  tampilkan konten produk
        }
    }

    @Test
    void lock1() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            try {
                Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
                assertNotNull(product);
                product.setPrice(3_199_000L);

                Thread.sleep(20_000L);
                productRepository.save(product);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Test
    void lock2() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
            assertNotNull(product);
            product.setPrice(3_499_000L);
            productRepository.save(product);
        });
    }

    @Test
    void specification() {
        Specification<Product> specification = (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("name"), "Xiaomi Redmi Note 10"),
                            criteriaBuilder.equal(root.get("name"), "Xiaomi Redmi Note 10 Pro")
                    )
            ).getRestriction();
        };

        List<Product> products = productRepository.findAll(specification);
        assertEquals(2, products.size());
    }

    @Test
    void projection() {
        List<SimpleProduct> simpleProducts = productRepository.findAllByNameLike("%Xiaomi%", SimpleProduct.class);
        assertEquals(2, simpleProducts.size());

        List<ProductPrice> productPrices = productRepository.findAllByNameLike("%Xiaomi%", ProductPrice.class);
        assertEquals(2,productPrices.size());
    }
}