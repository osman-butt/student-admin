package edu.hogwarts.studentadmin.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class HouseColor {
    @Enumerated(EnumType.STRING)
    private ColorType primaryColor;
    @Enumerated(EnumType.STRING)
    private ColorType secondaryColor;

    public HouseColor(ColorType primaryColor, ColorType secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public HouseColor() {}

    public ColorType getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(ColorType primaryColor) {
        this.primaryColor = primaryColor;
    }

    public ColorType getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(ColorType secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
}
