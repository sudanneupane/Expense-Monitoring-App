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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Pattern pattern = Pattern.compile("(?i)((debit|debited|credit|credited|withdrawn))");
            Pattern pattern1 = Pattern.compile("(?i)(a/c)");
//            Pattern pattern = Pattern.compile("(?i)(account)+(?:credit|debit)");


            Cursor cursor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(sms, null, null, null);
            }
            cursor.moveToFirst();
            //
            while (!cursor.isAfterLast()) {
                // Read the message
                String message = cursor.getString(12);
                Matcher matcher = pattern.matcher(message);
                Matcher matcher1 = pattern1.matcher(message);
                if (matcher.find() && matcher1.find()) {
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