package com.nicolasrozassepulveda.nicolas.bitphoto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String URL_INFO_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mail = (EditText)findViewById(R.id.editTextCorreoLogin);
        final EditText pass = (EditText)findViewById(R.id.editTextContraseñaLogin);
        final String corre = "n"; //despues tienen que ser extraidos de la BD
        final String password = "asd"; // despues tienen que ser extraidos de la BD
        final String errorCorreo = "Error al ingresar su correo";
        final String errorPass = "Error al ingresar su contraseña";
        Button botonIngresar = (Button) findViewById(R.id.buttonLogin);

        botonIngresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, Perfil.class);
                        String pas = pass.getText().toString();
                        String correo = mail.getText().toString();
                        if(corre.equals(correo)){
                            if(password.equals(pas)){
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), errorPass, Toast.LENGTH_SHORT).show(); //genera notificación
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), errorCorreo, Toast.LENGTH_SHORT).show(); //genera notificación
                        }

                    }
                }
        );
    }

    private void getInfo(){}

}
