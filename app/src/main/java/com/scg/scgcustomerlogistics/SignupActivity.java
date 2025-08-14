package com.scg.scgcustomerlogistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.scg.scgcustomerlogistics.util.EncryptedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class SignupActivity extends AppCompatActivity implements OnSuccessListener {
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;

    private TextInputEditText nameEditText;
    private TextInputEditText phoneEditText;
    private Button signUpButton;
    private String email;
    private String password;

    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        nameEditText = (TextInputEditText) findViewById(R.id.nameField);
        phoneEditText = (TextInputEditText) findViewById(R.id.phoneField);
        emailEditText = (TextInputEditText) findViewById(R.id.emailField);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordField);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                name = nameEditText.getText().toString();
                phone = phoneEditText.getText().toString();

                //Toast.makeText(getBaseContext(),"email: "+email+"password: "+password,Toast.LENGTH_SHORT).show();

//                new JsonTask().execute(Constants.BASE_URL + "api/login","POST",email,password);

                EncryptedPrefsUtil.saveString("email",email);
                EncryptedPrefsUtil.saveString("password",password);

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("email", email);
                    requestBody.put("name", name);
                    requestBody.put("phone", phone);
                    requestBody.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NetworkUtils.
                        fetchDataPost("signup",
                                "", requestBody,SignupActivity.this,
                                SignupActivity.this);

            }
        });

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {

        try {

            String result = responseBody.string();
            System.out.println("Result: "+result);

            JSONObject jsonObject = null;

            jsonObject = new JSONObject(result);


            if (
                    jsonObject.has("message") &&
                            jsonObject.get("message") instanceof String &&
                            jsonObject.has("status") &&
                            jsonObject.get("status") instanceof String
            ) {
                String message = jsonObject.getString("message");
                String status = jsonObject.getString("status");

                if (status.equals("success")) {
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    EncryptedPrefsUtil.saveString("authToken", jsonObject.getString("authToken"));
                    EncryptedPrefsUtil.saveString("userId", jsonObject.getString("userId"));
                    EncryptedPrefsUtil.saveString("email", email);
                    EncryptedPrefsUtil.saveString("password",password);

//                    long currentTimeMillis = System.currentTimeMillis();
//                    long expiryMillis = Constants.EXPIRY_MILLIS+currentTimeMillis;

//                    EncryptedPrefsUtil.saveString("tokenExpiry", expiryMillis+"");




                    startActivity(intent);

                } else if(status.equals("failed")) {
//                    Toasts.toastIconError(LoginActivity.this, message);
                    Toast.makeText(SignupActivity.this,message,Toast.LENGTH_SHORT).show();

                } else{

                    Toast.makeText(SignupActivity.this,"Signup Failed! Please Try Again",Toast.LENGTH_SHORT).show();

//                FirebaseCrashlytics.getInstance().recordException(new Exception("Login  Error: "+result));
                }
            }else{

                Toast.makeText(SignupActivity.this,"Signup Failed! Please Try Again",Toast.LENGTH_SHORT).show();

//            FirebaseCrashlytics.getInstance().recordException(new Exception("Login Error JSON Object: "+result));

            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onComplete() {

    }
}