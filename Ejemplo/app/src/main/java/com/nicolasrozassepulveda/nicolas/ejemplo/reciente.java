package com.nicolasrozassepulveda.nicolas.ejemplo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class reciente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciente);


        String correo = getIntent().getStringExtra("correo");
        ListView listaFotos = (ListView)findViewById(R.id.listViewImagenes);
        TextView correito = (TextView)findViewById(R.id.textViewCorreo);
        //ejemplo de como mostrar una imagen
        //UrlImageViewHelper.setUrlDrawable(imageView, "http://example.com/image.png");


        tarea2 tarea = new tarea2();
        //tarea.execute(listaFotos);
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tarea.rutas);
        //listaFotos.setAdapter(adaptador);

        correito.setText(correo);
    }

    class tarea2 extends AsyncTask<String, Void, List<String>> { //tipo empezar, tipo durante, tipo final
        String texto;
        String[] rutas=null;

        public String URL_BD_REMOTA = "http://bitphoto-tbd2015.rhcloud.com/bitphoto/";

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected List<String> doInBackground(String... params) { //tipo empezar //retorna tipo final
            this.texto = params[0];
            String resultado = "fallo";
            return getDatos(params[0]);
        }

        public List<String> getDatos(String correo) {
            List<String> resultado = null;
            try {
                String urlUser = URL_BD_REMOTA + "photo/" + correo + "/5";
                URL url = new URL(urlUser);
                URLConnection uc = url.openConnection();
                uc.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String linea = br.readLine();

                if (!linea.equals("")) {
                    try {
                        JSONObject json = new JSONObject(linea);
                        String usuario = json.getString("success");
                        JSONArray jsonArray = json.getJSONArray("photo");
                        List<String> rutas =null;
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject photos = jsonArray.getJSONObject(i); // photos
                            //photos.getString("urlserver");
                            rutas.add(photos.getString("urlserver"));

                        }

                        if (usuario.equals("true")) {
                            return rutas;
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

        protected void onPostExecute(List<String> result) { //tipo final retornado por doInBackground
            for(int i =0; i<result.size(); i++){
                rutas[i] = result.get(i).toString();
            }
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reciente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
