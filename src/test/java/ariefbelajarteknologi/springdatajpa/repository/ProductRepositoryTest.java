package ariefbelajarteknologi.springdatajpa.repository;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import ariefbelajarteknologi.springdatajpa.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

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
    void sort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));

        List<Product> products = productRepository.findAllByCategory_Name("GADGET TERKINI", sort);
        assertEquals(2, products.size());
        assertEquals("Xiaomi Redmi Note 10 Pro", products.get(0).getName());
        assertEquals("Xiaomi Redmi Note 10", products.get(1).getName());

    }
}