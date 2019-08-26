package Persistence;

import javax.persistence.*;

@Entity
@Table(name = "recovery")
public class Recovery extends Persister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    Users user;
    @Column(name = "code", nullable = false)
    String code;
    @Column(name = "validity", nullable = false)
    String validity;

    @Override
    public Integer getId() { return this.id; }
    @Override
    public void setId(Integer id) { this.id = id; }

    public Users getUsers() {
        return user;
    }

    public Users setUsers(Users users) {
        this.user = users;
        return this.user;
    }

    public String getCode() {
        return code;
    }

    public Recovery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getValidity() {
        return validity;
    }

    public Recovery setValidity(String validity) {
        this.validity = validity;
        return this;
    }
}
