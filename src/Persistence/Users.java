package Persistence;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users extends Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "passwd",nullable = false)
    private String passwd;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "level", nullable = false)
    private int level;

    @OneToOne(mappedBy = "user")
    private Recovery recovery;

    @OneToOne(mappedBy = "users")
    private PurchaseHistory purchaseHistory;

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return passwd;
    }
    public void setPassword(String senha) {
        this.passwd = senha;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return name;
    }
    public void setNome(String nome) {
        this.name = nome;
    }

    public int getLevel() { return this.level;}
    public void setLevel(int level){ this.level = level;}

    public Recovery getRecovery() { return recovery; }
    public Recovery setRecovery(Recovery recovery) {
        this.recovery = recovery;
        return this.recovery;
    }

    public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

    public PurchaseHistory setPurchaseHistory(PurchaseHistory purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
        return this.purchaseHistory;
    }

    @Override
    public Integer getId() { return id; }
    @Override
    public void setId(Integer id) { this.id = id; }
}
