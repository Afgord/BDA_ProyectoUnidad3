/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.conexion;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Clase encargada de gestionar la conexión con MongoDB.
 *
 * Implementa el patrón Singleton para asegurar que exista una sola instancia
 * de conexión durante la ejecución del sistema.
 *
 * Además, configura PojoCodecProvider para permitir el mapeo automático entre
 * clases Java POJO y documentos BSON de MongoDB.
 *
 * @author Afgord
 */
public class MongoDBConnection {

    /**
     * Cadena de conexión a MongoDB local.
     */
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";

    /**
     * Nombre de la base de datos utilizada por el sistema.
     */
    private static final String DATABASE_NAME = "reserva_trenes_db";

    /**
     * Instancia única de la clase.
     */
    private static MongoDBConnection instancia;

    /**
     * Cliente de MongoDB.
     */
    private MongoClient mongoClient;

    /**
     * Base de datos de MongoDB con soporte para POJOs.
     */
    private MongoDatabase database;

    /**
     * Constructor privado para evitar la creación directa de objetos.
     *
     * Configura el registro de codecs necesario para mapear automáticamente
     * POJOs de Java hacia documentos BSON.
     */
    private MongoDBConnection() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder()
                        .automatic(true)
                        .build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(
                        new com.mongodb.ConnectionString(CONNECTION_STRING))
                .codecRegistry(pojoCodecRegistry)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.database = mongoClient
                .getDatabase(DATABASE_NAME)
                .withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Obtiene la instancia única de conexión.
     *
     * @return Instancia única de MongoDBConnection.
     */
    public static MongoDBConnection getInstance() {
        if (instancia == null) {
            instancia = new MongoDBConnection();
        }

        return instancia;
    }

    /**
     * Obtiene la base de datos configurada con soporte para POJOs.
     *
     * @return Base de datos MongoDB.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Cierra la conexión con MongoDB y limpia la instancia Singleton.
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            instancia = null;
        }
    }
}