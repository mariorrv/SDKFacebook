package org.example.sdkfacebook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AdView adView;
    private ImageView fb_image;
    private Button exitButton;
    private TextView txt_userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        getFbKeyHash("0zMiK1BfmligxfVVIkgzqwBdirk=");

        setContentView(R.layout.activity_main);

        adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        fb_image = (ImageView) findViewById(R.id.fb_image);
        fb_image.setVisibility(View.INVISIBLE);

        txt_userinfo = (TextView) findViewById(R.id.txt_userinfo);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Inicio session Todo gut!!", Toast.LENGTH_SHORT).show();
                fb_image.setVisibility(View.VISIBLE);
                txt_userinfo.setText("ID usuario: " + loginResult.getAccessToken().getUserId() + "\n" +
                "Auth Token: " + loginResult.getAccessToken().getToken() + "\n" +
                "Pemisos: " + loginResult.getAccessToken().getPermissions() + "\n" +
                "ID del App: " + loginResult.getAccessToken().getApplicationId());
                loginButton.setVisibility(View.INVISIBLE);
                exitButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Inicio session Cancelado", Toast.LENGTH_SHORT).show();
                fb_image.setVisibility(View.INVISIBLE);
                txt_userinfo.setText("");
                loginButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Inicio session Error", Toast.LENGTH_SHORT).show();
                fb_image.setVisibility(View.INVISIBLE);
                txt_userinfo.setText("");
                loginButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.INVISIBLE);
            }
        });

        exitButton = (Button) findViewById(R.id.btn_logout);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                fb_image.setVisibility(View.INVISIBLE);
                txt_userinfo.setText("");
                loginButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.INVISIBLE);
            }
        });

        loginButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.INVISIBLE);

    }

    public void getFbKeyHash(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
                System.out.println("KeyHash: " + Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }

        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int reqCode, int resCode, Intent i){
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (adView != null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

}
