package org.guhan;

import javax.management.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Path("Vendor")
public class Vendor {

    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addVendor(UserDTO vendor){
        try{
            String query="insert into vendor values(?,?,?);";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,vendor.getUser_id());
            st.setString(2,vendor.getUser_name());
            st.setLong(3,vendor.getMobile());
            st.executeUpdate();
            return "Vendor added Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }


    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<UserDTO> getAllVendor(){
        try{
            ArrayList<UserDTO> vendors=new ArrayList<>();
            String query="select * from vendor";
            Class.forName(driver);
            Connection con=DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                UserDTO vendor=new UserDTO();
                vendor.setUser_id(rs.getInt("vendor_id"));
                vendor.setUser_name(rs.getString("vendor_name"));
                vendor.setMobile(rs.getLong("mobile"));
                vendors.add(vendor);
            }
            return vendors;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
