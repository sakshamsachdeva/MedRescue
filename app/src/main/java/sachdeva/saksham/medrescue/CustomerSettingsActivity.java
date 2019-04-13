package sachdeva.saksham.medrescue;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CustomerSettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField;

    private Button mBack, mConfirm;
    private AwesomeValidation awesomeValidation;
    private static final Pattern Name =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    ".{1,}" +               //at least 1 characters
                    "$");
    private static final Pattern Phone =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    ".{10,}" +               //at least 1 characters
                    "$");
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;

    private String userID;
    private String mName, mPhone;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        mNameField = (EditText) findViewById(R.id.name);
        awesomeValidation.addValidation(this, R.id.name, Name, R.string.error_field_required);

        mPhoneField = (EditText) findViewById(R.id.phone);
        awesomeValidation.addValidation(this, R.id.phone, Phone, R.string.error_invalid_no);

        mBack = (Button) findViewById(R.id.back);
        mConfirm = (Button) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    saveUserInformation();
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Name") != null) {
                        mName = map.get("Name").toString();
                        mNameField.setText(mName);

                    }

                    if (map.get("Phone") != null) {
                        mPhone = map.get("Phone").toString();
                        mPhoneField.setText(mPhone);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    private void saveUserInformation() {
        mName = mNameField.getText().toString();
        mPhone = mPhoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("Name", mName);
        userInfo.put("Phone", mPhone);
        mCustomerDatabase.updateChildren(userInfo);

        finish();
    }
}

