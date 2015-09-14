package com.nicolasrozassepulveda.nicolas.apppruebametodosgetpost;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ConectandoBD extends AsyncTask<Void, Integer, Boolean> {



    @Override
    protected Boolean doInBackground(Void... voids) {
        if(ConnectionDetect.isNetworkAvailable()){
            try{

                String urlUser = MainActivity.URL_BD_REMOTA +"login/"+MainActivity.correo+"/"+MainActivity.pasEncriptada; //contraseña debe ser encriptada con MD5
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
    return false;

}
