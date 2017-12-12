package com.example.a507.lej;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.myapplication.R;

import java.util.ArrayList;
import java.util.Locale;

public class Recog extends AppCompatActivity implements TextToSpeech.OnInitListener {

    protected Button btRecog, btTts;
    protected EditText etTts;
    protected TextToSpeech tts;
    protected ArrayList<String> arName, arPhoneNum;
    protected final int nNameSize = 3;
    protected static final int RECOG_CODE = 1234;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RECOG_CODE) {
                ArrayList<String> arStr = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String sRecog = arStr.get(0);
                Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                int nPos = arName.indexOf(sRecog);

                if (nPos == -1) {
                    tts.speak(sRecog + "은 없는 이름입니다", TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    String sPhoneNum = arPhoneNum.get(nPos);
                    Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sPhoneNum));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog);
        btRecog = (Button) findViewById(R.id.btRecog);
        btRecog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성 인식중.");
                startActivityForResult(intent, RECOG_CODE);
            }
        });

        tts = new TextToSpeech(this, this);

        arName = new ArrayList<String>(nNameSize);
        arPhoneNum = new ArrayList<String>(nNameSize);
        arName.add("aaa"); arPhoneNum.add("0");
        arName.add("bbb"); arPhoneNum.add("0");
        arName.add("ccc"); arPhoneNum.add("0");
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(0.5f);
            tts.setSpeechRate(1.0f);
        }

    }



}