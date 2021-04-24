package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginTag";
    //views
    ImageView img_royalcarribean;
    Button btn_login;
    EditText et_email,et_password;

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



        btn_login.setOnClickListener(this);
        img_royalcarribean.setOnClickListener(this);

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
            if(!et_email.getText().toString().equals("") &&
                    !et_password.getText().toString().equals(""))
            {
                signIn(et_email.getText().toString(),et_password.getText().toString());
            }else{
                Toast.makeText(this, "לא מילאתם את כל השדות", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "ברוך הבא "+user.getEmail(), Toast.LENGTH_LONG).show();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        Log.d(TAG, "doc snap:"+task.getResult());
                                        Log.d(TAG, "doc snap username:"+task.getResult().get("username"));
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        String username = documentSnapshot.get("username").toString();
                                        //^^^^^^^?????what to do with username???????
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "האימייל או הסיסמא לא נכונים",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}