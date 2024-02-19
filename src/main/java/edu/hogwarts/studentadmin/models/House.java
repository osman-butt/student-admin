package edu.hogwarts.studentadmin.models;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String founder;
    @JsonSerialize
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "primaryColor", column = @Column(name = "primary_color")),
            @AttributeOverride(name = "secondaryColor", column = @Column(name = "secondary_color"))
    })
    private HouseColor colors;

    public House() {}

    public House(String name, String founder,HouseColor colors) {
        this.name = name;
        this.founder = founder;
        this.colors = colors;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String colorsToString() {
        return colors.getPrimaryColor() + ", " +colors.getSecondaryColor();
    }
}
