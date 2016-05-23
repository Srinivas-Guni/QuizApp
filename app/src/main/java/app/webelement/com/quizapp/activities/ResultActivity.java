package app.webelement.com.quizapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

import app.webelement.com.quizapp.Model.Result;
import app.webelement.com.quizapp.Model.ResultInfo;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.adapter.ResultAdapter;

/**
 * Created by ADMIN on 3/22/16.
 */
public class ResultActivity extends AppCompatActivity {
    private DonutProgress donut_progress;
    ArrayList<Result> results = new ArrayList<>();
    private ResultInfo resultinfo;
    private ResultActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context = this;
        setToolbar();
        getBundleData();
        setViews();
    }

    private void getBundleData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resultinfo = getIntent().getParcelableExtra("result");

            results = resultinfo.getResultArrayList();

            for (Result result : results) {
                System.out.println(" " + result.getAnswer() + " " + result.isCorrect() + "");
            }

        }


    }

    private void setViews() {


        LayoutInflater inflater = LayoutInflater.from(ResultActivity.this);
        View view = inflater.inflate(R.layout.header, null);


        donut_progress = (DonutProgress) view.findViewById(R.id.donut_progress);
        donut_progress.setMax(Integer.parseInt(resultinfo.getTotal_count()));
        donut_progress.setProgress(Integer.parseInt(resultinfo.getCorrect_count()));

        donut_progress.setSuffixText("/" + resultinfo.getTotal_count());
        donut_progress.setFinishedStrokeColor(Color.GREEN);
        donut_progress.setUnfinishedStrokeColor(Color.RED);

//        donut_progress.setPrefixText("4/5");


        // donut_progress.animate();


        //YoYo.with(Techniques.Bounce).duration(500).playOn(donut_progress);

        ImageView correct_image = (ImageView) view.findViewById(R.id.image1);
        ImageView incorrect_image = (ImageView) view.findViewById(R.id.image2);

        GradientDrawable bgShape = (GradientDrawable) incorrect_image.getBackground();
        bgShape.setColor(Color.RED);

        GradientDrawable bgShape1 = (GradientDrawable) correct_image.getBackground();
        bgShape1.setColor(Color.GREEN);

        TextView correct = (TextView) view.findViewById(R.id.correct);
        TextView incorrect = (TextView) view.findViewById(R.id.incorrect);

        correct.setText(resultinfo.getCorrect_count());
        incorrect.setText(resultinfo.getIncorrect_count());

        YoYo.with(Techniques.Bounce).duration(1500).playOn(donut_progress);
        //  YoYo.with(Techniques.Bounce).duration(1500).playOn(incorrect);
        ListView listView = (ListView) findViewById(R.id.list);

        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            stringArrayList.add(" Test : " + i);

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, stringArrayList);


        listView.addHeaderView(view);
        listView.setAdapter(adapter);
        listView.setAdapter(new ResultAdapter((Context) context, results));

    }

    private void setToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.delete));
        getSupportActionBar().setIcon(R.drawable.logo_25);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


    }
}
