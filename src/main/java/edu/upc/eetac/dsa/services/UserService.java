
package edu.upc.eetac.dsa.services;

import edu.upc.eetac.dsa.GameManager;
import edu.upc.eetac.dsa.GameManagerImpl;
import edu.upc.eetac.dsa.models.User;
import edu.upc.eetac.dsa.models.Objeto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

//Models or Element Entity
//Swagger Imports
@Api(value = "/game", description = "Endpoint to User Service")
@Path("/gameManager")
public class UserService {
    static final Logger logger = Logger.getLogger(UserService.class);
    private GameManager manager;

    //Estructura de datos
    User user;
    List<Objeto> listaObjetos;

    public UserService(){
        //Configuring Log4j, location of the log4j.properties file and must always be inside the src folder
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        this.manager = GameManagerImpl.getInstance();
        if (this.manager.getNumUsers() == 0) {
            //Adding Users to the system
            this.manager.addUser("001", "Guille", "Montejo");
            this.manager.addUser("002", "Santiago", "Segura");
            this.manager.addUser("003", "Usuario", "Tercero");

            //Adding objects to the system
            this.manager.addObject(new Objeto("Espada", "x"));
            this.manager.addObject(new Objeto("Escudo", "y"));
            this.manager.addObject(new Objeto("pistola","z"));

            //Adding objects to users
            this.manager.addObjectToUser("001","z");
            this.manager.addObjectToUser("001", "y");
            this.manager.addObjectToUser("002", "x");
        }
    }

    //When multiple GET, PUT, POSTS & DELETE EXIST on the same SERVICE, path must be aggregated
    //Lista de Usuarios
    @GET
    @ApiOperation(value = "Get all Users", notes = "Retrieves the list of users")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
    })
    @Path("/listUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<User> user = this.manager.getUserList();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(user) {};
        return Response.status(201).entity(entity).build()  ;
    }

    //Añadir un nuevo usuario al sistema
    @POST
    @ApiOperation(value = "Create a new User", notes = "Adds a new user given name and surname")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/addUser/{name}/{surname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(@PathParam("name") String name,@PathParam("surname") String surname ) {
        if (name.isEmpty() || surname.isEmpty())  return Response.status(500).entity(new User()).build();
        String temp_id = manager.generateId();
        this.manager.addUser(temp_id,name,surname);
        return Response.status(201).entity(manager.getUser(temp_id)).build();
    }

    //Modificar un usuario
    @PUT
    @ApiOperation(value = "Update a User", notes = "Edits an existing User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @Path("/upDateUser/{id}/{name}/{surname}")
    public Response updateUser(@PathParam("id") String id,@PathParam("name") String name,@PathParam("surname") String surname ) {
        int resp = this.manager.upDateUser(id,name,surname);
        if (resp != 201) return Response.status(resp).build();
        return Response.status(201).entity(manager.getUser(id)).build();
    }

    //Consultar un Usuario
    @GET
    @ApiOperation(value = "Get User Information", notes = "Retrieve User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/getUserInfo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        User user = this.manager.getUserInfo(id);
        if (user == null) return Response.status(404).build();
        else  return Response.status(201).entity(user).build();
    }

    //Consultar objetos de un usuario
    @GET
    @ApiOperation(value = "Get a User Objects", notes = "Retrieve User Objects")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Objeto.class,responseContainer="List"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 204, message = "No Game Object found")
    })
    @Path("/consultGameObjectsUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectsFromUser(@PathParam("id") String id) {
        User user = this.manager.getUser(id);
        List<Objeto> listGameObject;
        if (user == null) return Response.status(404).build();
        else {
            if(user.getNumObjects()==0){
                Response.status(204).build();
            }
        }

        listGameObject = user.getListaObjetos();
        GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(listGameObject) {};
        return Response.status(201).entity(entity).build()  ;
    }

    //Añadir un objeto sobre un usuario
    //Adds a new object given multiple parameters(userId & gameObjectId)
    @PUT
    @ApiOperation(value = "Adds a Game object to user", notes = "Adds an existing Game Object to user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 404, message = "User/GameObject Not found Error")
    })
    @Path("/addGameObjectUser/{userId}/{gameObjectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addObject(@PathParam("userId") String userId,@PathParam("gameObjectId") String gameObjectId) {
        if (userId.isEmpty() || gameObjectId.isEmpty())  return Response.status(500).entity(new User()).build();
        else{
            User user = manager.getUser(userId);
            Objeto gameObject = manager.getObject(gameObjectId);
            if(user==null || gameObject ==null)  return Response.status(404).entity(new User()).build();
        }
        manager.addObjectToUser(userId,gameObjectId);
        return Response.status(201).entity(manager.getUser(userId)).build();
    }

    //EXTRA
    //Eliminar objeto
    @DELETE
    @ApiOperation(value = "delete an Object", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/{id}")
    public Response deleteObject(@PathParam("id") String id) {
        Objeto o = this.manager.getObject(id);
        if (o == null) return Response.status(404).build();
        else this.manager.removeObject(manager.getObject(id));
        return Response.status(201).build();
    }
}

