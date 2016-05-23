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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.webelement.com.quizapp.MainActivity;
import app.webelement.com.quizapp.Model.Result;
import app.webelement.com.quizapp.Model.ResultInfo;
import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.NetworkCall.Urls;

import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.utility.TagName;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/22/16.
 */
public class Login extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    private Button btnLogin;
    private Context context;

    @NotEmpty
    private EditText user_name;


    @NotEmpty
    private EditText password;
    private Validator validator;
    private TextInputLayout password_layout, user_name_layout;
    private TextView signup;
    private TextView forgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        context = this;

        generateUUID();
        setViews();
        setListener();


    }

    private void generateUUID() {

        UUID uuid = UUID.randomUUID();
        System.out.println("UUID : " + uuid.toString());

    }

    private void setListener() {

        btnLogin.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    private void setViews() {

        user_name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pwd);
        signup = (TextView) findViewById(R.id.signup);
        forgot = (TextView) findViewById(R.id.forgot);


        user_name_layout = (TextInputLayout) findViewById(R.id.usernameWrapper);
        password_layout = (TextInputLayout) findViewById(R.id.passwordWrapper);

        btnLogin = (Button) findViewById(R.id.login);
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
                Log.d("Login", "clicked");
                removeErrors();
                validator.validate();
               /* Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
*/

                break;
            case R.id.signup:

                Intent intent = new Intent(context, CreateAccount.class);
                startActivity(intent);


                break;
            case R.id.forgot:

                Intent intent1 = new Intent(context, ForgotPassword.class);
                startActivity(intent1);

                break;
            default:
                break;

        }
    }

    private void removeErrors() {

        user_name_layout.setErrorEnabled(false);
        password_layout.setErrorEnabled(false);

    }

    @Override
    public void onValidationSucceeded() {
        // Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();

        if (Utility.checkNetworkConnection(context)) {
            loginUser();
        } else {
            Utility.showSnackBar(context, getResources().getString(R.string.nointernet));
        }
    }

    private void loginUser() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.login,
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
