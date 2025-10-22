package com.setec.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Entity
@Table(name = "tbl_product")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private double price;
    private int qty;
    private String imageUrl;

    public double getAmount() {
        return price * qty;
    }

    // Build a full URL for the image (e.g., http://localhost:8080/static/image.jpg)
    public String getFullImageUrl() {
        if (imageUrl == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(imageUrl)
                .toUriString();
    }
}
