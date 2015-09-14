package com.nicolasrozassepulveda.nicolas.apppruebametodosgetpost;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static String URL_BD_REMOTA = "http://bitphoto-tbd2015.rhcloud.com:8080/bitphoto";
    public static String pasEncriptada, correo;
    private static final char[] CONSTS_HEX = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
    private Context mContext = this;
    public static final Boolean debug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mail = (EditText)findViewById(R.id.editTextCorreoLogin);
        final EditText pass = (EditText)findViewById(R.id.editTextContraseñaLogin);
        final String error = "Error al ingresar su correo o contraseña";
        Button botonIngresar = (Button) findViewById(R.id.buttonLogin);

        botonIngresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, Perfil.class);
                        pasEncriptada = encriptaEnMD5(pass.getText().toString());
                        correo = mail.getText().toString();

                        if(ConnectionDetect.isNetworkAvailable(mContext)){

                            boolean info = getInfo(correo , pasEncriptada);
                            if (info) {
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show(); //genera notificación
                            }

                        }
                        Toast.makeText(getApplicationContext(), "no hay conexión a internet", Toast.LENGTH_SHORT).show(); //genera notificación



                    }
                }
        );
    }


    private boolean getInfo(String correo, String contraseña){ // hacer eso en doInBackground para que esto se haga en internet

        try{

            String urlUser = URL_BD_REMOTA +"login/"+correo+"/"+contraseña; //contraseña debe ser encriptada con MD5
            URL url = new URL(urlUser);
            URLConnection uc = url.openConnection();
            uc.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String linea = br.readLine();

            if(!linea.equals("")){
                try {
                    JSONObject json = new JSONObject(linea);
                    String usuario = json.getString("success");

                    if (usuario.equals("true"))
                    {
                        return true;
                    }
                    else{
                        return false;
                    }

                } catch (JSONException e) {
                    return false;
                }
            }

            br.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
        }
        return false;
    }

    public static String encriptaEnMD5(String stringAEncriptar)
    {
        try
        {
            MessageDigest msgd = MessageDigest.getInstance("MD5");
            byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
            StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++)
            {
                int bajo = (int)(bytes[i] & 0x0f);
                int alto = (int)((bytes[i] & 0xf0) >> 4);
                strbCadenaMD5.append(CONSTS_HEX[alto]);
                strbCadenaMD5.append(CONSTS_HEX[bajo]);
            }
            return strbCadenaMD5.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

