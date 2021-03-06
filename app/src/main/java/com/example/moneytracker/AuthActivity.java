package com.example.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private Button btnSignIn, btnSignUp, btnStart, btnLogOut;
    private EditText eMail, password;
    private TextView startTextView;
   // public static String UID;
    private  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        init();
        // ======= SIGN IN===============
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(eMail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(eMail.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                       // showUnsigned();
                                        Toast.makeText(AuthActivity.this, "Проверьте логин и пароль, а также подключение к сети",
                                                Toast.LENGTH_SHORT).show();
   //TODO При отсутствии интернета также выдаёт этот тост. Требуется обрабатывать все случаи, когда не удается войти
                                    }
                                }
                            });
                }
            }
        });
        // ============== SIGN UP ============
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(eMail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(eMail.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                 //   showSigned();
                                    sendEmailVerification();
                                    Toast.makeText(AuthActivity.this, "Верификация отправлена. Проверьте почту",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                 //   showUnsigned();
                                    Toast.makeText(AuthActivity.this, "Ошибка. Проверьте правильность почты и пароля",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        // =========== START BUTTON ===============
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//        // =========== LOG OUT BUTTON ===============
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.getInstance().signOut();
//                showUnsigned();
//                eMail.setText("");
//                password.setText("");
//            }
//        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null && currentUser.isEmailVerified()) {
//            showSigned();
//        } else {
//            showUnsigned();
//            //  Toast.makeText(this, "USER NULL", Toast.LENGTH_SHORT).show();
//        }
//    }
//
    private void sendEmailVerification() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, "Проверьте свой e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthActivity.this, "Ошибка. Проверьте почту заново, также папку спама", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public static boolean isAuthorized() {
//        return mAuth.getCurrentUser() != null? true: false;
//    }

//    private void showSigned() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        assert user != null;
//        if (user.isEmailVerified()) {
//            startTextView.setText("Вы вошли как: " + user.getEmail());
//         //   UID = user.getUid();
//            btnSignIn.setVisibility(View.GONE);
//            btnSignUp.setVisibility(View.GONE);
//            eMail.setVisibility(View.GONE);
//            password.setVisibility(View.GONE);
//            btnStart.setVisibility(View.VISIBLE);
//            btnLogOut.setVisibility(View.VISIBLE);
//
//        } else {
//            Toast.makeText(AuthActivity.this, "Проверьте почту", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void showUnsigned() {
//        startTextView.setText("UNSIGNED");
//        btnSignIn.setVisibility(View.VISIBLE);
//        btnSignUp.setVisibility(View.VISIBLE);
//        eMail.setVisibility(View.VISIBLE);
//        password.setVisibility(View.VISIBLE);
////        btnStart.setVisibility(View.GONE);
////        btnLogOut.setVisibility(View.GONE);
//        ItemsAdapter.fireBaseMaxId = 0;
//    }

    private void init() {
        btnSignIn = findViewById(R.id.signInButton);
        btnSignUp = findViewById(R.id.signUpButton);
//        btnStart = findViewById(R.id.startButton);
//        btnLogOut = findViewById(R.id.logOutButton);
        eMail = findViewById(R.id.eMailEditText);
        startTextView = findViewById(R.id.startTextView);
        password = findViewById(R.id.passwordEditText);
        mAuth = FirebaseAuth.getInstance();
    }
}
