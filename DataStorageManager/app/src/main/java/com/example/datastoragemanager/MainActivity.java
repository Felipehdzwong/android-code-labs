package com.example.datastoragemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String INTERNAL_STORAGE_FILENAME = "my-internal-file";
    private static final String SHARED_PREFERENCES_FILENAME = "my-preferences-file";
    private static final String UPDATE_INFO_KEY = "update-info";
    private static final String EMPTY_STRING = "";
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

        etInfo = findViewById(R.id.edtTextUpdate);
        tvInfo = findViewById(R.id.textViewHello);
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
            default:
                Log.w("Switch block", "No case matched");
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
