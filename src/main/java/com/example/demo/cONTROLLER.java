package com.example.demo;

 import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class cONTROLLER {

    @RequestMapping("/TableName")
    public ArrayList<String> index() {

    	 Connection con = null;
         Statement st = null;
         ResultSet rs = null;
         ArrayList<String> al=new ArrayList<String>();

         String url = "jdbc:postgresql://db-7a9c1875-0005-4d58-b5a0-75930add96b1.c7uxaqxgfov3.us-west-2.rds.amazonaws.com:5432/postgres";
         String user = "ui317m0ucytv6i8l";
         String password = "yrb80dgx1usvuaiiitxbphi31";
        
   
/*
         String url = "jdbc:postgresql://localhost:5432/postgres";
         String user = "postgres";
         String password = "1234";*/
         try {
             con = DriverManager.getConnection(url, user, password);
             st = con.createStatement();

             rs = st.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public'");
            while(rs.next()) {

            	al.add(rs.getString(1));
             }




         } catch (SQLException ex) {

        	 System.out.println("ERROR:::"+ex.getMessage());

         } finally {
             try {
                 if (rs != null) {
                     rs.close();
                 }
                 if (st != null) {
                     st.close();
                 }
                 if (con != null) {
                     con.close();
                 }

             } catch (SQLException ex) {
               
        
             }
         }
        return al;
    }
    @RequestMapping("/TableData/{Tablename}")
    public String  index1(@PathVariable String Tablename,HttpServletResponse response) throws FileNotFoundException,IOException,FileNotFoundException, IOException  {
    	 String filename = "NewExcelFile.xls" ;
         HSSFWorkbook workbook = new HSSFWorkbook();
         HSSFSheet sheet = workbook.createSheet("FirstSheet");  
         

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;

     /*   String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "1234";*/
        
        String url = "jdbc:postgresql://db-7a9c1875-0005-4d58-b5a0-75930add96b1.c7uxaqxgfov3.us-west-2.rds.amazonaws.com:5432/postgres";
        String user = "ui317m0ucytv6i8l";
        String password = "yrb80dgx1usvuaiiitxbphi31";
        List<String>data=new ArrayList<String>();
        System.out.println("=========================================");
        try {
        	System.out.println("connected");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("connected");
            st = con.createStatement();
           rs1 = st.executeQuery("SELECT * FROM public."+Tablename+"");
            
            ResultSetMetaData metaData = rs1.getMetaData();
            int rowCount = metaData.getColumnCount();
            System.out.println(Tablename+" "+metaData.getColumnCount());
            System.out.println("Table Name : " + metaData.getTableName(1));
            HSSFRow rowhead = sheet.createRow((short)0);
            for (int i = 0; i < rowCount; i++) {
            System.out.print(metaData.getColumnName(i + 1) + " \t");
            data.add(metaData.getColumnName(i + 1));
            rowhead.createCell(i).setCellValue(metaData.getColumnName(i + 1));
            }
            int k=1;
            while(rs1.next())
            {	
            	HSSFRow row = sheet.createRow((short)k);
            	int p=0;
            	for (String s:data) {
            			System.out.print(rs1.getObject(s).toString()+ " \t");
            			row.createCell(p).setCellValue(rs1.getObject(s).toString());
            			p++;
				}
               	k++;
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            //------------------------download from cloud--------------------

            byte[] excelBytes = null;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                excelBytes = out.toByteArray();
            } catch (Exception e) {
               System.out.println("ERROR:::"+e.getMessage());
            }


            try {
            	byte[] partRules=excelBytes;
    			InputStream is = new ByteArrayInputStream(partRules);
    			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    			IOUtils.copy(is, response.getOutputStream());
    			response.flushBuffer();
    		} catch (IOException e) {
    			System.out.println(e.getMessage());
    		}
            fileOut.close();
            System.out.println("Your excel file has been generated!");


        } catch (SQLException ex) {
        	System.out.println("ERROR:::"+ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();

                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
              
            }
        }
		return "DATA";
    }
}