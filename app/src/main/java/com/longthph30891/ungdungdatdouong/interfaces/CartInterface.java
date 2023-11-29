package com.longthph30891.ungdungdatdouong.interfaces;

public interface CartInterface {

    void onIncreaseClick(int position);

    void onDecreaseClick(int position);

    void checkItem(int position, boolean isChecked);

}
