package com.nicolasrozassepulveda.nicolas.ejemplo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final TextView mon = (TextView) findViewById(R.id.montar);
        final EditText nombre = (EditText) findViewById(R.id.editTextUsuario);
        final EditText contraseña = (EditText) findViewById(R.id.editTextCon);
        final Button ingresar = (Button) findViewById(R.id.buttonIngresar);
        final Button ingresados = (Button)findViewById(R.id.buttonIngresados);


        ingresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            tarea1 tarea = new tarea1();

                            if(nombre !=null && contraseña!=null) {
                                tarea.execute(mon, nombre, contraseña);
                                ingresar.setVisibility(View.INVISIBLE);
                                ingresados.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "ya puedes ingresar", Toast.LENGTH_SHORT).show(); //genera notificación
                            }else{
                                Toast.makeText(getApplicationContext(), "Ingresa bien los datos", Toast.LENGTH_SHORT).show(); //genera notificación
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );


        ingresados.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String resultado = mon.getText().toString();

                if(resultado.equals("fallo")) {
                    ingresar.setVisibility(View.VISIBLE);
                    ingresados.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Los datos ingresados no son válidos", Toast.LENGTH_SHORT).show(); //genera notificación


                } else if (resultado.equals("true")) {
                    ingresar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "felicidades tus datos son válidos", Toast.LENGTH_SHORT).show(); //genera notificación


                    Intent i = new Intent(MainActivity.this, reciente.class);
                    i.putExtra("correo", nombre.getText().toString());
                    startActivity(i);

                }
            }

        });


    }

// tareas en segundo plano

    class tarea1 extends AsyncTask<TextView, Void, String> {
        TextView texto;
        private final char[] CONSTS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        public String URL_BD_REMOTA = "http://bitphoto-tbd2015.rhcloud.com/bitphoto/";

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(TextView... params) {
            this.texto = params[0];
            String resultado = "fallo";
            return getDatos(params[1], params[2]);
        }

        public String getDatos(TextView nombre, TextView contraseña) {
            String resultado = "fallo";
            try {
                String contra = encriptaEnMD5(contraseña.getText().toString());
                String urlUser = URL_BD_REMOTA + "login/" + nombre.getText().toString() + "/" + contra; //contraseña debe ser encriptada con MD5
                URL url = new URL(urlUser);
                URLConnection uc = url.openConnection();
                uc.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String linea = br.readLine();

                if (!linea.equals("")) {
                    try {
                        JSONObject json = new JSONObject(linea);
                        String usuario = json.getString("success");

                        if (usuario.equals("true")) {
                            return "true";
                        } else {
                            return resultado;
                        }

                    } catch (JSONException e) {
                        return resultado;
                    }
                }

                br.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } finally {
            }
            return resultado;
        }

        public String encriptaEnMD5(String stringAEncriptar) {
            try {

                MessageDigest msgd = MessageDigest.getInstance("MD5");
                byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
                StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
                for (int i = 0; i < bytes.length; i++) {
                    int bajo = (int) (bytes[i] & 0x0f);
                    int alto = (int) ((bytes[i] & 0xf0) >> 4);
                    strbCadenaMD5.append(CONSTS_HEX[alto]);
                    strbCadenaMD5.append(CONSTS_HEX[bajo]);
                }
                return strbCadenaMD5.toString();
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            texto.setText(result);
        }


// tareas en segundo plano



    }

}