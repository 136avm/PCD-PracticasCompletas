package ejercicio2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase principal que contiene el punto de entrada principal (main) y las variables compartidas que permiten el funcionamiento del cruce.
 * Inicializa todos los hilos (coches y peatones) y los lanza.
 * 
 * Permite la coordinación y el cruce de vehículos y peatones en un punto de intersección.
 * Controla el turno y las colas de espera para cada dirección y tipo de usuario.
 * Proporciona constantes para los tiempos de espera y los límites de vehículos y peatones.
 * 
 * Los vehículos y peatones se gestionan a través de hilos (Threads).
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class Cruce {
    // Constantes y semáforos usados como colas
    public static final int MAX_VEHICULOS = 4;
    public static final int MAX_PEATONES = 10;
    public static final int TIEMPO_PEATON = 3000;
    public static final int TIEMPO_COCHE = 500;
    public static final int TIEMPO_VUELTA_PEATON = 8000;
    public static final int TIEMPO_VUELTA_COCHE = 7000;
    public static int turno = 0; // 0 para PE, 1 para NS, 2 para EO
    public static int cochesNS = 0;
    public static int cochesNSesp = 0;
    public static int cochesEO = 0;
    public static int cochesEOesp = 0;
    public static int peatones = 0;
    public static int peatonesEsp = 0;
    public static ReentrantLock mutex = new ReentrantLock();
    public static Semaphore colaNS = new Semaphore(0);
    public static Semaphore colaEO = new Semaphore(0);
    public static Semaphore colaPE = new Semaphore(0);

    /**
     * Método principal que inicia el cruce de vehículos y peatones.
     * 
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {

        List<Thread> trafico = new LinkedList<Thread>();

        for (int i = 0; i < 100; i++) {
            trafico.add(new HiloPeaton());
        }

        for (int i = 0; i < 50; i++) {
            trafico.add(new HiloCoche());
        }

        new Turnador().start();

        for (Thread elem : trafico) {
            elem.start();
        }

    }
}