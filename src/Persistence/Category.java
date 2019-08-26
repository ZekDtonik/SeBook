package Persistence;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category extends Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",unique = true, nullable = false)
    private String name;

    @OneToOne(mappedBy = "category")
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
