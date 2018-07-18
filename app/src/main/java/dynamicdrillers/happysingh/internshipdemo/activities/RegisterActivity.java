package dynamicdrillers.happysingh.internshipdemo.activities;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login, mainRegister;
    SqlLiteHelper liteHelper;
    SharedprefenceHelper sharedprefenceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        mainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    liteHelper.addUser(new User(null,username.getText().toString(),password.getText().toString()));
                    sharedprefenceHelper.setUserFlag();
                    Toast.makeText(RegisterActivity.this, "User Created..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }
            }
        });


    }


    private boolean validate() {

        if(!username.getText().equals("")){

            if(!password.getText().equals("")){
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
        login = findViewById(R.id.btn_login_main);
        mainRegister = findViewById(R.id.btn_register_main);
        sharedprefenceHelper = SharedprefenceHelper.getInstance(this);
        liteHelper = new SqlLiteHelper(this);
    }
}
