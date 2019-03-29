package it.polito.lab1;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS = "costumer_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateDataFromPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_edit) {
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDataFromPreferences () {
        SharedPreferences preferences = getSharedPreferences(MY_PREFS, 0);

        TextView tv;


        String name = preferences.getString("name", null);
        if (name != null) {
            tv = findViewById(R.id.nameText);
            tv.setText(name);
        }

        String surname = preferences.getString("surname", null);
        if (surname != null) {
            tv = findViewById(R.id.surnameText);
            tv.setText(surname);
        }

        String phone = preferences.getString("phone", null);
        if (phone != null) {
            tv = findViewById(R.id.phoneText);
            tv.setText(phone);
        }

        String mail = preferences.getString("mail", null);
        if (mail != null) {
            tv = findViewById(R.id.emailText);
            tv.setText(mail);
        }

        String description = preferences.getString("description", null);
        if (description != null) {
            tv = findViewById(R.id.descriptionText);
            tv.setText(description);
        }

        String address = preferences.getString("address", null);
        if (address != null) {
            tv = findViewById(R.id.addressText);
            tv.setText(address);
        }
        String profilePicPref = preferences.getString("profileImage", null);
        if (profilePicPref != null) {
            Uri profilePicUri = Uri.parse(profilePicPref);
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePicUri);
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                circularBitmapDrawable.setCircular(true);
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageDrawable(circularBitmapDrawable);
            } catch (Exception e) {
            }
        }
    }

}
