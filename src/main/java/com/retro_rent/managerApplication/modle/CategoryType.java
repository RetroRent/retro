package com.retro_rent.managerApplication.modle;


import javax.persistence.*;

@Entity
@Table(name = "category_type")
public class CategoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_type")
    private int type;

    public void setName(String category) {
        this.name = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
