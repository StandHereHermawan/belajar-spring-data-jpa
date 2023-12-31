package ariefbelajarteknologi.springdatajpa.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void success() {
        assertThrows(RuntimeException.class,() -> {
            categoryService.create();
        });
    }

    @Test
    @Disabled
    void failed() {
        assertThrows(RuntimeException.class, () -> {
            categoryService.test();
        });
    }

    @Test
    void programmatic() {
        assertThrows(RuntimeException.class, () -> {
            categoryService.createCategoriesError();
        });
    }

    @Test
    void manual() {
        assertThrows(RuntimeException.class, () -> {
            categoryService.manual();
        });
    }
}