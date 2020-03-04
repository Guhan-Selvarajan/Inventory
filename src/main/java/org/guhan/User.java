package org.guhan;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Path("Users")
public class User {

    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";


    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUser(@QueryParam("user_id")int user_id){
        try{
            UserDTO users=new UserDTO();
            String query="select * from user where user_id=?;";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,user_id);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                users.setUser_id(user_id);
                users.setUser_name(rs.getString("user_name"));
                users.setMobile(rs.getLong("mobile"));
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<UserDTO> getAllUser(){
        try{
            ArrayList<UserDTO> userList=new ArrayList<>();
            String query="select * from user;";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while (rs.next()){
                UserDTO users=new UserDTO();
                users.setUser_id(rs.getInt("user_id"));
                users.setUser_name(rs.getString("user_name"));
                users.setMobile(rs.getLong("mobile"));
                userList.add(users);
            }
            return userList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
