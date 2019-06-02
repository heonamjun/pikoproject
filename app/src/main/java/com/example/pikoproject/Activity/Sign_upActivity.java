package com.example.pikoproject.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pikoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_upActivity extends AppCompatActivity {
    private static final String TAG = "Sing_upActivty";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.createUser).setOnClickListener(oc);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    View.OnClickListener oc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.createUser:
                    singup();
                    break;
            }
        }
    };

    private void singup() {

        String email = ((EditText) findViewById(R.id.edittext_email2)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_passwod2)).getText().toString();
        String passwordcheck = ((EditText) findViewById(R.id.editText_password2_check)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordcheck.length() > 0) {
            if (password.equals(passwordcheck)) {
                final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
                loaderLayout.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loaderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입을 성공하였습니다.");
                                    finish();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                    //updateUI(null);
                                }
                            }
                        });
            } else {
                Toast.makeText(Sign_upActivity.this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }


    }


    private void startToast(String msg){
        Toast.makeText(Sign_upActivity.this,msg,Toast.LENGTH_SHORT).show();
    }


}