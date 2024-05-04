package ejercicio3;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que simula un cliente que solicita una máquina, imprime un mensaje y usa una mesa. La impresión va por cerrojo.
 * Las mesas y las máquinas las controla una instancia de MonitorMesa y MonitorMaquina respectivamente.
 * 
 * Esta clase extiende la clase Thread y representa un hilo de ejecución para un cliente.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class HiloCliente extends Thread {
    private final int tiempoX;
    private final int tiempoY;
    private final int id;
    private int idMaquina;
    private int idMesa;
    ReentrantLock l = new ReentrantLock();

    /**
     * Constructor de la clase HiloCliente.
     * 
     * @param id El identificador único del cliente.
     */
    public HiloCliente(int id) {
        this.id = id;
        this.tiempoX = (int) (Math.random() * (12000 - 8000 + 1)) + 8000;
        this.tiempoY = (int) (Math.random() * (60000 - 30000 + 1)) + 30000;
    }

    /**
     * Método que se ejecuta cuando se inicia el hilo.
     */
    public void run() {
        try {
            this.idMaquina = Banco.maquinas.elegirMaquina();
            Thread.sleep(tiempoX);
            Banco.maquinas.liberarMaquina(idMaquina);

            this.idMesa = Banco.mesas.elegirMesa();
            l.lock();
            try {
                System.out.println("-----------------------------------------------------------------"+
                        "\nCliente " + id + " ha solicitado su servicio en la máquina: " + (idMaquina+1) +
                        "\nTiempo en solicitar el servicio: " + tiempoX/1000+"s" +
                        "\nSerá atendido en la mesa: " + (idMesa+1) +
                        "\nTiempo en la mesa: " + tiempoY/1000+"s" +
                        Banco.mesas.toString() +
                        "-----------------------------------------------------------------\n");
            } finally {
                l.unlock();
            }
            Banco.mesas.cambiarEsperaMesa(tiempoY, idMesa);
            Banco.mesas.esperarEnColaParaMesa(idMesa);
            Thread.sleep(tiempoY);
            Banco.mesas.cambiarEsperaMesa(-tiempoY, idMesa);
            Banco.mesas.liberarMesa(idMesa);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}