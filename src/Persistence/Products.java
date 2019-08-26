package Persistence;

import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "products")
public class Products extends  Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "sinopse")
    private String sinopse;
    @Column(name = "value")
    private Float value;
    @Column(name = "image")
    private String image;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private Author author;

    @JoinColumn(name = "category_id",referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Category category;


    @OneToOne(mappedBy = "product")
    private PurchaseHistoryItem purchaseHistoryItem;

    public PurchaseHistoryItem getPurchaseHistoryItem() {
        return this.purchaseHistoryItem;
    }

    public PurchaseHistoryItem setPurchaseHistoryItem(PurchaseHistoryItem purchaseHistoryItem) {
        this.purchaseHistoryItem = purchaseHistoryItem;
        return this.purchaseHistoryItem;
    }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSinopse() {
        return sinopse;
    }
    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Float getValue() {
        return value;
    }
    public void setValue(Float value) {
        this.value = value;
    }

    @JSON(serialize = false,deserialize = false)
    public Author getAuthor() { return this.author; }
    public void setAuthor(Author value){ this.author = value; }

    public Map<String, Object> getAuthorInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id",this.getAuthor().getId());
        map.put("name",this.getAuthor().getName());
        return map;
    }

    @JSON(serialize = false,deserialize = false)
    public Category getCategory() { return this.category; }
    public void setCategory(Category value){ this.category = value; }

    public Map<String, Object> getCategoryInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id",this.getCategory().getId());
        map.put("name",this.getCategory().getName());
        return map;
    }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }


}
