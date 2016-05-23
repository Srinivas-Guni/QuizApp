package app.webelement.com.quizapp.adapter;

/**
 * Created by ADMIN on 3/28/16.
 */


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.webelement.com.quizapp.Model.ExamModel;
import app.webelement.com.quizapp.Model.ResultInfo;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.activities.ResultActivity;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<ExamModel> examList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView no;
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            no = (TextView) view.findViewById(R.id.no);

        }
    }


    public RecyclerAdapter(ArrayList<ExamModel> examList) {
        this.examList = examList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ExamModel exam = examList.get(position);
        holder.name.setText(exam.getDate());
        holder.no.setText("Exam " + (position + 1) + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Toast.makeText(v.getContext(), "clicked" + position, Toast.LENGTH_SHORT).show();

                ResultInfo resultInfo = exam.getResultInfo();
                Intent intent = new Intent(v.getContext(), ResultActivity.class);
                intent.putExtra("result", resultInfo);
                v.getContext().startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return examList.size();
    }
}
