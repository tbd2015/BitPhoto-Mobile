package com.nicolasrozassepulveda.nicolas.apppruebametodosgetpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Button botonInicio = (Button) findViewById(R.id.buttonFotosDe);
        botonInicio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Perfil.this, Fotos_de.class);
                        startActivity(i);
                    }
                }
        );
    }


}