package org.dbarrera.cardmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dav3 on 5/23/13.
 */
public class CardHelp extends Activity implements View.OnClickListener {

    ImageButton sendmail;
    TextView device_model, device_version, device_sdk;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_help);
        setupWidgets();
    }

    private void setupWidgets() {
        // TODO Auto-generated method stub
        sendmail = (ImageButton)findViewById(R.id.imgbtn_email);
        sendmail.setOnClickListener(this);
        device_model = (TextView)findViewById(R.id.lbl_device_model);
        device_model.setText("" + device_model.getText().toString() + " " + Build.MODEL.toString());
        device_version = (TextView)findViewById(R.id.lbl_device_version);
        device_version.setText("" + device_version.getText().toString() + " " + Build.VERSION.RELEASE.toString());
        device_sdk = (TextView)findViewById(R.id.lbl_device_sdk);
        device_sdk.setText("" + device_sdk.getText().toString() + " " + Build.VERSION.SDK_INT);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.imgbtn_email){
            Toast.makeText(getApplicationContext(), "Favor no eliminar las lineas del cuerpo del correo. Escribir adicional a esto.", Toast.LENGTH_LONG).show();
            String emailBody = "Device: " + Build.MODEL.toString() + "\nVersion: " + Build.VERSION.RELEASE.toString() + "\nAPI: " + Build.VERSION.SDK_INT + "\n";
            sendMail(emailBody);
        }
    }

    private void sendMail(String emailBody) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"dbarrer@uees.edu.ec"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Acerca de Card Manager");
        i.putExtra(Intent.EXTRA_TEXT   , "" + emailBody);
        try {
            startActivity(Intent.createChooser(i, "Enviar correo via..."));
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "No hay clientes de correo instalados.", Toast.LENGTH_SHORT).show();
        }
    }
}