package app.webelement.com.quizapp.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.webelement.com.quizapp.Model.UserAnswer;
import app.webelement.com.quizapp.R;

/**
 * Created by ADMIN on 3/21/16.
 */
public class HorizontalTest extends AppCompatActivity {
    private GridView horizontalGridView;
    Context context;
    ArrayList<UserAnswer> userAnswersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_scroll);
        context = this;

        horizontalGridView = (GridView) findViewById(R.id.horizontal_gridView);

        populateList();
        createJson();
        populate();


    }

    private void populateList() {

        for (int i = 0; i < 5; i++) {

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQid((i + 1) + "");
            userAnswer.setAnsid((i + 2) + "");
            userAnswersList.add(userAnswer);


        }

    }

    private void createJson() {

        JSONArray jsonArray = new JSONArray();
        for (UserAnswer userAnswer : userAnswersList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("q_id", userAnswer.getQid());
                jsonObject.put("ans_id", userAnswer.getAnsid());
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_submission", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json", jsonObject.toString());

    }

    public void populate() {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            list.add("Test " + i);
        }

        gridViewSetting(horizontalGridView, list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);

        horizontalGridView.setAdapter(adapter);

        //   horizontalGridView.setNumColumns(list.size());


    }

    private void gridViewSetting(GridView gridview, ArrayList<String> list) {

        int size = list.size();
        // Calculated single Item Layout Width for each grid element ....
        int width = 0;

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(singleItemWidth);
        gridview.setHorizontalSpacing(2);
        gridview.setStretchMode(GridView.STRETCH_SPACING);
        gridview.setNumColumns(size);
    }

}
