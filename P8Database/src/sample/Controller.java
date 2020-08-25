package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.sql.SQLDataException;
import java.sql.SQLException;

public class Controller {

    private int recordID;
    private String name;
    private String color;
    private int size;
    private String barb;

    private DBManager manager = new DBManager();
    private ObservableList<String> flyList = FXCollections.observableArrayList();

    @FXML private TextField txtId;

    @FXML private Button btnInsert;

    @FXML private Button btnDelete;

    @FXML private Button btnEnterId;

    @FXML private Button btnEdit;

    @FXML private Button btnDisplay;

    @FXML private TextField txtName;

    @FXML private TextField txtColor;

    @FXML private TextField txtSize;

    @FXML private TextField txtBarb;

    @FXML private ListView<String> lvTable;

    @FXML
    void onActionDelete(ActionEvent event) {
        //check if the id textfield is not null
        if(txtId.getText()!=null){
            //get the id from the textfield, and parse
            String sID = txtId.getText();
            recordID = Integer.parseInt(sID);
            manager.deleteRecord(recordID);
            displayDB();
        }else {
            JOptionPane.showMessageDialog(null, "Must enter a valid ID to delete.");
        }
    }

    @FXML
    void onActionEdit(ActionEvent event) {
        getRecordValues();
        //call manager.edit(..);
        manager.editRecord(recordID, name, color, size, barb);
        clearTextFields();
        displayDB();
    }

    @FXML
    void onActionEnterId(ActionEvent event) {
        try{
            //get the recordID from the textfield
            recordID = Integer.parseInt(txtId.getText());
            //create an array of Strings
            String[] rowEdit = new String[4];
            rowEdit = manager.getRecordById(recordID);
            //set the results from the array into the textfields
            txtName.setText(rowEdit[0]);
            txtColor.setText(rowEdit[1]);
            txtSize.setText(rowEdit[2]);
            txtBarb.setText(rowEdit[3]);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "Record ID must be an integer.");
        }

    }

    @FXML
    void onActionInsert(ActionEvent event) {
        //Get the values of the textfields out of the form
        getRecordValues();
        recordID = manager.getLastID();
        //new id will be recordID + 1
        recordID++;
        //call manager.insert(all our fields);
        manager.insert(recordID, name, color, size, barb);
        clearTextFields();
        displayDB();
    }

    @FXML
    void onActiondisplay(ActionEvent event) {
        displayDB();
    }

    public void getRecordValues(){
        //get values from text fields and assign into variables
        try{
            name = txtName.getText();
            color = txtColor.getText();
            size = Integer.parseInt(txtSize.getText());
            barb = txtBarb.getText();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "All fields must be filled and hook size must be an integer.");
        }
    }

    public void clearTextFields(){
        //clear them all
        txtName.clear();
        txtColor.clear();
        txtSize.clear();
        txtBarb.clear();
    }

    private void displayDB(){ //need to display List in the ListView
        //Using the display method/button to manage the table creation/deletion
        //manager.createTable();
        //manager.populateDatabase();
        //manager.dropTable();

        ///*
        //clear the List
        flyList.clear();
        int numRecords = manager.getLastID();
        //create a string array
        String[] record = new String[4];
        //use a loop to go through the rows using numRecords as the size
        for(int i = 1; i<=numRecords; ++i){
            //get each record and check if record[0] is null or isBlank.
            record = manager.getRecordById(i);
            if(record[0].isBlank()){
                continue;
            }else {
                //else add your records in a string to the List
                flyList.add(" "+i+" "+record[0]+" | Color: "+record[1]+" | Size: "+record[2]+" - "+record[3]);
            }
            //when you are done, set the list into the ListView
            lvTable.setItems(flyList);
        }
    }
    //*/
}
