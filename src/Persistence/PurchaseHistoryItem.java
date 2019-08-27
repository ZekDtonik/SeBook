package Persistence;

import javax.persistence.*;

@Entity
@Table(name = "purchasehistoryitem")
public class PurchaseHistoryItem extends Persister{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product",referencedColumnName = "id")
    private Products product;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "history", nullable = false)
    private PurchaseHistory purchaseHistory;
    public PurchaseHistory getPurchaseHistory() { return this.purchaseHistory; }
    public void setPurchaseHistory(PurchaseHistory product) { this.purchaseHistory = product; }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Products getProduct() {
        return product;
    }
    public void setProduct(Products products) {
        this.product = products;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
