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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etInfo;
    private TextView tvInfo;
    private final static String SHARED_PREFERENCES_FILENAME = "my-preferences";
    private final static String UPDATE_INFO_KEY = "update-info";
    private static final String EMPTY_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (v.getId() == R.id.btnOk){
            Log.i("btnOk", "btnOk clicked");
            storeUpdateInfoIntoPreferences();
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
