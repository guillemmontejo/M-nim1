package edu.upc.eetac.dsa.models;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Objeto {

    String nombre ;
    String id;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Constructor sin inicializacion
    public Objeto(){
        //Objeto vacío
    }
    //Objeto inicializado con nombre e id
    public Objeto (String nombre, String id){
    this.id = id;
    this.nombre = nombre;
    }

    public String toString() {
        return "Id: " + this.getId() + " |Name: " + this.getNombre();
    }
}
