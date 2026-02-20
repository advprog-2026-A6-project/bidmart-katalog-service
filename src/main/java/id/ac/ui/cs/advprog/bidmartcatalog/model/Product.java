package id.ac.ui.cs.advprog.bidmartcatalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dum_product")
@Getter @Setter
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    private String productName;
    private int productQuantity;
}