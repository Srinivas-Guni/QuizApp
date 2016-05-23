package app.webelement.com.quizapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.webelement.com.quizapp.Model.ExamModel;
import app.webelement.com.quizapp.Model.Result;
import app.webelement.com.quizapp.Model.ResultInfo;
import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.NetworkCall.Urls;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.activities.NavDrawerActivity;
import app.webelement.com.quizapp.adapter.RecyclerAdapter;
import app.webelement.com.quizapp.utility.TagName;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/28/16.
 */
public class MyExams extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    ArrayList<ExamModel> examList = new ArrayList<>();
    private Context context;
    private User user;
    ArrayList<ExamModel> examModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_exams_layout, null);

        context = view.getContext();
        setViews(view);

        user = Utility.getUser(context);
        getExamList();

        return view;

    }

    private void getExamList() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.getMyexamList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        System.out.println(response);


                        try {

                            JSONObject jsonObjectResponse = new JSONObject(response);

                            JSONArray jsonArray = jsonObjectResponse.optJSONArray("my_quiz_list");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.optJSONObject(i);
                                String date = jsonObject.optString("date");
                                ExamModel examModel = new ExamModel();
                                examModel.setDate(Utility.formatToYesterdayOrToday(context,date));

                                JSONObject jsonObjectInfo = jsonObject.optJSONObject("info");


                                ResultInfo resultInfo = new ResultInfo();
                                resultInfo.setCorrect_count(jsonObjectInfo.optString("correct_count"));
                                resultInfo.setIncorrect_count(jsonObjectInfo.optString("incorrect_count"));
                                resultInfo.setTotal_count(jsonObjectInfo.optString("total_count"));

                                JSONArray jsonArrayRows = jsonObject.optJSONArray("rows");

                                ArrayList<Result> results = new ArrayList<>();
                                for (int j = 0; j < jsonArrayRows.length(); j++) {

                                    JSONObject jsonObject1 = jsonArrayRows.optJSONObject(j);


                                    Result result = new Result();
                                    result.setAnswer(jsonObject1.optString("answer"));
                                    result.setQuestion(jsonObject1.optString("question"));
                                    result.setCorrect_answer(jsonObject1.optString("correct_answer"));
                                    int bit = Integer.valueOf(jsonObject1.optString("isCorrect"));

                                    boolean status = bit == 1 ? true : false;
                                    result.setIsCorrect(status);
                                    results.add(result);


                                }
                                resultInfo.setResultArrayList(results);
                                examModel.setResultInfo(resultInfo);
                                examModels.add(examModel);


                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (examModels.size() > 0) {
                            mAdapter.notifyDataSetChanged();
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
                params.put("user_id", user.getId());

                // params.put("uuid", uuid.toString());
               /* params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void prepareList() {
        for (int i = 0; i < 15; i++) {
            ExamModel examModel = new ExamModel();
            examModel.setName("Test : " + i);

            examList.add(examModel);

        }

        mAdapter.notifyDataSetChanged();
    }

    private void setViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        mAdapter = new RecyclerAdapter(examModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }
}
