package todo.app.taskmonitor;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.awt.font.TextAttribute;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class displayTask extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref ;
    TextView task,date,time1,time2,location;
    String task1,date1,time11,time12,location1,url,key;
    ImageView img;
    List<users> list;
    String mob;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        task=(TextView)findViewById(R.id.task);
        date=(TextView)findViewById(R.id.date);
        time1=(TextView)findViewById(R.id.time1);
        time2=(TextView)findViewById(R.id.time2);
        location=(TextView)findViewById(R.id.location);
        img=(ImageView)findViewById(R.id.image);
        Intent intent=getIntent();
        task1 = intent.getExtras().getString("Task_Name");
        date1 = intent.getExtras().getString("Date");
        time11 = intent.getExtras().getString("Time1");
        time12 = intent.getExtras().getString("Time2");
        location1 = intent.getExtras().getString("Location");
        url=intent.getExtras().getString("Url");
        key=intent.getExtras().getString("Key");

        task.setText("Task Name  :  "+task1);
        date.setText("Date  :  "+date1);
        time1.setText("Start Time  :  "+time11);
        time2.setText("End Time  :  "+time12);
        location.setText("Location  :  "+location1);
        set(url);
    }

    public void set(String url)
    {
        ImageView img=(ImageView)findViewById(R.id.image) ;
        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(img,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess()
                    {

                    }
                    @Override
                    public void onError(){}
                });

    }

    public void dl(View view)
    {
        ReadBtn();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(mob).child("tasks").child(key);
        ref.setValue(null);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

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
}
