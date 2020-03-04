package org.guhan;

public class ReportDTO {
    private int item_id;
    private String item_name;
    private int in_stock;
    private int out_stock;
    private int hand_stock;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(int in_stock) {
        this.in_stock = in_stock;
    }

    public int getOut_stock() {
        return out_stock;
    }

    public void setOut_stock(int out_stock) {
        this.out_stock = out_stock;
    }

    public int getHand_stock() {
        return hand_stock;
    }

    public void setHand_stock(int hand_stock) {
        this.hand_stock = hand_stock;
    }
}
