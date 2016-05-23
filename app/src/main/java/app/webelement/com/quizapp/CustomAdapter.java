package app.webelement.com.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.webelement.com.quizapp.Model.OptionQ;


/**
 * Created by ADMIN on 2/6/16.
 */
public class CustomAdapter extends BaseAdapter {


    private final Context context;
    private final ArrayList<OptionQ> optionQArrayList;
    public static int selected=-1;

    public CustomAdapter(Context context, ArrayList<OptionQ> questionList) {

        this.context = context;
        this.optionQArrayList = questionList;

    }

    @Override
    public int getCount() {
        return optionQArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(context);

        convertView = inflater.inflate(R.layout.option_row, null);

        TextView textView = (TextView) convertView.findViewById(R.id.option_name);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);


        OptionQ optionQ = optionQArrayList.get(position);
        textView.setText(optionQ.getName());


        if (position == selected) {
            icon.setVisibility(View.VISIBLE);

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                icon.setVisibility(View.VISIBLE);
                selected=position;
                notifyDataSetChanged();


            }
        });

        return convertView;
    }
}
