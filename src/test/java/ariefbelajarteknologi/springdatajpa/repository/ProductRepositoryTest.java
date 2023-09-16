package ariefbelajarteknologi.springdatajpa.repository;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import ariefbelajarteknologi.springdatajpa.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
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
}