package org.guhan;

import java.util.ArrayList;

public class InvoiceDTO {
    private int invoice_id;
    private ArrayList<ItemDTO> itemDTOS;
    private int user_id;
    private String date;
    private int total;






    public ArrayList<ItemDTO> getItemDTOS() {
        return itemDTOS;
    }

    public void setItemDTOS(ArrayList<ItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "invoice_id=" + invoice_id +
                ", user_id=" + user_id +
                ", date=" + date +
                ", total=" + total +
                '}';
    }
}
