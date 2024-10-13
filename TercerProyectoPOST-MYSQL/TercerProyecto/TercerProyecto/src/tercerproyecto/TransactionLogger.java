/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tercerproyecto;

/**
 *
 * @author cristian
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class TransactionLogger {

    private static final String LOG_FILE = "transacciones.log";  // Nombre del archivo donde se guardarán los logs

    // Método para registrar un mensaje en el archivo de log
    public static void log(String message) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true); PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String timeStamp = LocalDateTime.now().toString();  // Obtiene la fecha y hora actuales
            printWriter.println("[" + timeStamp + "] " + message);  // Escribe el mensaje con un timestamp
        } catch (IOException e) {
            e.printStackTrace();  // Si ocurre un error, lo imprime en la consola
        }
    }
}
