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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
            String today;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(cal.getTime());
//            System.out.println("DATE : " + strDate);

            return  strDate;

        }
        catch (Exception e )
        {
            Toast.makeText(this, "Error Fetching Date", Toast.LENGTH_LONG).show();
        }
        return "";
    }

    public String getContentDate(@NonNull Cursor position)
    {
        long timestampMillis = position.getLong(position.getColumnIndexOrThrow("date" ));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(timestampMillis);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public boolean isSameDate(String checker, String checked)
    {
        String[] last = checker.split("[- ]");
        String[] latest = checked.split("[- ]");

        if((last[0].equals(latest[0])) && (last[1].equals(latest[1])) && (last[2].equals(latest[2])) )
        {
            return true;
        }
        return false;


    }

    public void readSMS(View button) {
        try {
            String lastcheckeddate, currentdate;
            String months[] = {" ","Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sep",
                    "Oct", "Nov", "Dec"};


            Uri sms = Uri.parse("content://sms");
            Pattern pattern = Pattern.compile("(?i)((debit|debited|credit|credited|withdrawn))");
            Pattern pattern1 = Pattern.compile("(?i)(a/c)");
//            Pattern pattern = Pattern.compile("(?i)(account)+(?:credit|debit)");



            Cursor cursor = null;
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(sms, null, null, null, null);
            //}
            cursor.moveToFirst();
//            long datetime = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
//            String rangeDate = datetime.split(" ");
//            //
            lastcheckeddate = gettoday();


            display.setText(null);
            while (!cursor.isAfterLast()) {
                // Read the message
                String message = cursor.getString(12);
                Matcher matcher = pattern.matcher(message);
                Matcher matcher1 = pattern1.matcher(message);
                if (matcher.find() && matcher1.find()) {
                    currentdate = getContentDate(cursor);
//                    String date = getContentDate(cursor);
//                    isSameDate(lastcheckeddate, currentdate);
                    if(!isSameDate(lastcheckeddate, currentdate))
                    {
                        String[] splitdate = currentdate.split("[- ]");
                        String printDate = months[Integer.parseInt(splitdate[1]) ] + " " + splitdate[2] + ", " +splitdate[0];
                        display.append("----------------------------------------------------\n");
                        display.append( printDate + "\n\n");
                        lastcheckeddate = currentdate;

                    }

                    // Display the message
                    display.append(message + "\n\n");
                }

                // Move to the next message
                cursor.moveToNext();
            }
            //        display.setText(cursor.getString(12));

            gettoday();
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_LONG).show();
        }

    }
}