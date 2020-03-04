package org.guhan;

import java.util.ArrayList;

public class BillDTO {
    private int bill_id;
    private ArrayList<ItemDTO> items;
    private int vendor_id;
    private String date;
    private int total;


    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public ArrayList<ItemDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDTO> items) {
        this.items = items;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
