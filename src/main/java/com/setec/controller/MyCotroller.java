package com.setec.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.setec.dao.PostProductDAO;
import com.setec.dao.PutProductDAO;
import com.setec.entities.product;
import com.setec.repos.ProductRepos;

@RestController
@RequestMapping("/api/product")
public class MyCotroller {

	//http://localhost:8080/swagger-ui/index.html
    private final ProductRepos productRepo;

    // Constructor injection
    public MyCotroller(ProductRepos productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public Object getAll() {
        var products = productRepo.findAll();
        if (products.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Empty product"));
        } else {
            return products;
        }
    }
    
    @GetMapping("/{id}")
    public Object getById(@PathVariable("id") Integer id) {
        var pro = productRepo.findById(id);
        if (pro.isPresent()) {
            return ResponseEntity.ok(pro.get());
        } else {
            return ResponseEntity.status(404).body(
                Map.of("message", "Product with ID = " + id + " not found")
            );
        }
    }
    
    @GetMapping("name/{name}")
    public Object getByName(@PathVariable("name") String name) {
    	List<product> pros = productRepo.findByName(name);
    	if(pros.size()==0) {
    		return ResponseEntity.status(404).body(
                    Map.of("message", "Product with Name = " + name + " not found")
                );
    	}else {
    		return pros;
    	}
    }
    

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object postProduct(@ModelAttribute PostProductDAO product) throws Exception {
        
    	var file = product.getImageFile();
    	String uploadDir = new File("myApp/static").getAbsolutePath();
    	File dir =  new File(uploadDir);
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
    	String extension = Objects.requireNonNull(file.getOriginalFilename());
    	String fileName = UUID.randomUUID()+" "+extension;
    	
    	String filePath = Paths.get(uploadDir, fileName).toString();
    	
    	file.transferTo(new File(filePath));
    	
    	product pro = new product();
    	pro.setName(product.getName());
    	pro.setQty(product.getQty());
    	pro.setPrice(product.getPrice());
    	pro.setImageUrl("/static/" + fileName);

    	productRepo.save(pro);

    	
        return ResponseEntity.status(200).body(pro);
    }
    
    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable("id") Integer id) {
		var p = productRepo.findById(id);
		if (p.isPresent()) {
			new File("myApp/" + p.get().getImageUrl()).delete();
			productRepo.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(Map.of("message", "Product with ID = " + id + " deleted successfully"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Product with ID = " + id + " not found"));
	}
    
    
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object PutProduct(@ModelAttribute PutProductDAO product)throws Exception {
      Integer id = product.getId();
      var p = productRepo.findById(id);
      if(p.isPresent()) {
        var update = p.get();
        update.setName(product.getName());
        update.setPrice(product.getPrice());
        update.setQty(product.getQty());
        
        productRepo.save(update);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("Message","Product update successfully"," product ",update));
      }
      return ResponseEntity.status(404).body(Map.of("Message","Product Id = "+id+" Nien Found "));
      
    }

}