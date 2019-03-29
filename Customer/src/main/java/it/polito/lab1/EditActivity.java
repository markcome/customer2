package it.polito.lab1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {


    public static final String MY_PREFS = "costumer_prefs";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int IMGPRV = 2;

    private ImageView imageView;
    private Uri profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageView = findViewById(R.id.imageView);
        updateDataFromPreferences();
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

    private void updateDataFromPreferences () {
        SharedPreferences preferences = getSharedPreferences(MY_PREFS, 0);

        EditText et;

        String name = preferences.getString("name", null);
        if (name != null) {
            et = findViewById(R.id.nameText);
            et.setText(name);
        }

        String surname = preferences.getString("surname", null);
        if (surname != null) {
            et = findViewById(R.id.surnameText);
            et.setText(surname);
        }

        String phone = preferences.getString("phone", null);
        if (phone != null) {
            et = findViewById(R.id.phoneText);
            et.setText(phone);
        }

        String mail = preferences.getString("mail", null);
        if (mail != null) {
            et = findViewById(R.id.emailText);
            et.setText(mail);
        }

        String description = preferences.getString("description", null);
        if (description != null) {
            et = findViewById(R.id.descriptionText);
            et.setText(description);
        }

        String address = preferences.getString("address", null);
        if (address != null) {
            et = findViewById(R.id.addressText);
            et.setText(address);
        }
        String profilePicPref = preferences.getString("profileImage", null);
        if (profilePicPref != null) {
            profilePicUri = Uri.parse(profilePicPref);
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePicUri);
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            } catch (Exception e) {
            }
        }
    }
    private void editSharePreferences(){
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

        if (profilePicUri != null) {
            editor.putString("profileImage", profilePicUri.toString());
        }

        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case IMGPRV:
                    profilePicUri = data.getData();
                case REQUEST_IMAGE_CAPTURE:
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePicUri);
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    } catch (Exception e) {
                        System.out.println("Error handling imageView");
                    }
            }
        }
    }

    public void onEditImageClick(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditActivity.this);

        alertDialogBuilder
                .setTitle(R.string.select_picture)
                .setPositiveButton(R.string.camera, (dialog, which) -> dispatchTakePictureIntent())
                .setNegativeButton(R.string.gallery, (dialog, which) -> {
                    Intent importFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(importFromGallery, IMGPRV);
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dispatchTakePictureIntent () {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {

            }

            if (photoFile != null) {
                profilePicUri = FileProvider.getUriForFile(this, this.getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePicUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
