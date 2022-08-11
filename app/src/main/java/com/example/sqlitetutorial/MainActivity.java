package com.example.sqlitetutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // create references to buttons and other controls
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define buttons/controls
        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        ShowCustomersOnListView(databaseHelper);

        // button listeners
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());

                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    customerModel = new CustomerModel(-1, "error", 0, false);
                    Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                }

                databaseHelper = new DatabaseHelper(MainActivity.this);

                boolean success = databaseHelper.addOne(customerModel);

                ShowCustomersOnListView(databaseHelper);

                Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper = new DatabaseHelper(MainActivity.this);
                //List<CustomerModel> everyone = databaseHelper.getEveryone();

                //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();

                // Print in list area instead of toast
                ShowCustomersOnListView(databaseHelper);

            }
        });


        // item click listener to delete clicked records from the list
        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                // identify and delete clicked customer
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(i);
                databaseHelper.deleteOne(clickedCustomer);

                // Print updated list
                ShowCustomersOnListView(databaseHelper);

                // Toast of deleted item
                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer, Toast.LENGTH_SHORT).show();

            }
        });



    }







    private void ShowCustomersOnListView(DatabaseHelper databaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }

}