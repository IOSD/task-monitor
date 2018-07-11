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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

/*
algo:
upload the pic with name in format mobile no+key name

 */
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
    DatabaseReference ref1;
    users user;
    String ch;
    Integer mob1;
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

        user=new users();
        ReadBtn();
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

            final StorageReference ref = mStorageRef.child("images/"+mob+"pic"+user.getTask_name()+".jpg");
            Task uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    progressDialog.dismiss();

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                         downloadUrl = task.getResult();
                         link=downloadUrl.toString();
//                        Toast.makeText(add.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                        add2();


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
        //if there is not any file
        else {
            //you can display an error toast
            link="null";
            add2();
//            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(add.this, dt, Toast.LENGTH_SHORT).show();

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
//                        Toast.makeText(add.this, tt1, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(add.this, tt2, Toast.LENGTH_SHORT).show();
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

    public void getVal1()
    {
//        user.setName(name.getText().toString());
        user.setTask_name(task.getText().toString());
        user.setDate(dat.getText().toString());
        user.setLocation(loc.getText().toString());
        user.setTime1(tim1.getText().toString());
        user.setTime2(tim2.getText().toString());
        user.setUrl(link);
//        Toast.makeText(this, link, Toast.LENGTH_SHORT).show();
    }

    public void add1(View view) {
        getVal1();
        uploadFile();
    }

    public void add2()
    {
        getVal1();
//        Toast.makeText(this, ch, Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        ref1 = database.getReference("users");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVal1();
                ReadBtn();
//                ReadBtn1();

//                ch = String.valueOf(mob1 + 3);
                ref1.child(mob).child("tasks").child(user.getTask_name()).setValue(user);
//                ref1.child("9851124909").child("tasks").child("2").setValue(user);
                Toast.makeText(add.this, "success", Toast.LENGTH_SHORT).show();
//                write2();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
