package app.webelement.com.quizapp.fragment;

/**
 * Created by ADMIN on 3/26/16.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import app.webelement.com.quizapp.CustomAdapter;
import app.webelement.com.quizapp.Model.OptionQ;
import app.webelement.com.quizapp.Model.Questions;
import app.webelement.com.quizapp.Model.Result;
import app.webelement.com.quizapp.Model.ResultInfo;
import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.Model.UserAnswer;
import app.webelement.com.quizapp.NetworkCall.CustomHttpclient;
import app.webelement.com.quizapp.NetworkCall.Urls;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.activities.ResultActivity;
import app.webelement.com.quizapp.utility.TagName;
import app.webelement.com.quizapp.utility.Utility;


public class ContentFragment extends Fragment implements View.OnClickListener {


    private ProgressDialog progress;
    private TextView question_name;
    private ListView options_list_view;

    ArrayList<Questions> questionList = new ArrayList<>();
    ArrayList<Result> results = new ArrayList<>();

    HashMap<String, String> user_answers = new HashMap<>();

    public static int current = 0;
    private Context context;
    private Button next;
    ArrayList<UserAnswer> userAnswersList = new ArrayList<>();
    private TextView question_no;
    private RelativeLayout header;
    private String jsonToSend;
    private RelativeLayout start;
    private User user;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_layout,container,false);


        context=v.getContext();
        setViews(v);
        setListener();


        return v;
    }



    //


    private void setListener() {


        start.setOnClickListener(this);
        next.setOnClickListener(this);


        //  user_answers.put(questionList.get(current).getId(), questionList.get(current).getOprtions().get(CustomAdapter.selected).getId());


    }

    private void submitQuiz() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.submitQuizUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        System.out.println(response);

                        ResultInfo resultInfo = new ResultInfo();
                        if (results.size() > 0) {
                            results.clear();
                        }

                        try {

                            JSONObject jsonObjectResponse = new JSONObject(response);
                            JSONObject jsonObjectInfo = jsonObjectResponse.optJSONObject("info");


                            resultInfo.setCorrect_count(jsonObjectInfo.optString("correct_count"));
                            resultInfo.setIncorrect_count(jsonObjectInfo.optString("incorrect_count"));
                            resultInfo.setTotal_count(jsonObjectInfo.optString("total_count"));

                            JSONArray jsonArray = jsonObjectInfo.optJSONArray("rows");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.optJSONObject(i);


                                Result result = new Result();
                                result.setAnswer(jsonObject.optString("answer"));
                                result.setQuestion(jsonObject.optString("question"));
                                result.setCorrect_answer(jsonObject.optString("correct_answer"));
                                int bit = Integer.valueOf(jsonObject.optString("isCorrect"));

                                boolean status = bit == 1 ? true : false;
                                result.setIsCorrect(status);
                                results.add(result);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        resultInfo.setResultArrayList(results);
                        if (results.size() > 0) {

                            start.setVisibility(View.VISIBLE);
                            header.setVisibility(View.GONE);
                            userAnswersList.clear();


                            //  results.clear();

                            Intent intent = new Intent(context, ResultActivity.class);
                            intent.putExtra("result", resultInfo);
                            startActivity(intent);
                        }
                        // Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error.toString());
                        Log.d("error", error.toString());


                        //  Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("json", jsonToSend);

                // params.put("uuid", uuid.toString());
               /* params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


//        new SubmitQuiz().execute();

    }

    private void createJson() {
        UUID uuid = UUID.randomUUID();
        JSONArray jsonArray = new JSONArray();
        for (UserAnswer userAnswer : userAnswersList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("q_id", userAnswer.getQid());
                jsonObject.put("ans_id", userAnswer.getAnsid());
                jsonObject.put("user_id", user.getId());
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", uuid.toString());
            jsonObject.put("user_submission", jsonArray);
            jsonToSend = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("json", jsonToSend);


    }

    private void changeQuestion() {
        question_name.setText(questionList.get(current).getName());

        int question_num = current + 1;
        question_no.setText(question_num + "");
        CustomAdapter.selected = -1;
        options_list_view.setAdapter(new CustomAdapter(context, questionList.get(current).getOprtions()));

        YoYo.with(Techniques.SlideInLeft).duration(1000).playOn(header);

    /*    for (OptionQ o : questionList.get(0).getOprtions()) {
            Log.d("idd", o.getId());
            Log.d("name", o.getName());
        }*/


    }

    private void setViews(View view) {

        header = (RelativeLayout) view.findViewById(R.id.header);
        start = (RelativeLayout) view.findViewById(R.id.start_quiz_layout);

        header.setVisibility(View.GONE);
        question_name = (TextView) view.findViewById(R.id.question_name);
        question_no = (TextView) view.findViewById(R.id.question_no);
        options_list_view = (ListView) view.findViewById(R.id.options);
        next = (Button) view.findViewById(R.id.next);


    }

    private void getQuestionsFromUrl() {

        new GetQuestionsFromUrl().execute();


    }
/*
    private void setToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

  *//*      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*//*
        // getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.delete));
        getSupportActionBar().setIcon(R.drawable.logo_25);


    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.start_quiz_layout:

                if (Utility.checkNetworkConnection(context)) {

                    user = Utility.getUser(context);
                    if (user != null) {

                        Log.d("uesrid", user.getId());

                        getQuestionsFromUrl();
                    }
                } else {

                    Utility.showSnackBar(context, getResources().getString(R.string.nointernet));
                }


                break;
            case R.id.next:

                doNextOrSubmit();

                break;
        }


    }

    private void doNextOrSubmit() {

        if (!next.getText().toString().equalsIgnoreCase("submit")) {

            if (CustomAdapter.selected > -1) {
                UserAnswer userAnswer = new UserAnswer();

                userAnswer.setQid(questionList.get(current).getId());
                userAnswer.setAnsid(questionList.get(current).getOprtions().get(CustomAdapter.selected).getId());
                userAnswersList.add(userAnswer);


                Log.d("qid and ansid", userAnswer.getQid() + " " + userAnswer.getAnsid());

                if (!(current >= questionList.size() - 1)) {
                    current++;
                    changeQuestion();


                } else {
                    next.setText("submit");
                }
            } else {
                YoYo.with(Techniques.Shake).duration(1000).playOn(header);
            }
        } else {


            Log.d("userans", userAnswersList.size() + "");

            if (user != null) {
                createJson();
                submitQuiz();


                for (UserAnswer userAnswer : userAnswersList) {
                    System.out.println(" qid " + userAnswer.getQid() + " Answer Id " + userAnswer.getAnsid());

                }
            }


        }


    }


    private class GetQuestionsFromUrl extends AsyncTask<String, String, String> {
        private String response = "na";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(context, "",
                    "Please Wait", true);


        }

        @Override
        protected String doInBackground(String... params) {

            response = CustomHttpclient.executeGet(Urls.getQuestionsUrl);


            Log.d("response", response);

            try {

                JSONArray jsonArray = new JSONArray(response);

                if (results.size() > 0 || questionList.size() > 0) {
                    results.clear();
                    questionList.clear();
                    current = 0;
                }

                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject questionJson = jsonArray.getJSONObject(i);


                    Questions questions = new Questions();
                    questions.setId(questionJson.optString("id"));
                    questions.setName(questionJson.optString("name"));

                    questionList.add(questions);

                    JSONArray optionJson = questionJson.optJSONArray("options");
                    ArrayList<OptionQ> optList = new ArrayList<>();
                    for (int j = 0; j < optionJson.length(); j++) {
                        JSONObject optionJsonObject = optionJson.getJSONObject(j);
                        OptionQ option = new OptionQ();

                        option.setId(optionJsonObject.optString("options_id"));
                        option.setName(optionJsonObject.optString("option_name"));
                        optList.add(option);


                    }

                    questions.setOptions(optList);


                }


            } catch (JSONException e) {
                e.printStackTrace();
                response = TagName.ISERROR;
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            if (s.equalsIgnoreCase(TagName.ISERROR)) {

            } else {
                //header.setVisibility(View.VISIBLE);
                question_name.setText(questionList.get(current).getName());
                options_list_view.setAdapter(new CustomAdapter(context, questionList.get(current).getOprtions()));


                int question_num = current + 1;
                question_no.setText(question_num + "");
                CustomAdapter.selected = -1;
                next.setText("next");


                start.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft).duration(500).playOn(header);

                for (OptionQ o : questionList.get(0).getOprtions()) {
                    Log.d("idd", o.getId());
                    Log.d("name", o.getName());
                }
            }

        }
    }

    //
}