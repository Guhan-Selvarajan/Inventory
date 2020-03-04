package org.guhan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ItemDTO {
    private int item_id;
    private String item_name;
    private int qty;
    private int price;




    public boolean checkStock(int item_id, int qty){
        String driver="com.mysql.cj.jdbc.Driver";
        String host="jdbc:mysql://localhost:3306/report";
        String user="root";
        String pass="toor123$";
        try{
            String query="select stock from items where item_id=?;";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,item_id);
            ResultSet rs=st.executeQuery();
            if(rs.next())
            {
                if(rs.getInt("stock")>=qty){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }




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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
