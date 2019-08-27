package Persistence;

import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "purchasehistory")
public class PurchaseHistory extends Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private Users users;
    @Column(name = "status")
    private String status;
    @Column(name = "date")
    private Date date;

    @OneToMany(mappedBy = "purchaseHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PurchaseHistoryItem> products;

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId(){ return this.getId();}

    @JSON(serialize = false, deserialize = false)
    public Users getUsers() {
        return users;
    }
    public void setUsers(Users users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public List<Map<String,Object>> getProductList(){
        List<Map<String, Object>> allProducts = new ArrayList<>();
        for(PurchaseHistoryItem item: this.products){
            Map<String, Object> tempMapItem = new HashMap<>();
            tempMapItem.put("id", item.getId());
            tempMapItem.put("quantity", item.getQuantity());
            Map<String, Object> tempProductInner =  new HashMap<>();
            tempProductInner.put("id",item.getProduct().getId());
            tempProductInner.put("value", item.getProduct().getValue());
            tempProductInner.put("authorId",item.getProduct().getAuthorInfo());
            tempProductInner.put("categoryId",item.getProduct().getCategoryInfo());
            tempProductInner.put("sinopse",item.getProduct().getSinopse());
            tempProductInner.put("name",item.getProduct().getName());
            tempMapItem.put("product",tempProductInner);

            allProducts.add(tempMapItem);
        }
        return allProducts;
    }

    @JSON(serialize = false, deserialize = false)
    public List<PurchaseHistoryItem> getProducts() {
        return products;
    }
    public void setProducts(List<PurchaseHistoryItem> products) {
        this.products = products;
    }
    public void addProduct(PurchaseHistoryItem item) {
        this.products.add(item);
    }
}
