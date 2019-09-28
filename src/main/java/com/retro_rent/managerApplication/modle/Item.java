package com.retro_rent.managerApplication.modle;
<<<<<<< HEAD

=======
#test
>>>>>>> 57be3bc097a2076053927663305dd9ab6c4d29f7
import javax.persistence.*;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "labels")
    private String labels;

    @Column(name = "description")
    private String description;

    @Column(name = "year_of_production")
    private Year year_of_production;

    @Column(name = "price_per_day")
    private double pricePerDay;

    @Column(name = "currency")
    private String currency;

    @Column(name = "available_day")
    private String available_day;

    @ManyToOne
    private ItemCategory itemCategory;

    @OneToMany
    private List<OrderedItem> orderedItems;

    @ManyToOne
    private Renter owner;

    @OneToMany
    private List<ItemReview> itemReviews;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAvailable_day() {
        return available_day;
    }

    public void setAvailable_day(String available_day) {
        this.available_day = available_day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public List<ItemReview> getItemReviews() {
        return itemReviews;
    }

    public void setItemReviews(List<ItemReview> itemReviews) {
        this.itemReviews = itemReviews;
    }

    public long getId() {
        return id;
    }

    public Renter getOwner() {
        return owner;
    }

    public void setOwner(Renter owner) {
        this.owner = owner;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Year getYear_of_production() {
        return year_of_production;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setYear_of_production(Year year_of_production) {
        this.year_of_production = year_of_production;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Item)) {
            return false;
        }

        Item item = (Item)o;

        if (this.getId() == item.getId()) {
            return true;
        }

        return false;
    }
}
