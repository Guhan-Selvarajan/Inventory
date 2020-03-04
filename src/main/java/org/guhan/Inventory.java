package org.guhan;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;


@Path("Inventory")
public class Inventory {

    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemDTO getItem(@QueryParam("item_id") int item_id) {
        try{
            String query="select * from items where item_id=?;";
            ItemDTO itemDTO =new ItemDTO();
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,item_id);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                itemDTO.setItem_id(item_id);
                itemDTO.setItem_name(rs.getString("item_name"));
                itemDTO.setQty(rs.getInt("stock"));
                itemDTO.setPrice(rs.getInt("price"));
            }
            else{
                itemDTO.setItem_name("Item not found");
            }
            con.close();
            return itemDTO;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ItemDTO> getAllItem() {
        try{
            String query="select * from items;";
            ArrayList<ItemDTO> items=new ArrayList<>();
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                ItemDTO itemDTO =new ItemDTO();
                itemDTO.setItem_id(rs.getInt("item_id"));
                itemDTO.setItem_name(rs.getString("item_name"));
                itemDTO.setQty(rs.getInt("stock"));
                itemDTO.setPrice(rs.getInt("price"));
                items.add(itemDTO);
            }
            con.close();
            return items;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String insertItem(ItemDTO itemDTO)
    {
        int item_id= itemDTO.getItem_id();
        String item_name= itemDTO.getItem_name();
        int stock= itemDTO.getQty();
        int price= itemDTO.getPrice();
        try{
            String query="insert into items values(?,?,?,?);";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,item_id);
            st.setString(2,item_name);
            st.setInt(3,stock);
            st.setInt(4,price);
            st.executeUpdate();
            con.close();
            return "Insert Successful";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    @PUT
    @Path("edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String editItem(ItemDTO itemDTO){
        int item_id= itemDTO.getItem_id();
        String item_name= itemDTO.getItem_name();
        int stock= itemDTO.getQty();
        int price= itemDTO.getPrice();
        try{
            String query="select * from items where item_id=?;";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,item_id);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                String query2="update items set item_name=?, stock=?, price=? where item_id=?;";
                PreparedStatement st2=con.prepareStatement(query2);
                st2.setString(1,item_name);
                st2.setInt(2,stock);
                st2.setInt(3,price);
                st2.setInt(4,item_id);
                st2.executeUpdate();
                con.close();
                return "Update Succesful";
            }
            else{
                con.close();
                return "Item not found";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteItem(@QueryParam("item_id")int item_id){
        try{
            String query="delete from items where item_id=?;";
            Class.forName(driver);
            Connection con=DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,item_id);
            st.executeUpdate();
            return "Delete Successful";
        }catch (Exception e){
            e.printStackTrace();
            return "Cannot Delete";
        }
    }
}
