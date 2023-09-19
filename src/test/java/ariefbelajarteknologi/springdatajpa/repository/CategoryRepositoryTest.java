package ariefbelajarteknologi.springdatajpa.repository;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import org.junit.jupiter.api.*;
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
    @Disabled
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

    @Test
    void queryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("GADGET TERKINI").orElse(null);
        assertNotNull(category);
        assertEquals("GADGET TERKINI", category.getName());

        List<Category> categories = categoryRepository.findAllByNameLike("%GADGET%");
        assertNotNull(category);
        assertEquals("GADGET TERKINI",categories.get(0).getName());
    }

    @Test
    void audit() {
        Category category = new Category();
        category.setName("LAPTOP TERKINI");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }
}