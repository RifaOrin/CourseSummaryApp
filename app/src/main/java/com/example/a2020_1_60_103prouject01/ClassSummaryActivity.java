package com.example.a2020_1_60_103prouject01;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

public class ClassSummaryActivity extends AppCompatActivity {
    private EditText dateEditText, lectureEditText, topicEditText, summaryEditText;
    private RadioButton  rbtheory, rblab;

    private String course = "";
    String errMsg = "";

    private long dateLong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summary);

        dateEditText = findViewById(R.id.date);
        lectureEditText = findViewById(R.id.lecture);
        topicEditText = findViewById(R.id.topic);
        summaryEditText = findViewById(R.id.summary);

        rbtheory = findViewById(R.id.theory);
        rblab = findViewById(R.id.lab);


        course = getIntent().getStringExtra("Course");
        TextView tvCourse = findViewById(R.id.course);
        if (course != null) {
            tvCourse.setText(course);
        }


        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClassSummaryActivity.this, LectureListActivity.class);
                i.putExtra("Course", course);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processValidate();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void processValidate(){
        String lectureEdit = lectureEditText.getText().toString().trim();
        String topicEdit = topicEditText.getText().toString().trim();
        String classSummary = summaryEditText.getText().toString();
        boolean theory = rbtheory.isChecked();
        boolean lab = rblab.isChecked();


        if (classSummary.length()<5){
            errMsg+="Class Summary Too Short\n";
        }

        if(lectureEdit.length() < 1 || lectureEdit.length() > 50){
            errMsg += "Invalid Lecture Number\n";
        }
        if(topicEdit.length() >= 30){
            errMsg += "Topic length must be less than or equal to 30 characters\n";
        }
        if(topicEdit.length() < 4){
            errMsg += "Topic length must be greater than or equal to 4 characters\n";
        }


        if(errMsg.length() > 0){
            // show the error message here
            showErrorDialog(errMsg);
            return;
        }

        String type = "";
        if(theory){
            type+="Theory";
        }
        else{
            type+="Lab";
        }
        String summaryID = "";
        ClassSummaryDB db = new ClassSummaryDB(this);
        if (summaryID.isEmpty()){
            summaryID = topicEdit + System.currentTimeMillis();
            db.insertLecture(summaryID, course, type, dateLong, lectureEdit, topicEdit, classSummary);
            finish();

        }
        else{
            db.updateLecture(summaryID, course, type, dateLong, lectureEdit, topicEdit, classSummary);
        }
        String[] keys = {"action", "sid", "semester", "id", "course", "type", "topic", "date", "lecture", "summary"};
        String[] values = {"backup", "2020-1-60-103", "2024-1", summaryID, course, type, topicEdit, String.valueOf(dateLong), lectureEdit, classSummary};

        httpRequest(keys, values);

        Intent i = new Intent(ClassSummaryActivity.this, LectureListActivity.class);
        i.putExtra("Course", course);
        startActivity(i);
        finish();


    }




    // Method to show the DatePickerDialog
    private void showDatePickerDialog() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ClassSummaryActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        try{
                            Date date = sdf.parse(selectedDate);
                            assert date != null;
                            dateLong = date.getTime();
                            dateEditText.setText(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
    }
    private void showErrorDialog(String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void httpRequest(final String[] keys, final String[] values){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}