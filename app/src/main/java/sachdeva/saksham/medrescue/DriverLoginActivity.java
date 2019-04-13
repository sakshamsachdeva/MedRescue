package sachdeva.saksham.medrescue;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class DriverLoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button mLogin, mRegistration,mForgetPassword;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null && mAuth.getCurrentUser().isEmailVerified()){
                    Intent intent = new Intent(DriverLoginActivity.this, DriverMapActivity.class);
                    Toast.makeText(DriverLoginActivity.this, "Welcome to Med Rescue", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }

            }
        };
        mForgetPassword = findViewById(R.id.forgetPassword);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverLoginActivity.this , ResetPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });

        mEmail= findViewById(R.id.email);
        mPassword=  findViewById(R.id.password);

        mLogin= findViewById(R.id.login);
        mRegistration= findViewById(R.id.registration);


        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverLoginActivity.this,DriverSignup.class);
                startActivity(intent);
                return;
            }
        });


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString();
                final String password =mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(DriverLoginActivity.this, "Driver Sign In Error", Toast.LENGTH_SHORT).show();
                        }else{

                            if(mAuth.getCurrentUser().isEmailVerified()){
                                String user_id= mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                current_user_db.setValue(true);

                            }else{
                                Toast.makeText(DriverLoginActivity.this, "Please, verify your email.", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }}
