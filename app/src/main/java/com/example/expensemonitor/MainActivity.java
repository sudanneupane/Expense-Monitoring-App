package com.example.expensemonitor;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView display;
//    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.output);
        display.setMovementMethod(new ScrollingMovementMethod());

//    Requesting permission to access the SMS
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public String gettoday()
    {
        try {
            LocalDate today = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                today = LocalDate.now();
            }
            return today.toString();
        }
        catch (Exception e )
        {
            System.exit(0);
        }
        return "";
    }

    public void readSMS(View button) {
        try {

            Uri sms = Uri.parse("content://sms");
            Pattern pattern = Pattern.compile("(?i)((debit|debited|credit|credited|withdrawn))");
            Pattern pattern1 = Pattern.compile("(?i)(a/c)");
//            Pattern pattern = Pattern.compile("(?i)(account)+(?:credit|debit)");



            Cursor cursor = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(sms, null, null, null);
            }
            cursor.moveToFirst();

//            long datetime = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
//            String rangeDate = datetime.split(" ");
//            //


            display.setText(null);
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
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_LONG).show();
        }

    }
}