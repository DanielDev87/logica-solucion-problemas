package com.logsoluprobl.appbank.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase Singleton que gestiona el acceso físico a los archivos JSON.
 */
@Component
public class FileManager {

    // Spring inyecta la ruta de la configuración. ¡ESTO ES CORRECTO!
    @Value("${bank.data.path}") 
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Constructor sin argumentos: Spring puede crearlo sin problemas
    public FileManager() {
        // Inicialización simple
    }

    /**
     * Obtiene el objeto File basándose en el nombre del archivo y el path base.
     */
    private File getFile(String fileName) {
        // Usa el dataPath inyectado por Spring para construir la ruta
        return new File(dataPath, fileName); 
    }
    
    // Métodos específicos para los archivos conocidos
    public File getCustomersFile() {
        return getFile("customers.json");
    }

    public File getAccountsFile() {
        return getFile("accounts.json");
    }

    /**
     * Lee datos de un archivo JSON y los deserializa.
     * @param fileName Nombre del archivo (ej: "customers.json")
     * @param typeReference Objeto TypeReference que indica el tipo de List<T>
     */
    public <T> List<T> read(String fileName, TypeReference<List<T>> typeReference) {
        File file = getFile(fileName);
        try {
            if (!file.exists() || file.length() == 0) {
                return List.of();
            }
            return objectMapper.readValue(file, typeReference);
        } catch (IOException e) {
            // Recomiendo una mejor excepción
            throw new RuntimeException("Error al leer el archivo JSON: " + file.getName(), e);
        }
    }

    /**
     * Serializa y escribe datos a un archivo JSON.
     * @param fileName Nombre del archivo (ej: "customers.json")
     * @param data Lista de objetos a escribir
     */
    public <T> void write(String fileName, List<T> data) {
        File file = getFile(fileName);
        try {
            // Asegura que el directorio exista (útil para paths relativos)
            file.getParentFile().mkdirs(); 
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el archivo JSON: " + file.getName(), e);
        }
    }
}