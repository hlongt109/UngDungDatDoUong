package com.longthph30891.ungdungdatdouong.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String typeId, typeName, typeImage;

    public Category() {

    }

    public Category(String typeId, String typeName, String typeImage) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeImage = typeImage;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }
}
