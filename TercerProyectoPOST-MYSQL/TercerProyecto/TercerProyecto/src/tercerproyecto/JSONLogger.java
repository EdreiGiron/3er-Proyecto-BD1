/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tercerproyecto;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author edrei
 */

public class JSONLogger {

    private static final String BITACORA_FILE = "bitacora.json";

    public boolean agregarBitacora(String base, String dpi, String operacion, String query) {
        try {
            // Leer el archivo JSON actual o crear uno nuevo si no existe
            JSONArray bitacoraArray = leerArchivoBitacora();

            // Crear un nuevo objeto JSON para la entrada de la bitácora
            JSONObject nuevaEntrada = new JSONObject();
            nuevaEntrada.put("base", base);
            nuevaEntrada.put("dpi", dpi);
            nuevaEntrada.put("operacion", operacion);
            nuevaEntrada.put("segundos", java.time.Instant.now().getEpochSecond());
            nuevaEntrada.put("query", query);

            // Agregar la nueva entrada al arreglo JSON
            bitacoraArray.add(nuevaEntrada);

            // Escribir el arreglo actualizado al archivo JSON
            try (FileWriter fileWriter = new FileWriter(BITACORA_FILE)) {
                fileWriter.write(bitacoraArray.toJSONString()); // Sobreescribir el archivo completo con los nuevos datos
                fileWriter.flush();
            }

            return true; // Operación exitosa
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Hubo un error al escribir en el archivo
        }
    }

    JSONArray leerArchivoBitacora() {
        try (FileReader reader = new FileReader(BITACORA_FILE)) {
            // Leer el archivo JSON existente
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (Exception e) {
            // Si no se puede leer el archivo, retornar un nuevo JSONArray vacío
            return new JSONArray();
        }
    }
}
