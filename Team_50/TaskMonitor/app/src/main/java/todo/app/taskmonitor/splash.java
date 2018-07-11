package todo.app.taskmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class splash extends AppCompatActivity {

    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Toast.makeText(getBaseContext(), "File saved successfully!",
//                Toast.LENGTH_SHORT).show();
        write1();
        read();
//        Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
    }
    public void write1() {
//        Toast.makeText(getBaseContext(), "1", Toast.LENGTH_SHORT).show();
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("register.txt", MODE_APPEND);//modeprivate-->modeappend
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write("");
//            outputWriter.write("0");
            //outputWriter.append(textmsg0.getText().toString());
            outputWriter.close();

            //display file saved message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read()
    {
//        Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
        try {
            FileInputStream fileIn=openFileInput("register.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            String str=s.toString();
            Toast.makeText(getBaseContext(),str, Toast.LENGTH_SHORT).show();

            if(str.equals("1")) {
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent=new Intent(this,register.class);
                startActivity(intent);
            }
//            textmsg.setText("");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
