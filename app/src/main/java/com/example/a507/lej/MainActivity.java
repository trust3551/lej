package com.example.a507.lej;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.myapplication.R;

public class MainActivity extends AppCompatActivity {
    protected Button btContact, btSms2, btTts;
    private static final int CODE_CONTACT = 1234;


    protected String getNameFromPhoneNum(String sNum) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(sNum));
        String sFilter[] = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, sFilter, null, null, null);
        String sName = "";
        if (cursor != null && cursor.moveToFirst()) {
            sName = cursor.getString(0);
        }
        return sName;
    }

    protected String getPhoneNumFromName(String sName) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(sName));
        String[] arProjection = new String[]{ContactsContract.Contacts._ID};
        Cursor cursor = getContentResolver().query(uri, arProjection, null, null, null);
        String sPhoneNum = "";
        if (cursor != null && cursor.moveToFirst()) {
            String sId = cursor.getString(0);
            String[] arProjNum = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            String sWhereNum = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
            String[] sWhereNumParam = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, sId};
            Cursor cursorNum = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    arProjNum, sWhereNum, sWhereNumParam, null);
            if (cursorNum != null && cursorNum.moveToFirst()) {
                sPhoneNum = cursorNum.getString(0);
            }
            cursorNum.close();
        }
        cursor.close();
        return sPhoneNum;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CONTACT && resultCode == RESULT_OK) {
            String sFilter[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(data.getData(), sFilter, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String sName = cursor.getString(0);
                String sNum = cursor.getString(1);
                Toast.makeText(this, sName + " = " + sNum, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btContact = (Button) findViewById(R.id.btContact);
        btContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, CODE_CONTACT);
            }
        });

        btSms2 = (Button)findViewById(R.id.btSms2);
        btSms2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SMS.class);
                startActivity(intent);
            }
        });
        btTts = (Button)findViewById(R.id.btTts);
        btTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Recog.class);
                startActivity(intent);
            }
        });

    }


}

