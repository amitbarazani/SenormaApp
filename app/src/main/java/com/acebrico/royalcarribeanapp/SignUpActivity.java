package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //views
    ImageView img_royalcarribean;
    EditText et_email,et_password,et_fullname,et_id;
    Button btn_signUp;
    TextView tv_signin;
    //firebase
    FirebaseAuth mAuth;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_signUp = findViewById(R.id.btn_signup);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_id = findViewById(R.id.et_id);
        et_fullname = findViewById(R.id.et_fullname);
        tv_signin = findViewById(R.id.tv_signin);
        role = getIntent().getExtras().getString("role");

        mAuth = FirebaseAuth.getInstance();

        tv_signin.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        img_royalcarribean.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(view == btn_signUp)
        {
            if (!et_email.getText().toString().equals("")
                    && !et_password.getText().toString().equals("")
                    && !et_fullname.getText().toString().equals("")
                    && !et_id.getText().toString().equals("")) {
                if (isValidEmail(et_email.getText().toString())) {
                    if (isValidPassword(et_password.getText().toString())) {
                        signUp(et_email.getText().toString(),et_password.getText().toString()
                                ,et_id.getText().toString(),et_fullname.getText().toString());
                    } else {
                        Toast.makeText(this, "invalid password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "there are some empty fields", Toast.LENGTH_SHORT).show();
            }
        }else if(view == tv_signin)
        {
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }




    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    /*
     ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=\S+$)          # no whitespace allowed in the entire string
    .{8,}             # anything, at least eight places though
    $                 # end-of-string
     */

    public static boolean isValidPassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=\\S+$).{6,}$")) {
            return false;
        } else {
            return true;
        }
    }



    public void signUp(final String email, final String password, final String ID, final String fullname)
    {

        //prg.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(email,fullname,ID,password,role,"No");

                        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear().apply();
                        editor.putString("fullName",fullname);
                        editor.putString("email", email);
                        editor.putString("idNumber", ID);
                        editor.putString("Online", "No");
                        editor.putString("password", password);
                        editor.putString("role", role);
                        editor.commit();

                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        //updating user in firebase database
                        db.getReference(role+"s/").child(ID).setValue(user);

                        Log.d("TAG", "createUserWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "SignUp worked!",
                                Toast.LENGTH_LONG).show();
                        if(role.equals("Client")) {
                            startActivity(new Intent(SignUpActivity.this, MenuClientActivity.class));
                        }else{
                            startActivity(new Intent(SignUpActivity.this, MenuAgentActivity.class));
                        }
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "createUserWithEmail:failure", e);
                Toast.makeText(SignUpActivity.this, "Sign up didn't work...",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}