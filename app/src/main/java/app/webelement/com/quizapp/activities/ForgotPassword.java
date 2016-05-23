package app.webelement.com.quizapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.NetworkCall.Urls;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.utility.TagName;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/29/16.
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty(sequence = 1, message = "This field is required")
    @Email(sequence = 2,message = "Please  Enter valid email")
    private EditText email;
    private Button reset;
    private Context context;
    private Validator validator;
    private TextInputLayout emailWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password_layout);

        context = this;
        setToolbar();
        setViews();
        setListener();


    }

    private void setListener() {


        reset.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    private void setViews() {
        email = (EditText) findViewById(R.id.email);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailwrapper);

        reset = (Button) findViewById(R.id.reset);

    }

    private void setToolbar() {
        LinearLayout rootView = (LinearLayout) findViewById(R.id.toolbar_layout);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.delete));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getSupportActionBar().setIcon(R.drawable.logo_25);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.reset:

                removeErrors();
                validator.validate();

                break;
            default:
                break;

        }


    }

    private void removeErrors() {


        emailWrapper.setErrorEnabled(false);


    }

    private void sendEmail() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.reset,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        System.out.println(response);


                        try {

                            JSONObject jsonObjectResponse = new JSONObject(response);

                            boolean isError = jsonObjectResponse.optBoolean(TagName.ISERROR);
                            String message = jsonObjectResponse.optString(TagName.MESSAGE);
                            if (!isError) {
                                Utility.showSnackBar(context, message);
                                finish();

                            } else {

                                //Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                Utility.showSnackBar(context, message);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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

                UUID uuid = UUID.randomUUID();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email.getText().toString());
                params.put("uuid", uuid.toString());
                // params.put("uuid", uuid.toString());
               /* params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onValidationSucceeded() {

        if (Utility.checkNetworkConnection(context)) {
            sendEmail();
        }else {
            Utility.showSnackBar(context, getResources().getString(R.string.nointernet));
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                TextInputLayout error_view = (TextInputLayout) view.getParent();
                error_view.setErrorEnabled(true);
                error_view.setError(message);


                //((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
