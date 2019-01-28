package com.example.android.conde.com.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.conde.com.grocerylist.Data.DatabaseHandler;
import com.example.android.conde.com.grocerylist.Model.Grocery;
import com.example.android.conde.com.grocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog mAlertDialog;
    private EditText mGreoceryItem;
    private EditText mQuantity;
    private Button mSaveButton;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHandler(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        Intent intent = getIntent();
        byPassActivity(intent.getBooleanExtra("noBypass", false));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            createPopupDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        mGreoceryItem = view.findViewById(R.id.et_grocery_item);
        mQuantity = view.findViewById(R.id.et_grocery_quantity);
        mSaveButton = view.findViewById(R.id.btn_save);

        dialogBuilder.setView(view);
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: Save to db
                //Todo: Go to next screen
                if(!mGreoceryItem.getText().toString().isEmpty()
                        && !mQuantity.getText().toString().isEmpty()){
                saveGroceryToDB(view);
                }
            }
        });
    }

    private void saveGroceryToDB(View view) {
        Grocery grocery = new Grocery();
        String newGrocery = mGreoceryItem.getText().toString();
        String newGroceryQuantity = mQuantity.getText().toString();
        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);
        db.addGrocery(grocery);

        Snackbar.make(view, "Item saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        Log.d("ID", "saveGroceryToDB: "+db.getGroceriesCount());

        //Add a little delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  mAlertDialog.dismiss();
                  //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1000);

    }

    /**Goes straight to the list activity so the user doesn't have to enter a new grocery to see
     * what they have in the list if the db isn't empty*/
    public void byPassActivity(boolean bypass){
        if(db.getGroceriesCount() > 0 && !bypass){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            //MainActivity is taken off the stack. User can't go back to it.
            finish();
        }
    }

}
