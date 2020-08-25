package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JOptionPane;

/**
 *
 * @author inelson1
 */
public class DBManager {

    private Connection connection;
    private Statement statement;
    private final String TABLE_NAME = "WilsonP8";
    private Result result;

    //Constructor established the connection to the database
    public DBManager(){
        try {
            // Load the JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try{
            // Establish a connection
            connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@instora01.admin.ad.cnm.edu:1521:orcl","scott", "burkesummer2020");
            //statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"The database could not be located. "
                            + "Please select the database"+ " file you wish to connect to.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }



    }

    public void insert(int recordID, String jname, String jcolor, int jsize, String jbarb){
        try {
            //Insert a new record into the database
            String insertQuery =
                    "INSERT INTO "+TABLE_NAME +
                            " VALUES (?, ?, ? , ?, ?)";

            PreparedStatement  insertUpdate = null;
            insertUpdate = connection.prepareStatement(insertQuery);

            insertUpdate.setInt(1, recordID);
            insertUpdate.setString(2, jname);
            insertUpdate.setString(3, jcolor);
            insertUpdate.setInt(4, jsize);
            insertUpdate.setString(5, jbarb);
            insertUpdate.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLastID() {
        int ID = 0;
        ResultSet results;
        try {
            statement = connection.createStatement();

            String newQuery = "SELECT MAX(ID)"
                    + "FROM " + TABLE_NAME;
            results = statement.executeQuery(newQuery);

            if (results.next()) {
                ID = Integer.parseInt(results.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ID;
    }

    public String[] getRecordById(int recordID){

        String jname = "";
        String jcolor= "";
        String strjsize = "";
        String jbarb = "";

        try {
            PreparedStatement getRecordStmt = null;


            //Display the contents of the record

            String getRecordQuery = "SELECT * "
                    +"FROM "+TABLE_NAME
                    +" WHERE ID = ?";//+ recordID;
            getRecordStmt = connection.prepareStatement(getRecordQuery);
            getRecordStmt.setInt(1, recordID);

            ResultSet result = getRecordStmt.executeQuery();

            if(result.next())
            {
                jname = result.getString(2);
                jcolor = result.getString(3);
                strjsize = result.getString(4);
                jbarb = result.getString(5);
            }


        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] getRow = {jname, jcolor, strjsize, jbarb};
        return getRow;
    }

    public void editRecord(int recordID, String jname, String jcolor, int jsize, String jbarb){
        try {
            PreparedStatement  editStatement = null;
            //update the record
            String editQuery = "UPDATE " + TABLE_NAME
                    + " SET NAME = ?, COLOR = ?, HOOKSIZE = ?, BARB = ? WHERE ID = ?";
            editStatement = connection.prepareStatement(editQuery);

            editStatement.setString(1, jname);
            editStatement.setString(2, jcolor);
            editStatement.setInt(3, jsize);
            editStatement.setString(4, jbarb);
            editStatement.setInt(5, recordID);
            editStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRecord(int  recordID) {

        try {
            PreparedStatement deleteStmt = null;
            //Delete the record.
            // String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE ID = " + recordID;
            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";
            deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, recordID);
            deleteStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Classes for creating the table and putting values in it.

    //Create a table
    public void createTable(){
        try {
            statement = connection.createStatement();
            statement.executeUpdate
                    ("CREATE TABLE  " +TABLE_NAME+ " " +
                            "(ID NUMBER(3) NOT NULL PRIMARY KEY, "
                            + "NAME Varchar2(30) NOT NULL, "
                            + "COLOR Varchar2(30) NOT NULL , "
                            + "HOOKSIZE NUMBER(2) NOT NULL , "
                            + "BARB Varchar2(30) NOT NULL )");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void populateDatabase(){
        try {
            //Add 5 rows of data to the table
            statement = connection.createStatement();
            String query1 = "INSERT INTO " +TABLE_NAME+ " " +
                    "(ID, NAME, COLOR, HOOKSIZE, BARB)"  +
                    "VALUES (1, 'Elk Hair Caddis', 'Natural', 16, 'Barbed')";
            String query2 = "INSERT INTO " +TABLE_NAME+ " " +
                    "(ID, NAME, COLOR, HOOKSIZE, BARB)"  +
                    "VALUES (2, 'Leech Streamer', 'Black', 8, 'Barbless')";
            String query3 = "INSERT INTO " +TABLE_NAME+ " " +
                    "(ID, NAME, COLOR, HOOKSIZE, BARB)"  +
                    "VALUES (3, 'Copper John', 'Red', 12, 'Barbless')";
            String query4 = "INSERT INTO " +TABLE_NAME+ " " +
                    "(ID, NAME, COLOR, HOOKSIZE, BARB)"  +
                    "VALUES (4, 'Midge', 'Gray', 30, 'Barbed')";
            String query5 = "INSERT INTO " +TABLE_NAME+ " " +
                    "(ID, NAME, COLOR, HOOKSIZE, BARB)"  +
                    "VALUES (5, 'San Juan Worm', 'Pink', 14, 'Barbless')";
            statement.executeQuery(query1);
            statement.executeQuery(query2);
            statement.executeQuery(query3);
            statement.executeQuery(query4);
            statement.executeQuery(query5);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dropTable(){

        try {
            statement = connection.createStatement();

            //Be sure to change the name of the table
            String drop = "Drop Table " +TABLE_NAME+ " " ;
            statement.execute(drop);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
