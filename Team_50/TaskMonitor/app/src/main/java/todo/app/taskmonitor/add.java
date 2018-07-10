package todo.app.taskmonitor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class add extends AppCompatActivity {
    EditText task,loc;
//    Button date1,time1,add1;
    TextView dat,tim1,tim2;
    ImageView img;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 456;
    StorageReference mStorageRef;
    Uri downloadUrl;
    String link="",dt,tt1,tt2,mob,taskno;
    FirebaseDatabase database;
    static final int READ_BLOCK_SIZE = 100;
    DatabaseReference ref;
    users user;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        task=(EditText)findViewById(R.id.task);
        loc=(EditText)findViewById(R.id.location);
        img=(ImageView)findViewById(R.id.img1);
        dat=(TextView)findViewById(R.id.date);
        tim1=(TextView)findViewById(R.id.time1);
        tim2=(TextView)findViewById(R.id.time2);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance();
//        ref=database.getReference("users").child("9734793007");
        user=new users();
    }
//Image capture starts....
public void showFileChooser(View view) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference riversRef = mStorageRef.child("images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
//                            Uri downloadUrl = StorageReference.getDownloadUrl();
                            //and displaying a success toast
                            link=downloadUrl.toString();
                            Toast.makeText(getApplicationContext(), "File Uploaded "+link, Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }


    }

//Image capture ends...

//Date and Time Picker starts....
    public void datepick(View view)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dat.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dt=dat.getText().toString();
                        Toast.makeText(add.this, dt, Toast.LENGTH_SHORT).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void timepick(View view)
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tim1.setText(hourOfDay + ":" + minute);
                        tt1=tim1.getText().toString();
                        Toast.makeText(add.this, tt1, Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void timepick2(View view)
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tim2.setText(hourOfDay + ":" + minute);
                        tt2=tim1.getText().toString();
                        Toast.makeText(add.this, tt2, Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
//Date and Time Picker ends....

    //function to read mobile number for using it as primary key.
    public void ReadBtn() {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("mobile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
                mob=s.toString();
            }
            InputRead.close();

//            textmsg.setText("");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ReadBtn1() {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("taskn.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
                taskno=s.toString();
            }
            InputRead.close();

//            textmsg.setText("");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVal1()
    {
//        user.setName(name.getText().toString());
        user.setTask_name(task.getText().toString());
        user.setDate(dat.getText().toString());
        user.setLocation(loc.getText().toString());
        user.setTime1(tim1.getText().toString());
        user.setTime2(tim2.getText().toString());
        user.setUrl(link);
    }
    public void write2() {
//        Toast.makeText(getBaseContext(), "1", Toast.LENGTH_SHORT).show();
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("taskn.txt", MODE_PRIVATE);//modeprivate-->modeappend
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(taskno+1);
//            outputWriter.write("0");
            //outputWriter.append(textmsg0.getText().toString());
            outputWriter.close();

            //display file saved message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add1(View view)
    {
        uploadFile();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVal1();
                ReadBtn();
                ReadBtn1();
                ref.child(mob).child("tasks").child(taskno+1).setValue(user);
                Toast.makeText(add.this, "success1", Toast.LENGTH_SHORT).show();
                write2();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
