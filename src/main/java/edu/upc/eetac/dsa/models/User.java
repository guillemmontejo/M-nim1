package edu.upc.eetac.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String id;
    private String nombre;
    private String apellido;
    private List<Objeto> listaObjetos = null; //Lista de objetos de cada usuario

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }


    //Constructores diferentes
    public User() {
        //usuario vacío
    }
    public User(String id, String nombre, String apellido){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.listaObjetos = new LinkedList<>();
    }
    public User(String id, String nombre, String apellido, List<Objeto> listaObjetos){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.listaObjetos = listaObjetos;
    }
    public User(String id, String nombre,String apellido, Objeto objeto){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.listaObjetos = new LinkedList<>();
        this.listaObjetos.add(objeto);
    }


    //Consutlar número de objetos de un usuario
    public int getNumObjects(){
        return this.listaObjetos.size();
    }

    //Añadir objeto a un usuario
    public int addObjeto(Objeto objeto){
        try{
            this.listaObjetos.add(objeto);
        }
        catch (ExceptionInInitializerError e)
        {
            return 400;//400 Bad Request
        }
        catch (IndexOutOfBoundsException e){
            return 507;//Insufficient storage
        }
        return 201;//201 Created
    }

    //Consultar objeto en la lista con index
    public Objeto getObjeto(int index){
        try{
            return this.listaObjetos.get(index);
        }
        catch (IndexOutOfBoundsException | ExceptionInInitializerError e){
            return null;
        }
    }

    //Consultar lista de objetos de un usuario
    public List<Objeto> getListaObjetos() {
        return listaObjetos;
    }

    //Setter de una lista de objetos para el usuario
    public void setListaObjetos(List<Objeto> listaObjetos) {
        this.listaObjetos = listaObjetos;
    }

    //Añadir nueva lista de objetos a una lista de objetos existente en el usuario
    public int setListaObjetos_resCode(List<Objeto> listaObjetos){
        try{
            this.listaObjetos.addAll(listaObjetos);
        }
        catch(NullPointerException e){
            return 204;//204 No Content
        }
        catch( IndexOutOfBoundsException e){
            return 400;//400 Bad Request
        }
        return 201;//201 Created
    }

    //To string method
    public String toString(){
        return "ID: " + this.getId() + " |Name: " + this.getNombre() + " |Surname: " + this.getApellido();
    }

}
