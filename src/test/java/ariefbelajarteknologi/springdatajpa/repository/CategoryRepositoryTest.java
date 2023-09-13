package ariefbelajarteknologi.springdatajpa.repository;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    void insert() {
        Category category = new Category();
        category.setName("GADGET");

        categoryRepository.save(category);

        assertNotNull(category.getId());
    }

    @Test
    @Order(2)
    void update() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        category.setName("GADGET TERKINI");
        categoryRepository.save(category);

        category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);
        assertEquals("GADGET TERKINI", category.getName());
    }

    @Test
    @Order(3)
    void delete() {
        Category category = new Category();
        category.setName("GADGET");

        categoryRepository.save(category);
        assertNotNull(category.getId());

        category = categoryRepository.findById(category.getId()).orElse(null);
        categoryRepository.delete(category);
        category = categoryRepository.findById(category.getId()).orElse(null);
        assertNull(category);
    }
}