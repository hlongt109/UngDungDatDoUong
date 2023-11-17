package com.duongnd.sipdrinkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class LoaiDoUong{
    private String typeId;
    private String typeName;
    private String typeImage;

    public LoaiDoUong(String typeId, String typeName, String typeImage) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeImage = typeImage;
    }

    public LoaiDoUong() {
    }

    public String getTypeId() {
        return typeId;
    }

    public LoaiDoUong setTypeId(String typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public LoaiDoUong setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public LoaiDoUong setTypeImage(String typeImage) {
        this.typeImage = typeImage;
        return this;
    }
    public HashMap<String,Object> converHashMap(){
        HashMap<String,Object> LoaiDoUong = new HashMap<>();
        LoaiDoUong.put("Id", typeId);
        LoaiDoUong.put("Name",typeName);
        LoaiDoUong.put("Image",typeImage);
        return LoaiDoUong;
    }
}
