package DBMS;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

import static Model.Color.*;

public class BackUp{
    public static Connection BackUP(Connection con){
        Myconnection my = new Myconnection();
        return my.MyConnection(con);
    }
    public static void main(String[] args) {
        backupTablesToSQLFile(BackUP(null), "backUp.sql");
    }
    public static void backupTablesToSQLFile(Connection conn, String filePath) {
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables("module", null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                writer.println("-- Data for table: " + tableName);
                String table = "create table "+tableName;
                writer.println(table);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rood = rs.getMetaData();
                int columnCount = rood.getColumnCount();

                while (rs.next()) {
                    StringBuilder insert = new StringBuilder("INSERT INTO " + tableName + " VALUES(");
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        if (value == null) {
                            insert.append("NULL");
                        } else if (value instanceof String) {
                            insert.append("'").append(value).append("'");
                        } else {
                            insert.append(value);
                        }
                        if (i < columnCount) insert.append(", ");
                    }
                    insert.append(");");
                    writer.println(insert);
                }
                writer.println();
            }
            System.out.println(LIME_GREEN_GLOW+"\t\t\t\t\t\t\t\t\t\t"+"Backup file created at: " + filePath+RESET);

        } catch (Exception e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\t"+e+RESET);
        }
    }
}