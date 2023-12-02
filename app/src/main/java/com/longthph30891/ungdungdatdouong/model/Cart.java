package com.longthph30891.ungdungdatdouong.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Cart implements Parcelable {
    private String idGioHang, idDoUong, idKhachHang, productName, productImage;
    private int soLuong;
    private double productPrice;

    private boolean isChecked;

    public Cart() {
    }


    public Cart(String idGioHang, String idDoUong, String idKhachHang, String productName, String productImage, int soLuong, double productPrice, boolean isChecked) {
        this.idGioHang = idGioHang;
        this.idDoUong = idDoUong;
        this.idKhachHang = idKhachHang;
        this.productName = productName;
        this.productImage = productImage;
        this.soLuong = soLuong;
        this.productPrice = productPrice;
        this.isChecked = isChecked;
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    protected Cart(Parcel in) {
        idGioHang = in.readString();
        idDoUong = in.readString();
        idKhachHang = in.readString();
        productName = in.readString();
        productImage = in.readString();
        soLuong = in.readInt();
        productPrice = in.readDouble();
        isChecked = in.readByte() != 0;
    }

    public String getIdGioHang() {
        return idGioHang;
    }

    public void setIdGioHang(String idGioHang) {
        this.idGioHang = idGioHang;
    }

    public String getIdDoUong() {
        return idDoUong;
    }

    public void setIdDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idGioHang);
        dest.writeString(idDoUong);
        dest.writeString(idKhachHang);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeInt(soLuong);
        dest.writeDouble(productPrice);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idGioHang='" + idGioHang + '\'' +
                ", idDoUong='" + idDoUong + '\'' +
                ", idKhachHang='" + idKhachHang + '\'' +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", soLuong=" + soLuong +
                ", productPrice=" + productPrice +
                ", isChecked=" + isChecked +
                '}';
    }
}
