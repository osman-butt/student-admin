package edu.hogwarts.studentadmin.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

@Entity
public class House {

    @Id
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

    public HouseColor getColors() {
        return colors;
    }

    public void setColors(HouseColor colors) {
        this.colors = colors;
    }
}
