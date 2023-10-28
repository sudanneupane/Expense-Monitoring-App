package com.example.expensemonitor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.output);
        display.setMovementMethod(new ScrollingMovementMethod());

//    Requesting permission to access the SMS
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public void readSMS(View button) {
        try {
            Uri sms = Uri.parse("content://sms");
//            Pattern pattern = Pattern.compile("(?i)((debit|debited|credit|credited))");
            Pattern pattern = Pattern.compile("(?i)(a/c)");


            Cursor cursor = getContentResolver().query(sms, null, null, null);
            cursor.moveToFirst();
            //
            while (!cursor.isAfterLast()) {
                // Read the message
                String message = cursor.getString(12);
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    display.append(message + "\n\n");
                }
                // Display the message
                //            display.append(message + "\n\n");

//                display.append(matcher.find() + "\n\n");
                // Move to the next message
                cursor.moveToNext();
            }
            //        display.setText(cursor.getString(12));


            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
        }

//    public void handleInput(View v)
//    {
//        EditText t = findViewById(R.id.userinput);
//        String inp =t.getText().toString();
//        ((TextView)findViewById(R.id.output)).setText(inp);
//
//
//    }


    }
}