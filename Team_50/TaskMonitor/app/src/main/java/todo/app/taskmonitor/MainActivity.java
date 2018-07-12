package todo.app.taskmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycle;
    Button add;
    FirebaseDatabase database;
    DatabaseReference ref;
    List<users> list;
    RecyclerView recyclerView;
    String mob;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = (Button) findViewById(R.id.add);
//        ImageView img=(ImageView)findViewById(R.id.image);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        database=FirebaseDatabase.getInstance();
        ReadBtn();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(mob).child("tasks");
//        Toast.makeText(this, mob, Toast.LENGTH_SHORT).show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<users>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Toast.makeText(MainActivity.this, dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();
                    users val = dataSnapshot1.getValue(users.class);
                    users user = new users();
                    String name = val.getTask_name();
//                    Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                    String time1 = val.getTime1();
                    String time2 = val.getTime2();
                    String loc = val.getLocation();
                    String date = val.getDate();
                    String url=val.getUrl();

                    user.setUrl(url);
                    user.setTask_name(name);
                    user.setTime1(time1);
                    user.setTime2(time2);
                    user.setLocation(loc);
                    user.setDate(date);
                    user.setKey(dataSnapshot1.getKey());
                    list.add(user);
                }
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this,list);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(MainActivity.this,1);
                recyclerView.setLayoutManager(recyce);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
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

    public void add(View view)
    {
        Intent intent=new Intent(this,add.class);
        startActivity(intent);
    }

}
