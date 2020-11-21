package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.models.User;
import edu.upc.eetac.dsa.models.Objeto;
import org.apache.log4j.Logger;

import java.util.*;

public class GameManagerImpl implements GameManager {
    //Singleton
    private static GameManager instance;

    //HashMap is same as Dictionary
    private HashMap<String , User> diccionarioUsuarios;
    private List<Objeto> listaObjetos;
    private static Logger log = Logger.getLogger(GameManager.class);

    //Singleton fachada
    public static GameManager getInstance(){
        if(instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }


    //Private Constructor
    private GameManagerImpl(){
        //Singleton Private Constructor
        this.diccionarioUsuarios = new HashMap<>();
        this.listaObjetos = new LinkedList<>();
    }


    //Métodos

    //Listado ordenado alfabéticamente de los Usuarios
    public  List<User> getSortedUsersAlphabetical(){
        if(this.diccionarioUsuarios != null){
            List<User> result = new LinkedList<User>(diccionarioUsuarios.values());
            log.info("List of users before sorting it alphabetically: " + result.toString());

            Collections.sort(result, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    //ToIgnoreCase: To not distinguish between Capital and LowerCase
                    return u1.getNombre().compareToIgnoreCase(u2.getNombre());
                }
            });
            log.info("List Ordered Alphabetically: " + result.toString());
            return result; //200 OK PETITION
        }
        else
            return null; //404 (Empty Table)
    }

    //Añadir usuario al sistema
    public int addUser(String id, String nombre, String apellido){
        User newUser = new User(id,nombre,apellido);
        log.info("Creating new User: " + newUser);
        try{
            diccionarioUsuarios.put(id,newUser);
            log.info("User Added: " + newUser );
            return 201; //OK CREATED
        }
        catch (IndexOutOfBoundsException e){
            log.error("UserMap Full Error");
            return 507; //INSUFFICIENT STORAGE
        }
        catch (IllegalArgumentException e){
            log.error("Incorrect format exception");
            return 400; //BAD REQUEST
        }
    }

    //Modificar usuario
    public  int upDateUser(String id, String nombre, String apellido){
        User userToUpdate = this.diccionarioUsuarios.get(id);
        log.info("The user you want to update is: " + userToUpdate);
        if(userToUpdate != null){
            try {
                userToUpdate.setNombre(nombre);
                userToUpdate.setApellido(apellido);
                log.info("Updated user paraeters" + userToUpdate);
                return 201; //OK CREATED
            }
            catch (IllegalArgumentException e){
                log.error("An incorrect format for the User");
                return 400; //BAD REQUEST
            }
        } else {
            return 404; //User Not Found
        }
    }

    //Consultar número de usuarios en el sistema
    public int getNumUsers(){
        return this.diccionarioUsuarios.size();
    }

    //Consultar información de un usuario
    public User getUserInfo(String id){
        User usuario = diccionarioUsuarios.get(id);
        if(usuario != null){
            log.info("User to get information: " + usuario);
        }else{
            log.error("User not found for ID: "+id);
        }
        return usuario;
    }

    public User getUser(String id){
        User user = diccionarioUsuarios.get(id);
        return user;
    }
    //Añadir objeto al usuario
    public int addObjectToUser(String id, String gameObjectID){
        User user_temp = diccionarioUsuarios.get(id);
        Objeto object_temp = getObject(gameObjectID);
        if(user_temp != null && object_temp != null) {
            log.info("You want to add the object: " + object_temp.getNombre() + " to the user: " + user_temp.getNombre());
            int code = user_temp.addObjeto(object_temp);
            if (code == 201) {
                log.info("Object added to user " + user_temp.getNombre() + " : " + object_temp.getNombre());
                return 201; //OK CREATED
            } else if (code == 400) {
                log.error("Bad Format");
                return 400; //BAD REQUEST
            } else {
                log.error("No Inventory space for user: " + user_temp.getNombre());
                return 507; //INSUFFICIENT STORAGE
            }
        }
        else{
            log.error("User: "+id +" &/or Object: " + gameObjectID +" NOT FOUND!");
            return 404; //USER NOT FOUND
        }
    }


    //Añadir lista de objetos al usuario
    public int addObjectListToUser(String id, List<Objeto> listaObjetos){
        User user = diccionarioUsuarios.get(id);
        if(user != null){
            int code = user.setListaObjetos_resCode(listaObjetos);
            if(code == 201){
                log.info("Object List added to user " + user.getNombre());
                return 201; //OK CREATED
            }
            else if(code == 400){
                log.error("400: Bad Format");
                return 400; //BAD REQUEST
            }
            else{
                log.error("204: No Object Content: "+ user.getNombre());
                return 204; //204 No Content
            }
        }
        else{
            log.error("User Not found");
            return 404; //USER NOT FOUND
        }

    }

    //Consultar objetos del usuario
    public int getObjectsFromUser(String id){
        User user = diccionarioUsuarios.get(id);
        if(user!=null){
            log.info("User: "+user.getNombre() + " has NumObjects: "+user.getNumObjects());
            return user.getNumObjects();
        }
        else{
            log.error("User Not found");
            return 404; //USER NOT FOUND
        }
    }


    //EXTRAS

    //Añadir objeto
    public int addObject(Objeto objeto){
        int result;
        try {
            this.listaObjetos.add(objeto);
            log.info("201: Object Added: " + objeto.getNombre());
            result = 201;//OK CREATED
        } catch (IllegalArgumentException e) {
            log.error("400: Bad Object parameters");
            result = 400;//BAD REQUEST
        } catch (IndexOutOfBoundsException e) {
            log.error("507: Insufficient Storage");
            result = 507;//INSUFFICIENT STORAGE
        }
        return result;
    }

    public int removeObject(Objeto objeto){
        int result;
        try {
            this.listaObjetos.remove(objeto);
            log.info("201: Object removed: " + objeto.getNombre());
            result = 201;//OK CREATED
        } catch (IllegalArgumentException e) {
            log.error("400: Bad Object parameters");
            result = 400;//BAD REQUEST
        }
        return result;
    }

    //Añadir lista de objetos
    public int addListObject(List<Objeto> listaObjetos){
        int result;
        try {
            this.listaObjetos.addAll(listaObjetos);
            log.info("201: ObjectList Added: " + listaObjetos.toString());
            result = 201;//OK CREATED
        } catch (IllegalArgumentException e) {
            log.error("400: Bad List parameters");
            result = 400;//BAD REQUEST
        } catch (IndexOutOfBoundsException e) {
            log.error("507: Insufficient Storage");
            result = 507;//INSUFFICIENT STORAGE
        }
        return result;
    }


    //Consultar objeto
    public Objeto getObject(String idObjeto){
        Objeto resultadoObjeto = null;
        try{
            for(Objeto gameObject : this.listaObjetos){
                if (gameObject.getId().compareTo(idObjeto) == 0){
                    resultadoObjeto = gameObject;
                    log.info("302: Object found: " + gameObject.getNombre());
                }
            }
        }catch(ExceptionInInitializerError e){
            log.error("400: Object list not initialized");
            return null; //400 ERROR List of Objects not initialized
        }
        return resultadoObjeto;
    }


    //Consultar lista de Objetos de un usuario
    public List<User> getUserList(){
        List<User> lista = null;
        if(this.diccionarioUsuarios.size() != 0){
            lista = new LinkedList<>(this.diccionarioUsuarios.values());
            log.info("User List: "+lista.toString());
        }
        return lista; //Null: 404 Empty User HashMap

    }

    //Generate Id
    @Override
    public String generateId(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 3) { // length of the random generated ID
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
    //Liberar Recursos
    @Override
    public  void clearReserves() {
        this.listaObjetos.clear();
        this.diccionarioUsuarios.clear();
    }
}
