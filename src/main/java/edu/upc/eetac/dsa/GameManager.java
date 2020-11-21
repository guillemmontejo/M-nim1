package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.models.User;
import edu.upc.eetac.dsa.models.Objeto;

import java.util.List;

public interface GameManager {

    //Listado ordenado alfabéticamente de los Usuarios
    List<User> getSortedUsersAlphabetical();

    //Añadir usuario al sistema
    int addUser(String id, String nombre, String apellido);

    //Modificar usuario
    int upDateUser(String id, String nombre, String apellido);

    //Consultar número de usuarios
    int getNumUsers();

    //Consultar información de un usuario
    User getUserInfo(String id);

    //Añadir objeto al usuario
    int addObjectToUser(String id, String gameObjectID);

    //Añadir lista de objetos al usuario
    int addObjectListToUser(String id, List<Objeto> listaObjetos);

    //Consultar objetos del usuario
    int getObjectsFromUser(String id);


    //OTROS

    //Añadir objeto
    int addObject(Objeto objeto);

    //Eliminar objeto
    int removeObject(Objeto objeto);

    //Añadir lista de objetos
    int addListObject(List<Objeto> listaObjetos);

    //Consultar objeto
    Objeto getObject(String idObjeto);

    //Consultar lista usuarios
    List<User> getUserList();

    void clearReserves();

    String generateId();

    User getUser(String id);
}
