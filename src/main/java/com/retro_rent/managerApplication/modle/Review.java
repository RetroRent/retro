package com.retro_rent.managerApplication.modle;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @Column(name = "review_rank")
    private int rank;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private User given_by;

    @Column(name = "given_on")
    private Date givenOn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getGiven_by() {
        return given_by;
    }

    public void setGiven_by(User given_by) {
        this.given_by = given_by;
    }

    public Date getGivenOn() {
        return givenOn;
    }

    public void setGivenOn(Date givenOn) {
        this.givenOn = givenOn;
    }
}
