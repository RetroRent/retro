package com.retro_rent.managerApplication.modle;


import javax.persistence.*;
import java.util.List;

/**
 * Represents an User for this web application.
 */
@Entity
@Table(name = "renter")
public class Renter {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Meeting> meetings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

} // class User