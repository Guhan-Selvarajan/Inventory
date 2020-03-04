package org.guhan;


import jdk.nashorn.internal.objects.annotations.Getter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;

@Path("Bill")
public class Bill {

    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";


    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<InvoiceDTO> getAllBills(){
        try{
            String query="select bill_id from bill;";
            ArrayList<InvoiceDTO> bills=new ArrayList<>();
            Class.forName(driver);
            Connection con = DriverManager.getConnection(host, user, pass);
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                InvoiceDTO bill=new InvoiceDTO();
                int bill_id=rs.getInt("bill_id");
                ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
                String query2 = "select * from bill inner join stocks on bill.bill_id = stocks.bill_id where bill.bill_id=?;";
                PreparedStatement st2 = con.prepareStatement(query2);
                st2.setInt(1, bill_id);
                ResultSet rs2 = st2.executeQuery();
                while(rs2.next()) {
                    bill.setInvoice_id(bill_id);
                    bill.setUser_id(rs2.getInt("bill_id"));
                    bill.setDate(rs2.getString("date"));
                    bill.setTotal(rs2.getInt("total"));
                    ItemDTO itemDTO =new ItemDTO();
                    itemDTO.setItem_id(rs2.getInt("item_id"));
                    itemDTO.setQty(rs2.getInt("qty"));
                    itemDTO.setPrice(rs2.getInt("price"));

                    itemDTOS.add(itemDTO);
                }
                bill.setItemDTOS(itemDTOS);
                bills.add(bill);
            }
            return bills;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createBill(InvoiceDTO bill){
        try{
            String query="insert into bill values(?,?,?,?);";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            con.setAutoCommit(false);
            Savepoint save1=con.setSavepoint("save1");
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,bill.getInvoice_id());
            st.setInt(2,bill.getUser_id());
            st.setString(3,bill.getDate());
            st.setInt(4,bill.getTotal());
            st.executeUpdate();
            for(ItemDTO itemDTO : bill.getItemDTOS()){
                String query2 = "insert into stocks values(?,?,?,?);";
                PreparedStatement st2 = con.prepareStatement(query2);
                st2.setInt(1, bill.getInvoice_id());
                st2.setInt(2, itemDTO.getItem_id());
                st2.setInt(3, itemDTO.getQty());
                st2.setInt(4, itemDTO.getPrice());
                String query3="update items set stock=stock+? where item_id=?;";
                PreparedStatement st3=con.prepareStatement(query3);
                st3.setInt(1, itemDTO.getQty());
                st3.setInt(2, itemDTO.getItem_id());
                st3.executeUpdate();
                st2.executeUpdate();
            }
            con.commit();
            return "Bill added succesfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
}
