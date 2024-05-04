package ejercicio3;

/**
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 * Clase que simula el banco con sus máquinas y mesas, crea automáticamente 50 clientes que solicitan una máquina y una mesa
 * y sus monitores correspondientes.
 */

public class Banco {
    public static final int NMESAS = 4;
    // Creamos el monitor de las máquinas
    public static MonitorMaquina maquinas = new MonitorMaquina();

    // Creamos los monitores de las mesas
    public static MonitorMesa mesas = new MonitorMesa();

    public static void main(String[] args) {

        // Creamos los clientes
        Thread[] clientes = new Thread[50];
        for (int i = 0; i < 50; i++) {
            clientes[i] = new HiloCliente(i);
        }

        // Iniciamos todos los hilos
        for (Thread cliente : clientes) {
            cliente.start();
        }

    }
}
