package com.example.sergio.gijondomindividual.dummy;

import java.util.HashMap;

/**
 * Created by Sergio on 01/02/2018.
 */

public class Lugar extends HashMap<String, Object> {
    public Lugar(String imagen, String nombre, String categoria,String descripcion,String web,String direccion) {
        this.put("foto", imagen);
        this.put("nombre", nombre);
        this.put("categoria", categoria);
        this.put("descripcion", descripcion);
        this.put("web", web);
        this.put("direccion", direccion);

    }
    public static String[] getClaves(){
        String[] claves={"foto","nombre","categoria","descripcion","web","direccion"};
        return claves;
    }
}
