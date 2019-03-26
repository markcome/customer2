package it.polito.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private Button button;
    public static final String MY_PREFS = "costumer_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_save) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        editSharePreferences();
        return super.onOptionsItemSelected(item);
    }

    private void editSharePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();

        EditText et = findViewById(R.id.nameText);
        editor.putString("name", et.getText().toString());
        et = findViewById(R.id.surnameText);
        editor.putString("surname", et.getText().toString());
        et = findViewById(R.id.phoneText);
        editor.putString("phone", et.getText().toString());
        et = findViewById(R.id.emailText);
        editor.putString("email", et.getText().toString());
        et = findViewById(R.id.addressText);
        editor.putString("address", et.getText().toString());
        et = findViewById(R.id.descriptionText);
        editor.putString("description", et.getText().toString());

        editor.apply();
    }
}
