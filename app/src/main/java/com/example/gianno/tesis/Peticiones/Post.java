package com.example.gianno.tesis.Peticiones;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gianno on 14/04/2015.
 */
public  class Post {


    final static String urlCalcularDistancia="http://192.168.0.13:80/tesis/calculodistanciapuntos.php";
    //final static String urlCalcularDistancia="http://192.168.1.68/Tesis/calculodistanciapuntos.php";
    final static String urlCalcularRuta="http://192.168.0.13:80/tesis/calculoruta.php";



    public static HttpEntity calcularDistanciaCorte(List<NameValuePair> parametros) {   try {
        HttpPost httppost = new HttpPost(urlCalcularDistancia);
         HttpClient cliente=new DefaultHttpClient();;
         HttpContext contexto=new BasicHttpContext();;



       /* parametros.add(new BasicNameValuePair("Latitud","0" ));
        parametros.add(new BasicNameValuePair("Longitud", "0"));*/

        httppost.setEntity(new UrlEncodedFormEntity(parametros));
        HttpResponse response = cliente.execute(httppost, contexto);
        HttpEntity entity = response.getEntity();
        return  entity;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static HttpEntity calcularRuta(/*List<NameValuePair> parametros*/) {   try {
        HttpPost httppost = new HttpPost(urlCalcularRuta);
        HttpClient cliente=new DefaultHttpClient();
        HttpContext contexto=new BasicHttpContext();



        // httppost.setEntity(new UrlEncodedFormEntity(parametros));
        HttpResponse response = cliente.execute(httppost, contexto);
        HttpEntity entity = response.getEntity();
        return  entity;

    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }

    }
}
