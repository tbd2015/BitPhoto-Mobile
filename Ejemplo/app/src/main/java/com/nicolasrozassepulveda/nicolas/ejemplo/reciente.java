package com.nicolasrozassepulveda.nicolas.ejemplo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
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


        //TextView correito = (TextView)findViewById(R.id.textViewCorreo);

        //correito.setText(correo);

        tarea2 t = new tarea2();
        t.execute(correo);

        //ListView listaURL = (ListView)findViewById(R.id.listViewImagenes);
        ArrayList<String> url = new ArrayList<String>();
        url = t.urls;

        //List<String> listaFotos = t.lista; // acá guardar lista de urlserver de fotos
        //System.out.println(listaFotos.get(0));

        Button anterior = (Button)findViewById(R.id.buttonAnterior);
        Button siguiente = (Button)findViewById(R.id.buttonSiguiente);
        WebView webview=(WebView)findViewById(R.id.webView);
        final WebView webview2=(WebView)findViewById(R.id.webView2);
        WebView webview3=(WebView)findViewById(R.id.webView3);
        WebView webview4=(WebView)findViewById(R.id.webView4);
        WebView webview5=(WebView)findViewById(R.id.webView5);


        final int[] numeroFoto = {1};
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
     }

    class tarea2 extends AsyncTask<String, Void, ArrayList<String>> { //tipo empezar, tipo durante, tipo final

        public String URL_BD_REMOTA = "http://bitphoto-tbd2015.rhcloud.com/bitphoto/";
        public String URL_BD_REMOTA_FOTO = "http://bitphotoweb-tbd2015.rhcloud.com";
        String correo;
        ArrayList<String> urls = new ArrayList<String>();

        WebView webview=(WebView)findViewById(R.id.webView);
        WebView webview2=(WebView)findViewById(R.id.webView2);
        WebView webview3=(WebView)findViewById(R.id.webView3);
        WebView webview4=(WebView)findViewById(R.id.webView4);
        WebView webview5=(WebView)findViewById(R.id.webView5);



        @Override
        protected void onPreExecute() {


        }

        @Override
        protected ArrayList<String> doInBackground(String... params) { //tipo empezar //retorna tipo final
            this.correo = params[0];
            return getDatos(correo);
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public ArrayList<String> getDatos(String correo) {

            String urlUser = URL_BD_REMOTA + "photo/" + correo + "/5";
            String resultado = "fallo";
            try {
                URL url = new URL(urlUser);
                URLConnection uc = url.openConnection();
                uc.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String linea = readAll(br);
                //System.out.println(linea);

                if(!linea.equals("")){
                    try {
                        JSONObject json = new JSONObject(linea);
                        String linea2 = json.getString("photos");
                        //System.out.println("linea 2: "+linea2);

                        JSONObject json2 = new JSONObject(linea2);

                        //leer arreglo acá
                        JSONArray jsonurl = json2.getJSONArray("photo");
                        //System.out.println("llegue!!!");

                        for(int i = 0; i < jsonurl.length(); i++){
                          //  System.out.println("tamaños: "+i);
                            String linea3 = jsonurl.getString(i).toString();
                            JSONObject json3 = new JSONObject(linea3);
                            String uri = json3.getString("urlserver");
                            //System.out.println(uri);
                            urls.add(URL_BD_REMOTA_FOTO+""+uri);
                            System.out.println(urls.size());
                            System.out.println(urls.get(i));
                        }
                    return urls;
                        //terminar de leer arreglo
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                br.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } finally {
            }

            return urls;
        }


        protected void onPostExecute(ArrayList<String> result) { //tipo final retornado por doInBackground
            //for(int i=0; i<result.size(); i++){
                //System.out.println(result.get(i).toString());
                //uris.setText(result.get(i).toString());

                /*probando*/

                    webview.loadUrl(result.get(0).toString());
                    webview2.loadUrl(result.get(1).toString());
                    webview3.loadUrl(result.get(2).toString());
                    webview4.loadUrl(result.get(3).toString());
                    webview5.loadUrl(result.get(4).toString());

                /*probando*/
            //}

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
