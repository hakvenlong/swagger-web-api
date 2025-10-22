package com.setec.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.setec.entities.product;

@Repository
public interface ProductRepos extends JpaRepository<product, Integer> {

	List<product> findByName(String name);

	

}
