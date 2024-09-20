package com.SWP391.KoiManagement;

import com.SWP391.KoiManagement.entity.Category;
import com.SWP391.KoiManagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public void run(String... args) throws Exception {
		if (!categoryRepository.existsById(1)){
			categoryRepository.save(Category.builder()
							.cateId(1)
							.cateName("Koi Health Treatment")
					.build());
		}
		if (!categoryRepository.existsById(2)){
			categoryRepository.save(Category.builder()
					.cateId(2)
					.cateName("Water parameter improvement")
					.build());
		}
	}
}
