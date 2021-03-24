package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Meal.
 */
@Entity
@Table(name = "meal")
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "quantity")
    private Float quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = "meals", allowSetters = true)
    private Food food;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Meal date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getQuantity() {
        return quantity;
    }

    public Meal quantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Food getFood() {
        return food;
    }

    public Meal food(Food food) {
        this.food = food;
        return this;
    }

    public void setFood(Food food) {
        this.food = food;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meal)) {
            return false;
        }
        return id != null && id.equals(((Meal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meal{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
