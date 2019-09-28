package com.retro_rent.managerApplication.modle;

import javax.persistence.*;

@Entity
@Table(name = "item_review")
public class ItemReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    private Review review;

    @ManyToOne
    private Item item;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
