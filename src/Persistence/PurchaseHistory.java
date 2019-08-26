package Persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "purchaseHistory", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<PurchaseHistoryItem> products = new ArrayList<PurchaseHistoryItem>();

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

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
