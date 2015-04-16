package com.example.gianno.tesis;

import android.os.AsyncTask;

import com.example.gianno.tesis.Peticiones.Post;
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
        try {

            HashMap<String,Object> Parametros=params[0];
            mMap=(GoogleMap)Parametros.get("mMap");
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            HttpEntity entity = Post.calcularDistanciaCorte((List<NameValuePair>)Parametros.get("ParametrosPost"));

            LeerCorteParser parserCorte= new LeerCorteParser();
            return parserCorte.readJsonStream(entity.getContent());

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
