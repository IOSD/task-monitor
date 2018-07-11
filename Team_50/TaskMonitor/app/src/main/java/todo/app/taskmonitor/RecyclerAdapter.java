package todo.app.taskmonitor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder>{
    private List<users> list;
    private Context context;
    public RecyclerAdapter(Context context,List<users> list) {
        this.list = list;
        this.context = context;
    }
    @Override
//    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.card,parent,false);
//        MyHolder myHolder = new MyHolder(view);
//
//
//        return myHolder;
//    }
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        users mylist = list.get(position);
        holder.task.setText("Task Name  :  "+mylist.getTask_name());
        //holder.img.setImageURI(mylist.getImage());
        holder.date.setText("Date  :  "+mylist.getDate());
        String url=mylist.getUrl();
        String key=mylist.getKey();
//        set(url);
//        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
        Picasso.with(context).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.img,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess()
                    {

                    }
                    @Override
                    public void onError(){}
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,displayTask.class);

                // passing data to the book activity
                intent.putExtra("Task_Name",list.get(position).getTask_name());
                intent.putExtra("Date",list.get(position).getDate());
                intent.putExtra("Time1",list.get(position).getTime1());
                intent.putExtra("Time2",list.get(position).getTime2());
                intent.putExtra("Location",list.get(position).getLocation());
                intent.putExtra("Url",list.get(position).getUrl());
                intent.putExtra("Key",list.get(position).getKey());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception e){



        }

        return arr;

    }
//@Override
//public int getItemCount() {
//    return list.size();
//}

    class MyHolder extends RecyclerView.ViewHolder {
        TextView task,date;
        CardView cardView ;
        ImageView img;

        public MyHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.image);
            task = (TextView)itemView.findViewById(R.id.task);
            date= (TextView) itemView.findViewById(R.id.date);

            cardView=(CardView)itemView.findViewById(R.id.card);


        }

    }

}
