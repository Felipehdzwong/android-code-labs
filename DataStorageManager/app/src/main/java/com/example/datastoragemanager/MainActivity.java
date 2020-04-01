package com.example.datastoragemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CUSTOM_DIRECTORY = "Felipe/my-directory";
    private static final String INTERNAL_STORAGE_FILENAME = "my-internal-file";
    private static final String SHARED_PREFERENCES_FILENAME = "my-preferences-file";
    private static final String UPDATE_INFO_KEY = "update-info";
    private static final String EMPTY_STRING = "";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101; //This could be any value 1, 3, 69301, etc
    private EditText etInfo;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnInternalStorage = findViewById(R.id.btnInternalStorage);
        btnInternalStorage.setOnClickListener(this);

        final Button btnCurrentFiles = findViewById(R.id.btnGetFiles);
        btnCurrentFiles.setOnClickListener(this);

        final Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        final Button btnExternalStorage = findViewById(R.id.btnExternalStorage);
        btnExternalStorage.setOnClickListener(this);

        etInfo = findViewById(R.id.edtTextUpdate);
        tvInfo = findViewById(R.id.textViewHello);
    }

    private void checkExternalStorageWritePermission() {
       final boolean isPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

       if (isPermissionGranted){
           storeUpdateInfoIntoExternalStorage();
       }else {
           ActivityCompat.requestPermissions(this,
                   new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                   WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
       }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        String storedInfo = sharedPreferences.getString(UPDATE_INFO_KEY, getString(R.string.default_text));
        tvInfo.setText(storedInfo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnOk:
                Log.i("btnOk", "btnOk clicked");
                storeUpdateInfoIntoPreferences();
                break;
            case R.id.btnInternalStorage:
                storeUpdateInfoIntoInternalStorage();
                break;
            case R.id.btnGetFiles:
                displayCurrentInternalFiles();
                break;
            case R.id.btnExternalStorage:
                checkExternalStorageWritePermission();
                break;
            default:
                Log.w("Switch block", "No case matched");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // write external storage task you need to do.
                    storeUpdateInfoIntoExternalStorage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.w("Request Permission", "External Storage permission not granted");
                    Toast.makeText(this,"External Storage usage not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void storeUpdateInfoIntoExternalStorage() {
        final boolean isMediaMountedAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(isMediaMountedAvailable){
            createDirectoryInDownloadsPublicFolder(CUSTOM_DIRECTORY);
        }else{
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDirectoryInDownloadsPublicFolder(String path) {
        final File personalFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),path);

        if(!personalFolder.mkdirs()){
            Toast.makeText(this, "Error while creating directory", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Directory created successfully", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayCurrentInternalFiles() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] currentFiles = fileList();

        for (String file : currentFiles) {
            stringBuilder.append(file).append("\n");
        }

        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }

    private void storeUpdateInfoIntoInternalStorage() {
        String info = etInfo.getText().toString()+"\n";

        if(updateInfoIsValid(info)){
            try {
                FileOutputStream fileOutputStream = openFileOutput(INTERNAL_STORAGE_FILENAME, MODE_APPEND);
                fileOutputStream.write(info.getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "Data saved in file", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.w("Try-catch block","File was not found.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Try-catch block","Unable to write or close the file.");
            }finally {
                etInfo.setText(EMPTY_STRING);
            }
        }
        else {
            Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
        }

    }

    private void storeUpdateInfoIntoPreferences() {
        String info = etInfo.getText().toString();

        if (updateInfoIsValid(info)){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString(UPDATE_INFO_KEY, info);
            sharedPreferencesEditor.apply();

            etInfo.setText(EMPTY_STRING);
            Toast.makeText(this, "String '" + info + "' saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean updateInfoIsValid(String info) {
        return !info.trim().isEmpty();
    }
}
