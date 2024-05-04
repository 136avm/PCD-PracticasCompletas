package ejercicio3;

/**
 * Clase que simula un banco con máquinas y mesas, y crea automáticamente 50 clientes que solicitan una máquina y una mesa, junto con sus monitores correspondientes.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class Banco {
    /**
     * Número de mesas en el banco.
     */
    public static final int NMESAS = 4;
    
    // Creamos el monitor de las máquinas
    public static MonitorMaquina maquinas = new MonitorMaquina();

    // Creamos los monitores de las mesas
    public static MonitorMesa mesas = new MonitorMesa();

    /**
     * Método principal que crea y inicia los hilos de los clientes.
     * 
     * @param args Los argumentos de la línea de comandos.
     */
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