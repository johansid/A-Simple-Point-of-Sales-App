package edu.depaul.csc472.lanny.xul_pos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import java.text.DecimalFormat;
import java.util.*;


public class MainActivity extends Activity {

    class itemOrder{
        private String name;
        private double price;
        private int quantity;

        public itemOrder(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

    }
    private ArrayList<itemOrder> itemOrders = new ArrayList<itemOrder>();

        // create what's on the menu
    static final String[] MENU = new String[]{
                "Sandwich", "Pizza", "Rice", "Noodle", "Juice", "Smoothie"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiate
        startNewOrder();

        //prepare menu
        final ArrayList<HashMap<String, Double>> menuList = new ArrayList<HashMap<String, Double>>();
        final HashMap<String, Double> hashMenu = new HashMap<String, Double>();
        hashMenu.put("Sandwich", 3.35);
        hashMenu.put("Pizza", 2.575);
        hashMenu.put("Rice", 2.37);
        hashMenu.put("Noodle", 4.26);
        hashMenu.put("Juice", 3.51);
        hashMenu.put("Smoothie", 2.74);

        menuList.add(hashMenu);

        //Auto complete
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MENU);
        final AutoCompleteTextView tv1 = findViewById(R.id.hItem);
        tv1.setAdapter(adapter);
        final EditText unitPrice = findViewById(R.id.hUp);

        //on item click listener
        tv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, Double> map = menuList.get(i);
                Double unitP = map.get(tv1.getText().toString());
                if (unitP != null){
                    unitPrice.setText(String.valueOf(unitP));
                }
            }
        });

        //3 buttons
        Button b1 = findViewById(R.id.NO);
        Button b2 = findViewById(R.id.NI);
        Button b3 = findViewById(R.id.To);

        //set new order button
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                startNewOrder();
            }
        });
        //set new item button
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                resetItem();
            }
        });
        //set total button
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                addSummary();
            }
        });
    }
    // Start a new order
    protected void startNewOrder() {

        itemOrders.clear();

        resetItem();

        TextView tvOrderTotal = findViewById(R.id.hTo);
        TextView tvOrderSum = findViewById(R.id.Sum);

        tvOrderTotal.setText("$0.00");
        tvOrderSum.setText("");
    }
    // reset item
    protected void resetItem() {

        AutoCompleteTextView tvItemName = findViewById(R.id.hItem);
        TextView tvItemQ = findViewById(R.id.hQuan);
        TextView tvItemUP = findViewById(R.id.hUp);

        tvItemName.setText("");
        tvItemQ.setText("1");
        tvItemUP.setText("0.00");
    }
    // add summary to the end
    protected void addSummary() {

        AutoCompleteTextView tvOrderName = findViewById(R.id.hItem);
        EditText tvItemQ = findViewById(R.id.hQuan);
        EditText tvItemUP = findViewById(R.id.hUp);

        String itemName = tvOrderName.getText().toString();

        // check if the item name is empty, show a toast
        if (itemName.isEmpty()){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "The item name is empty!", duration);
            toast.show();
        };

        int orderQ = Integer.parseInt(tvItemQ.getText().toString());
        double unitPrice = Double.parseDouble(tvItemUP.getText().toString());

        itemOrders.add(0, new itemOrder(itemName, unitPrice, orderQ));

        updateSummary();

    }
    //update the summary in the bottom
    protected void updateSummary() {
        double sumPrice = 0;
        String totalSummary = "";
        for (itemOrder item : itemOrders){
            sumPrice +=  (item.quantity * item.price);
            totalSummary = item.name + " x" + item.quantity + "\n" + totalSummary;
        }

        TextView tvOrderTotal = findViewById(R.id.hTo);
        TextView tvOrderSum = findViewById(R.id.Sum);

        DecimalFormat df = new DecimalFormat("#.00");
        tvOrderTotal.setText("$" + String.valueOf(df.format(sumPrice)));

        tvOrderSum.setText(totalSummary);
    }
}
