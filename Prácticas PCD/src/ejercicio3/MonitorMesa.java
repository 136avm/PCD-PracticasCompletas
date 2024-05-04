package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene el monitor que controla las mesas.
 * Permite elegir una mesa, actualizar sus valores y liberarla.
 * Permite que los clientes se bloqueen para esperar a que su mesa esté disponible.
 * 
 * Las mesas se gestionan mediante un array de Condition para indicar su disponibilidad,
 * y otro array de enteros para llevar el seguimiento de los tiempos de espera de cada mesa.
 * 
 * Esta clase se utiliza para coordinar el acceso de los clientes a las mesas del banco.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class MonitorMesa {
    ReentrantLock l = new ReentrantLock(true);
    private Condition[] mesas = new Condition[Banco.NMESAS];
    private int[] esperaMesas = new int[Banco.NMESAS];

    /**
     * Constructor de la clase MonitorMesa.
     * Inicializa el array de condiciones para las mesas y el array para los tiempos de espera.
     */
    public MonitorMesa() {
        for(int i=0; i<mesas.length; i++) {
            mesas[i] = l.newCondition();
            esperaMesas[i] = 0;
        }
    }

    /**
     * Método que elige una mesa con el menor tiempo de espera.
     * 
     * @return El índice de la mesa seleccionada.
     */
    public int elegirMesa() {
        l.lock();
        try {
            int minEspera = Integer.MAX_VALUE;
            int minIdx = 0;
            for(int i=0; i<esperaMesas.length; i++) {
                if(esperaMesas[i]<minEspera) {
                    minEspera = esperaMesas[i];
                    minIdx = i;
                }
            }
            return minIdx;
        } finally {
            l.unlock();
        }
    }

    /**
     * Método que actualiza el tiempo de espera de una mesa tras ser seleccionada por un cliente.
     * 
     * @param espera El tiempo de espera que se añade o se resta a la mesa.
     * @param idMesa El índice de la mesa a la que se le actualiza el tiempo de espera.
     */
    public void cambiarEsperaMesa(int espera, int idMesa) {
        l.lock();
        try {
            esperaMesas[idMesa] += espera;
        } finally {
            l.unlock();
        }
    }

    /**
     * Método que permite que un cliente espere en cola para su mesa elegida.
     * 
     * @param idMesa El índice de la mesa a la que se está esperando.
     * @throws InterruptedException si el hilo actual es interrumpido mientras espera.
     */
    public void esperarEnColaParaMesa(int idMesa) throws InterruptedException {
        l.lock();
        try {
            while(l.hasWaiters(mesas[idMesa])) {
                mesas[idMesa].await();
            }
        } finally {
            l.unlock();
        }
    }

    /**
     * Método que libera una mesa y avisa si hay clientes esperando para usarla.
     * 
     * @param idMesa El índice de la mesa que se va a liberar.
     */
    public void liberarMesa(int idMesa) {
        l.lock();
        try {
            mesas[idMesa].signal();
        } finally {
            l.unlock();
        }
    }

    /**
     * Método utilizado por los clientes para obtener los tiempos de espera de las mesas.
     * 
     * @return Una cadena de texto con los tiempos de espera de cada mesa.
     */
    public String toString() {
        l.lock();
        try {
            return "\nTiempo de espera en la mesa 1: " + esperaMesas[0]/1000+"s" + ", mesa 2: " + esperaMesas[1]/1000+"s" +
                    ", mesa 3: " + esperaMesas[2]/1000+"s" + ", mesa 4: " + esperaMesas[3]/1000+"s" + "\n";
        } finally {
            l.unlock();
        }
    }
}