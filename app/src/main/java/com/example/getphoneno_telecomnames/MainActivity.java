package com.example.getphoneno_telecomnames;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {
    TextView textView,simData;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.phoneNumber);
        simData = findViewById(R.id.simData);
        getPhoneNumbers();
        SimsName();
    }
    public void getPhoneNumbers(){
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            TelephonyManager tMgr = (TelephonyManager)
                    this.getSystemService(Context.TELEPHONY_SERVICE);
            String MyPhoneNumber = "";
            try
            {
                MyPhoneNumber =tMgr.getLine1Number();
                SimsName();
                textView.setText("Known Number:"+MyPhoneNumber);
            }
            catch(NullPointerException ex)
            {
            }

            if(MyPhoneNumber.equals("")){
                MyPhoneNumber = tMgr.getSubscriberId();
                textView.setText("Not Available"+MyPhoneNumber);
            }

        } else {
            requestPermission();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
            textView.setText("Request Permission:");
        }
    }
    public void SimsName() {
        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                return;
            }
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                //if there are two sims in dual sim mobile
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    return;
                }
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(1);

                final String sim1 = simInfo.getDisplayName().toString();
                final String sim2 = simInfo1.getDisplayName().toString();
                simData.setText("Sim 1:"+sim1+""+"\nSim 2:"+sim2);

            } else {
                //if there is 1 sim in dual sim mobile
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);
                final String sim1 = simInfo.getDisplayName().toString();
                TelephonyManager tManager = (TelephonyManager) getBaseContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String userPhone = tManager.getLine1Number();
                Toast.makeText(this, userPhone, Toast.LENGTH_SHORT).show();
                textView.setText("sim 1 in dual mobile:\n"+sim1);

            }
        }else{
            //below android version 22
            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String sim1 = tManager.getNetworkOperatorName();
            Toast.makeText(this, "version 2", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                                PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                textView.setText(mPhoneNumber);
                break;
        }
    }
}