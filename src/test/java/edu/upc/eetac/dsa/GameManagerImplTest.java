package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.models.User;
import edu.upc.eetac.dsa.models.Objeto;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class GameManagerImplTest {
    // THE QUICK REMINDER: Remember to name the test class public smh
    //Log4j Logger initialization
    private static Logger logger = Logger.getLogger(GameManagerImplTest.class);
    //GameManager
    public GameManager manager = null;
    //Estructura de datos
    User user;
    List<Objeto> listaObjetos;


    //Metodo SetUp
    @Before
    public void setUp() {
        //Configuring Log4j
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        logger.debug("Debug Test Message!");
        logger.info("Info Test Message!");
        logger.warn("Warning Test Message!");
        logger.error("Error Test Message!");

        //Instancing GameManager Implementation
        manager = GameManagerImpl.getInstance();

        //Initialzing Test User
        user = new User("xyz", "Guille", "Montejo");


        //Initializing Object List
        listaObjetos =  new LinkedList<Objeto>();

        //Appending Object en Lista
        //Caso( String idCaso,String nombre, String apellidos, String genero, String correo, String direccion, Date fechaNacimiento,Date fechaInforme, String nivelRiesgo, String classificacion, int telefono)
        listaObjetos.add(new Objeto("Espada", "001"));
        listaObjetos.add(new Objeto("Pocion", "002"));
        listaObjetos.add(new Objeto("Escudo", "003"));

        manager.addListObject(listaObjetos);
    }

    //Tests
    //Método Test para crear un nuevo usuario en el sistema y verificar
    @Test
    public void addUserTest(){
        //Initial Test, initial users in game Zero!
        Assert.assertEquals(0, this.manager.getNumUsers());
        //Test adding user to Game Manager
        user = new User("3940","Usuario nuevo","apellido nuevo" ,listaObjetos);
        manager.addUser(user.getId(),user.getNombre(),user.getApellido());
        //We expect now 1 brotes
        Assert.assertEquals(1, this.manager.getNumUsers());
    }

    //Tests
    //Metodo Test para añadir un objeto al sistema
    @Test
    public void addObjetoAUserTest(){
        manager.addUser(user.getId(),user.getNombre(), user.getApellido());
        //Initial Test, objetos iniciales en user
        Assert.assertEquals(0, manager.getObjectsFromUser("xyz"));

        //Now we will add 1 object
        this.manager.addObjectToUser("xyz", listaObjetos.get(0).getId());

        //We expect now 1 object
        Assert.assertEquals(1, this.manager.getObjectsFromUser("xyz"));

        //Now we will add second object
        this.manager.addObjectToUser("xyz", listaObjetos.get(1).getId());

        //We expect now 2 objects
        Assert.assertEquals(2, this.manager.getObjectsFromUser("xyz"));
    }
    @After
    public void tearDown() throws Exception {
        manager.clearReserves();
    }
}
