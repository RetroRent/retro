package com.retro_rent.managerApplication.modle;

import javax.persistence.*;

@Entity
@Table(name = "item_category")
public class ItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    private CategoryType fCategory;
    @ManyToOne
    private CategoryType sCategory;
    @ManyToOne
    private CategoryType tCategory;

    public void setfCategory(CategoryType fCategory) {
        this.fCategory = fCategory;
    }

    public void setsCategory(CategoryType sCategory) {
        this.sCategory = sCategory;
    }

    public void settCategory(CategoryType tCategory) {
        this.tCategory = tCategory;
    }

    public void setCategory(int index, CategoryType categoryType) {
        switch (index) {
            case 1:
                setfCategory(categoryType);
                break;
            case 2:
                setsCategory(categoryType);
                break;
            case 3:
                settCategory(categoryType);
                break;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CategoryType getfCategory() {
        return fCategory;
    }

    public CategoryType getsCategory() {
        return sCategory;
    }

    public CategoryType gettCategory() {
        return tCategory;
    }
}
