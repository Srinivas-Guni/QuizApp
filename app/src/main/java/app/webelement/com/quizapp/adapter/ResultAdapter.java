package app.webelement.com.quizapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.webelement.com.quizapp.Model.Result;
import app.webelement.com.quizapp.R;

/**
 * Created by ADMIN on 3/23/16.
 */
public class ResultAdapter extends BaseAdapter {


    private final Context context;
    private final ArrayList<Result> results;


    public ResultAdapter(Context context, ArrayList<Result> results) {
        this.context = context;
        this.results = results;

    }

    @Override
    public int getCount() {

        return results.size();
    }

    @Override
    public Result getItem(int position) {

        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Result result = getItem(position);

        final Holder holder;

        View rowView;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.result_row, null);
            holder = new Holder();

            holder.question_no = (TextView) convertView.findViewById(R.id.question_no);
            holder.answer = (TextView) convertView.findViewById(R.id.your_ans);
            holder.correct = (TextView) convertView.findViewById(R.id.correct);
            holder.question = (TextView) convertView.findViewById(R.id.question_name);


            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();

        }

        //   holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.answer.setText("Your Answer : " + results.get(position).getAnswer());
        holder.question.setText(results.get(position).getQuestion());
        holder.question_no.setText((position + 1) + "");

        holder.correct.setText("Correct : " + results.get(position).getCorrect_answer());

        GradientDrawable bgShape = (GradientDrawable) holder.question_no.getBackground();
        if (result.isCorrect()) {

            bgShape.setColor(Color.GREEN);
        } else {
            bgShape.setColor(Color.RED);
        }


        // holder.img.setImageResource(imageId[position]);
        /*rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/


        return convertView;

    }

    public class Holder {
        TextView tv;
        public TextView question;
        public TextView question_no;
        public TextView answer;
        public TextView correct;
        // ImageView img;
    }
}
