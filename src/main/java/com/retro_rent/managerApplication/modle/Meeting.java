package com.retro_rent.managerApplication.modle;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "hour")
    private Time time;

    @Column(name = "short_description")
    private String short_description;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToMany
    private List<User> users;

    @ManyToOne
    private Renter owner;

    @Column(name = "location")
    private String location;
}
