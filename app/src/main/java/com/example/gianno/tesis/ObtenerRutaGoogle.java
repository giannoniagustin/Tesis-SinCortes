package com.example.gianno.tesis;

import android.graphics.Color;
import android.os.AsyncTask;

import com.example.gianno.tesis.Peticiones.Post;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Soledad on 15/04/2015.
 */
public class ObtenerRutaGoogle extends AsyncTask <HashMap<String,Object>,Void,List<List<HashMap<String, String>>>> {


    GoogleMap mMap;
    @Override
    protected  List<List<HashMap<String, String>>> doInBackground(HashMap<String,Object>... params) {
        try {
            HashMap<String,Object> Parametros=params[0];
            mMap=(GoogleMap)Parametros.get("mMap");
            // List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            HttpEntity entity = Post.calcularRuta(/*(List<NameValuePair>)Parametros.get("ParametrosPost")*/);
            LeerRutaParser parserRuta= new LeerRutaParser();
            return parserRuta.readJsonStream(entity.getContent());
        } catch (Exception e) {

            return null;
        }
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        super.onPostExecute(result);
        LatLng center = null;
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        //setUpMapIfNeeded();

        // recorriendo todas las rutas
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List<HashMap<String, String>> path = result.get(i);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilineas
            lineOptions.width(4);
            //Definimos el color de la Polilineas
            lineOptions.color(Color.RED);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);
        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 13));                     VER
    }
}

