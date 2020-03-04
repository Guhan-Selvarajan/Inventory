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

@Path("Report")
public class Report {

    String driver="com.mysql.cj.jdbc.Driver";
    String host="jdbc:mysql://localhost:3306/report";
    String user="root";
    String pass="toor123$";


    @GET
    @Path("inventory-summary-month")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ReportDTO> inventorySummaryMonth(@QueryParam("month")int month,@QueryParam("year")int year) {
        ArrayList<ReportDTO> summary = new ArrayList<>();
        try {
            String query = "select item_id,item_name,\n" +
                    "               @temp1:=ifnull((select SUM(qty) from stocks where item_id=items.item_id and bill_id in (select bill_id from bill where MONTH(date)<=? and YEAR(date)=?)),0) as Quantity_In,\n" +
                    "               @temp2:=ifnull((select SUM(qty) from orders where item_id=items.item_id and invoice_id in (select invoice_id from invoice where MONTH(date)<=? and YEAR(date)=?)),0) as Quantity_Out,\n" +
                    "               TRUNCATE(@temp1-@temp2,0) as Stock_On_Hand\n" +
                    "        from items;";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(host, user, pass);
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, month);
            st.setInt(3, month);
            st.setInt(2, year);
            st.setInt(4, year);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ReportDTO entry = new ReportDTO();
                entry.setItem_id(rs.getInt("item_id"));
                entry.setItem_name(rs.getString("item_name"));
                entry.setIn_stock(rs.getInt("Quantity_In"));
                entry.setOut_stock(rs.getInt("Quantity_Out"));
                entry.setHand_stock(rs.getInt("Stock_On_Hand"));
                summary.add(entry);
            }
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @GET
    @Path("inventory-summary")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ReportDTO> inventorySummary(){
        ArrayList<ReportDTO> summary=new ArrayList<>();
        try{
            String query ="select st.item_id,st.item_name,st.in_qty as Quantity_In,SUM(ifnull(o.qty,0)) as Quantity_Out,st.stock as Stock_On_Hand  from orders o right outer join (select i.stock,i.item_id,i.item_name,SUM(ifnull(s.qty,0)) as in_qty from stocks s right outer join items i on s.item_id = i.item_id group by i.item_id) as st on o.item_id = st.item_id group by st.item_id;";
            Class.forName(driver);
            Connection con=DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                ReportDTO entry=new ReportDTO();
                entry.setItem_id(rs.getInt("item_id"));
                entry.setItem_name(rs.getString("item_name"));
                entry.setIn_stock(rs.getInt("Quantity_In"));
                entry.setOut_stock(rs.getInt("Quantity_Out"));
                entry.setHand_stock(rs.getInt("Stock_On_Hand"));
                summary.add(entry);
            }
            return summary;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }



    @GET
    @Path("sales-by-item")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ItemDTO> salesReport(){
        try{
            String query="select i.item_id,i.item_name,SUM(ifnull(o.qty,0)) as qty, SUM(ifnull(o.price,0)) as total from orders o right outer join items i on o.item_id = i.item_id group by i.item_id;";
            Class.forName(driver);
            Connection con= DriverManager.getConnection(host,user,pass);
            PreparedStatement st=con.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            ArrayList<ItemDTO> itemDTOS =new ArrayList<>();
            while(rs.next()){
                ItemDTO itemDTO =new ItemDTO();
                itemDTO.setItem_id(rs.getInt("item_id"));
                itemDTO.setItem_name(rs.getString("item_name"));
                itemDTO.setQty(rs.getInt("qty"));
                itemDTO.setPrice(rs.getInt("total"));
                itemDTOS.add(itemDTO);
            }

            String query2="select sum(qty) as TOTAL_QTY,sum(total) as TOTAL_SALE from (select i.item_id,i.item_name,SUM(ifnull(o.qty,0)) as qty, SUM(ifnull(o.price,0)) as total from orders o right outer join items i on o.item_id = i.item_id group by i.item_id) as x;";
            PreparedStatement st2=con.prepareStatement(query2);
            ResultSet rs2=st2.executeQuery();
            if(rs2.next()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItem_id(-1);
                itemDTO.setItem_name("TOTAL");
                itemDTO.setPrice(rs2.getInt("TOTAL_SALE"));
                itemDTO.setQty(rs2.getInt("TOTAL_QTY"));
                itemDTOS.add(itemDTO);
            }
            con.close();
            return itemDTOS;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
