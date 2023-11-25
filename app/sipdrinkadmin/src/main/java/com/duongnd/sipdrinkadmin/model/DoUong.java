package com.duongnd.sipdrinkadmin.model;

import java.util.HashMap;

public class DoUong {
    private String idDoUong;
    private String maLoai;
    private String tenDoUong;
    private double gia;
    private String trangThai;
    private String image;
    private String mota;

    public String getIdDoUong() {
        return idDoUong;
    }

    public DoUong setIdDoUong(String idDoUong) {
        this.idDoUong = idDoUong;
        return this;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public DoUong setMaLoai(String maLoai) {
        this.maLoai = maLoai;
        return this;
    }

    public String getTenDoUong() {
        return tenDoUong;
    }

    public DoUong setTenDoUong(String tenDoUong) {
        this.tenDoUong = tenDoUong;
        return this;
    }

    public double getGia() {
        return gia;
    }

    public DoUong setGia(double gia) {
        this.gia = gia;
        return this;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public DoUong setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        return this;
    }

    public String getImage() {
        return image;
    }

    public DoUong setImage(String image) {
        this.image = image;
        return this;
    }

    public String getMota() {
        return mota;
    }

    public DoUong setMota(String mota) {
        this.mota = mota;
        return this;
    }

    public DoUong() {
    }

    public DoUong(String idDoUong, String maLoai, String tenDoUong, double gia, String trangThai, String image,String mota) {
        this.idDoUong = idDoUong;
        this.maLoai = maLoai;
        this.tenDoUong = tenDoUong;
        this.gia = gia;
        this.trangThai = trangThai;
        this.image = image;
        this.mota =mota;
    }
    public HashMap<String,Object> convertHashMap(){
        HashMap<String,Object> DoUong = new HashMap<>();
        DoUong.put("idDoUong", idDoUong);
        DoUong.put("maLoai",maLoai);
        DoUong.put("tenDoUong",tenDoUong);
        DoUong.put("gia",gia);
        DoUong.put("trangThai",trangThai);
        DoUong.put("image",image);
        DoUong.put("mota",mota);
        return DoUong;
    }
}
