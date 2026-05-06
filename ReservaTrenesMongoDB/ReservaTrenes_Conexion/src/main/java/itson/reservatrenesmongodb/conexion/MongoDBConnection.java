/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.conexion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Clase encargada de gestionar la conexión con MongoDB.
 *
 * Implementa el patrón Singleton para asegurar que exista una sola instancia
 * de conexión durante la ejecución del sistema.
 *
 * @author Afgord
 */
public class MongoDBConnection {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "reserva_trenes_db";

    private static MongoDBConnection instancia;

    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoDBConnection() {
        this.mongoClient = MongoClients.create(CONNECTION_STRING);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public static MongoDBConnection getInstance() {
        if (instancia == null) {
            instancia = new MongoDBConnection();
        }

        return instancia;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            instancia = null;
        }
    }
}
