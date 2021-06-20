package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginTag";
    //views
    ImageView img_royalcarribean;
    Button btn_login;
    EditText et_email,et_password;
    TextView tv_signup;
    TextView tv_forgotpassword;

    //
    Boolean isOnForgotPasswordMode;

    //firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_signup = findViewById(R.id.tv_signup);
        tv_forgotpassword = findViewById(R.id.tv_forgotpassword);

        isOnForgotPasswordMode = false;

        btn_login.setOnClickListener(this);
        img_royalcarribean.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_forgotpassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }else if(view == btn_login)
        {
            if(!isOnForgotPasswordMode) {
                if (!et_email.getText().toString().equals("") &&
                        !et_password.getText().toString().equals("")) {
                    signIn(et_email.getText().toString(), et_password.getText().toString());
                } else {
                    Toast.makeText(this, "you didn't fill all of the fields...", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(!et_email.getText().toString().equals(""))
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(et_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "mail sent successfully.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "mail hasn't sent, please contact the developer.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "please write an email.", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(view == tv_signup)
        {
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            intent.putExtra("role",getIntent().getExtras().getString("role"));
            startActivity(intent);
        }
        else if(view == tv_forgotpassword)
        {
            if(!isOnForgotPasswordMode) {
                isOnForgotPasswordMode = true;
                et_password.setVisibility(View.GONE);
                findViewById(R.id.tv_password).setVisibility(View.INVISIBLE);
                btn_login.setText("Send Reset Pass");
                tv_forgotpassword.setText("Login");
            }else{
                isOnForgotPasswordMode = false;
                et_password.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_password).setVisibility(View.VISIBLE);
                btn_login.setText("Log In");
                tv_forgotpassword.setText("Forgot Password?");
            }
        }
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            db.getReference(getIntent().getExtras().getString("role")+"s/").removeEventListener(childEventListener);

            Log.d(TAG, "snapshot:"+snapshot.toString());
            User user = snapshot.getValue(User.class);
            SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear().apply();
            editor.putString("fullName", user.fullName);
            editor.putString("email", user.email);
            editor.putString("idNumber", user.idNumber);
            editor.putString("Online", user.Online);
            editor.putString("password", user.password);
            editor.putString("role", user.role);
            editor.commit();
            Toast.makeText(LoginActivity.this,
                    "welcome "+user.fullName, Toast.LENGTH_LONG).show();
            if(getIntent().getExtras().getString("role").equals("Client")) {
                finishAffinity();
                startActivity(new Intent(LoginActivity.this, MenuClientActivity.class));
            }else{
                finishAffinity();
                startActivity(new Intent(LoginActivity.this, MenuAgentActivity.class));
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    FirebaseDatabase db;


    private void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            User user = new User();
                            db = FirebaseDatabase.getInstance();
                            db.getReference(getIntent().getExtras().getString("role")+"s/").orderByChild("email")
                                    .equalTo(currentUser.getEmail()).limitToFirst(1).addChildEventListener(childEventListener);


                                    /*.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Log.d(TAG, "test:"+task);
                                    Log.d(TAG, "test2:"+task.getResult());
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if(task.isSuccessful())
                                    {

                                    }
                                }
                            });

                                     */



                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "email or password isn't correct...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}