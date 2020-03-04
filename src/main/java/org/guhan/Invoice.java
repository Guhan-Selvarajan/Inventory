package org.guhan;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;


@Path("Invoice")
public class Invoice {



    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";

    @GET
    @Path("get-by-invoice")
    @Produces(MediaType.APPLICATION_JSON)
    public InvoiceDTO getInvoice(@QueryParam("invoice_id")int invoice_id) {
        try {
            InvoiceDTO invoice = new InvoiceDTO();
            ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
            String query = "select * from invoice inner join orders on invoice.invoice_id = orders.invoice_id where invoice.invoice_id=?;";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(host, user, pass);
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, invoice_id);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                invoice.setInvoice_id(invoice_id);
                invoice.setUser_id(rs.getInt("user_id"));
                invoice.setDate(rs.getString("date"));
                invoice.setTotal(rs.getInt("total"));
                ItemDTO itemDTO =new ItemDTO();
                itemDTO.setItem_id(rs.getInt("item_id"));
                itemDTO.setQty(rs.getInt("qty"));
                itemDTO.setPrice(rs.getInt("price"));

                itemDTOS.add(itemDTO);
            }
            invoice.setItemDTOS(itemDTOS);
            return invoice;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




    @GET
    @Path("get-by-user")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<InvoiceDTO> getInvoiceUser(@QueryParam("user_id")int user_id) {
        try{
            String query="select invoice_id from invoice where user_id=?;";
            ArrayList<InvoiceDTO> invoices=new ArrayList<>();
            Class.forName(driver);
            Connection con = DriverManager.getConnection(host, user, pass);
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1,user_id);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                InvoiceDTO invoice=new InvoiceDTO();
                int invoice_id=rs.getInt("invoice_id");

                ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
                String query2 = "select * from invoice inner join orders on invoice.invoice_id = orders.invoice_id where invoice.invoice_id=?;";
                PreparedStatement st2 = con.prepareStatement(query2);
                st2.setInt(1, invoice_id);
                ResultSet rs2 = st2.executeQuery();
                while(rs2.next()) {
                    invoice.setInvoice_id(invoice_id);
                    invoice.setUser_id(rs2.getInt("user_id"));
                    invoice.setDate(rs2.getString("date"));
                    invoice.setTotal(rs2.getInt("total"));
                    ItemDTO itemDTO =new ItemDTO();
                    itemDTO.setItem_id(rs2.getInt("item_id"));
                    itemDTO.setQty(rs2.getInt("qty"));
                    itemDTO.setPrice(rs2.getInt("price"));

                    itemDTOS.add(itemDTO);
                }
                invoice.setItemDTOS(itemDTOS);
                invoices.add(invoice);
            }
            return invoices;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<InvoiceDTO> getInvoiceAll() {
        try{
            String query="select invoice_id from invoice;";
            ArrayList<InvoiceDTO> invoices=new ArrayList<>();
            Class.forName(driver);
            Connection con = DriverManager.getConnection(host, user, pass);
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while(rs.next())
            {
                InvoiceDTO invoice=new InvoiceDTO();
                int invoice_id=rs.getInt("invoice_id");

                ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
                String query2 = "select * from invoice inner join orders on invoice.invoice_id = orders.invoice_id where invoice.invoice_id=?;";
                PreparedStatement st2 = con.prepareStatement(query2);
                st2.setInt(1, invoice_id);
                ResultSet rs2 = st2.executeQuery();
                while(rs2.next()) {
                    invoice.setInvoice_id(invoice_id);
                    invoice.setUser_id(rs2.getInt("user_id"));
                    invoice.setDate(rs2.getString("date"));
                    invoice.setTotal(rs2.getInt("total"));
                    ItemDTO itemDTO =new ItemDTO();
                    itemDTO.setItem_id(rs2.getInt("item_id"));
                    itemDTO.setQty(rs2.getInt("qty"));
                    itemDTO.setPrice(rs2.getInt("price"));

                    itemDTOS.add(itemDTO);
                }
                invoice.setItemDTOS(itemDTOS);
                invoices.add(invoice);
            }
            return invoices;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addInvoice(InvoiceDTO invoice){
        try{
            String query="insert into invoice values(?,?,?,?);";
            Class.forName(driver);
            Connection con=DriverManager.getConnection(host,user,pass);
            con.setAutoCommit(false);
            Savepoint save1=con.setSavepoint("save1");
            PreparedStatement st=con.prepareStatement(query);
            st.setInt(1,invoice.getInvoice_id());
            st.setInt(2,invoice.getUser_id());
            st.setString(3,invoice.getDate());
            st.setInt(4,invoice.getTotal());
            st.executeUpdate();
            for(ItemDTO itemDTO : invoice.getItemDTOS()){
                    String query2 = "insert into orders values(?,?,?,?);";
                    PreparedStatement st2 = con.prepareStatement(query2);
                    st2.setInt(1, invoice.getInvoice_id());
                    st2.setInt(2, itemDTO.getItem_id());
                    st2.setInt(3, itemDTO.getQty());
                    st2.setInt(4, itemDTO.getPrice());
                    if(itemDTO.checkStock(itemDTO.getItem_id(), itemDTO.getQty()))
                    {
                        String query3="update items set stock=stock-? where item_id=?;";
                        PreparedStatement st3=con.prepareStatement(query3);
                        st3.setInt(1, itemDTO.getQty());
                        st3.setInt(2, itemDTO.getItem_id());
                        st3.executeUpdate();
                        st2.executeUpdate();
                    }
                    else{
                        con.rollback(save1);
                        return "Insufficient Stock";
                    }

            }
            con.commit();
            return "Invoice added succesfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
}
