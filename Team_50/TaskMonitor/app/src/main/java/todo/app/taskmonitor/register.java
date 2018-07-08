package todo.app.taskmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class register extends AppCompatActivity {

    EditText name, mobile;
    Button reg;
    FirebaseDatabase database;
    DatabaseReference ref;
    users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.ph);
        reg = (Button) findViewById(R.id.reg);
        database = FirebaseDatabase.getInstance();
        user=new users();
    }

    public void getVal() {
        user.setName(name.getText().toString());
    }

    public void reg(View view) {
        final String ph=mobile.getText().toString();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getVal();
                ref.child(ph).setValue(user);
                Toast.makeText(register.this, "success", Toast.LENGTH_SHORT).show();
                //saving register.txt as 1 as flag value of successfull reg.
                try {
                    FileOutputStream fileout=openFileOutput("register.txt", MODE_PRIVATE);//modeprivate-->modeappend
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                    outputWriter.write(" ");
                    outputWriter.write("1");
                    //outputWriter.append(textmsg.getText().toString());
                    outputWriter.close();

                    //display file saved message
                    Toast.makeText(getBaseContext(), "File saved successfully!",
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //saving mobile number as it is to be used as primary key
                try {
                    FileOutputStream fileout=openFileOutput("mobile.txt", MODE_PRIVATE);//modeprivate-->modeappend
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                    outputWriter.write(" ");
                    outputWriter.write(ph);
                    //outputWriter.append(textmsg.getText().toString());
                    outputWriter.close();

                    //display file saved message
                    Toast.makeText(getBaseContext(), "File saved successfully!",
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
