package sachdeva.saksham.medrescue;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Welcome_Activity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    private Button mdriver, mCustomer;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        startService(new Intent(Welcome_Activity.this,onAppKilled.class));

        handler.postDelayed(runnable, 1000); //2000 is the timeout for the splash
    }

    public void driver(View v) {
        Toast.makeText(this,"I am a driver",Toast.LENGTH_SHORT).show();
    }

    public void patient(View v) {
        Toast.makeText(this,"I am a Patient",Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(Welcome_Activity.this, CustomerLoginActivity.class);
        startActivity(intent);
    }
    public void sendMessage2(View view)
    {
        Intent intent = new Intent(Welcome_Activity.this, DriverLoginActivity.class);
        startActivity(intent);
    }
    }
