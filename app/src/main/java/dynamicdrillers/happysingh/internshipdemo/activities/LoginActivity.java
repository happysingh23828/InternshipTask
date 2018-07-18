package dynamicdrillers.happysingh.internshipdemo.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dynamicdrillers.happysingh.internshipdemo.R;
import dynamicdrillers.happysingh.internshipdemo.SharedprefenceHelper;
import dynamicdrillers.happysingh.internshipdemo.SqlLiteHelper;
import dynamicdrillers.happysingh.internshipdemo.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button mainLogin, register;
    SqlLiteHelper liteHelper;
    SharedprefenceHelper sharedprefenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //clicks on MAin login Button
        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    if(liteHelper.isUserExist(username.getText().toString())){

                        User user   = new User(null,username.getText().toString(),password.getText().toString());
                        if(liteHelper.Authenticate(user)!=null){ // user logged Successfully

                            User userFromDatabase = liteHelper.Authenticate(user);
                            sharedprefenceHelper.setUserFlag();
                            sharedprefenceHelper.setUserDetails(userFromDatabase);
                            Toast.makeText(LoginActivity.this, "Login Successfully ..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Password Wrong...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(LoginActivity.this, "User Does Not Exist..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //cliks on registerButton
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


    }

    private boolean validate() {

        if(!username.getText().toString().equals("")){

            if(!password.getText().toString().equals("")){
                return  true;
            }
            else{
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else{
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void init() {
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        mainLogin = findViewById(R.id.btn_login_main);
        register = findViewById(R.id.btn_register);
        sharedprefenceHelper = SharedprefenceHelper.getInstance(this);
        liteHelper = new SqlLiteHelper(this);
    }
}
