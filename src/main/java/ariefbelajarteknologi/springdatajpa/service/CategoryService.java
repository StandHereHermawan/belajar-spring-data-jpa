package ariefbelajarteknologi.springdatajpa.service;

import ariefbelajarteknologi.springdatajpa.entity.Category;
import ariefbelajarteknologi.springdatajpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void create(){
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category "+i);
            categoryRepository.save(category);
        }
        throw new RuntimeException("Ups Rollback please");
    }

    public void test(){
        create();
    }
}
