package com.example.gianno.tesis;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gianno on 16/03/2015.
 */
public class ObtenerCortesBD extends AsyncTask <HashMap<String,Object>,Void,List<Corte>>{

    private GoogleMap  mMap;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Corte> doInBackground(HashMap<String,Object>... params) {
        HashMap<String,Object> Parametros=params[0];
        //Obtenemos el hashmap de los parametos
        mMap=(GoogleMap)Parametros.get("mMap");
        String latitud=Double.toString(((LatLng) Parametros.get("PosicionActual")).latitude);
        String longitud=Double.toString(((LatLng) Parametros.get("PosicionActual")).longitude);

        HttpClient cliente = new DefaultHttpClient();
        HttpContext contexto = new BasicHttpContext();
        HttpPost httppost = new HttpPost("http://192.168.0.13:80/tesis/calculodistanciapuntos.php");

        try {
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();

            parametros.add(new BasicNameValuePair("Latitud",latitud ));
            parametros.add(new BasicNameValuePair("Longitud", longitud));
             /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            HttpResponse response = cliente.execute(httppost, contexto);
            HttpEntity entity = response.getEntity();
            LeerCorteParser parserCorte= new LeerCorteParser();
            List<Corte> Cortes =parserCorte.readJsonStream(entity.getContent());
            return Cortes;
            } catch (Exception e) {

            return null;
            }

    }

    @Override
    protected void onPostExecute(List<Corte> Cortes) {

        super.onPostExecute(Cortes);

        for (Corte c:Cortes)
        {
            LatLng latLng = new LatLng(c.getLatitud(), c.getLongitud());
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(c.getDescripcion());
            mMap.addMarker(options);

        }

    }
}
