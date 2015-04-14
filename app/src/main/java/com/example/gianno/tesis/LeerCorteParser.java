package com.example.gianno.tesis;

import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;

import com.twitter.sdk.android.core.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gianno on 08/04/2015.
 */
public class LeerCorteParser {

    public List<Corte> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

   try {
        return leerCorteArray(reader);
    }
   finally {
       reader.close();
   }
    }

    public List<Corte> leerCorteArray(JsonReader reader) throws IOException {
        List cortes = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            cortes.add(leerCorte(reader));
        }
        reader.endArray();
        return cortes;
    }

    public Corte leerCorte(JsonReader reader) throws IOException {
         int idCorte=0;
         double latitud=0;
        double longitud=0;
        String fechaCreacion=null;
         String fechaInicio=null;
         String fechaFin=null;
         String descripcion = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("idCorte")) {
                idCorte = reader.nextInt();
            } else if (name.equals("Latitud")) {
                latitud = reader.nextDouble();
            }  else if (name.equals("Longitud")) {
                    longitud = reader.nextDouble();
            }  else if (name.equals("FechaCreacion")) {
                fechaCreacion = reader.nextString();
            }  else if (name.equals("FechaInicio")) {
                fechaInicio = reader.nextString();
            }  else if (name.equals("FechaFin")) {
                fechaFin = reader.nextString();
            }  else if (name.equals("Descripcion")) {
                descripcion = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Corte(idCorte,latitud,longitud,fechaCreacion,fechaInicio,fechaFin,descripcion);
    }

    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    public User readUser(JsonReader reader) throws IOException {
        String username = null;
        int followersCount = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                username = reader.nextString();
            } else if (name.equals("followers_count")) {
                followersCount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        //return new User(username, followersCount);
        return null;
    }}


