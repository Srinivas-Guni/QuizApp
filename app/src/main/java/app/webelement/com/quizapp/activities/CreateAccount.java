package app.webelement.com.quizapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmEmail;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.NetworkCall.Urls;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.utility.TagName;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/28/16.
 */
public class CreateAccount extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty
    @Email(message = "Invalid Email")
    private EditText user_name;
    @NotEmpty
    @Password(message = "Please valid password")
    private EditText password;

    @NotEmpty
    @ConfirmPassword(message = "Password Mismatch")
    private EditText cpassword;


    private TextInputLayout user_name_layout, password_layout, cpassword_layout;
    private Button btnRegister;
    private Validator validator;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        context = this;
        setViews();
        setListener();

    }

    private void setViews() {

        user_name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pwd);
        cpassword = (EditText) findViewById(R.id.cpwd);


        user_name_layout = (TextInputLayout) findViewById(R.id.usernameWrapper);
        password_layout = (TextInputLayout) findViewById(R.id.passwordWrapper);
        cpassword_layout = (TextInputLayout) findViewById(R.id.cpasswordWrapper);

        btnRegister = (Button) findViewById(R.id.register);


    }

    private void setListener() {

        btnRegister.setOnClickListener(this);
        //  signup.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                removeErrors();
                validator.validate();

                break;
        }

    }

    private void removeErrors() {

        user_name_layout.setErrorEnabled(false);
        password_layout.setErrorEnabled(false);
        cpassword_layout.setErrorEnabled(false);

    }

    @Override
    public void onValidationSucceeded() {

        if (Utility.checkNetworkConnection(context)) {
            registerUser();
        } else {
            Utility.showSnackBar(context, getResources().getString(R.string.nointernet));
        }


    }

    private void registerUser() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.register_user,
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


                                JSONObject user_info = jsonObjectResponse.optJSONObject(TagName.USER);
                                User user = new User();
                                user.setId(user_info.optString("id"));
                                user.setName(user_info.optString("username"));


                                Utility.storeUser(context, user);

                                Intent intent = new Intent(context, NavDrawerActivity.class);
                                startActivity(intent);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user_name.getText().toString());
                params.put("password", password.getText().toString());
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
