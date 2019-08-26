package Persistence;

import org.apache.struts2.json.annotations.JSON;
import org.apache.struts2.json.annotations.JSONFieldBridge;

import javax.persistence.*;

@Entity
@Table(name = "author")

public class Author extends Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "author")
    private Products products;

    public Products setProducts(Products products) {
        this.products = products;
        return this.products;
    }

    @Override
    public Integer getId() {
        return this.id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}