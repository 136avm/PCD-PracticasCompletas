package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene el monitor que controla las máquinas.
 * Permite elegir una máquina y liberarla. Los clientes pueden quedar bloqueados si no hay máquinas disponibles.
 * 
 * Las máquinas se gestionan mediante un array de booleanos que indica si están ocupadas o no.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class MonitorMaquina {
    ReentrantLock l = new ReentrantLock(true);
    private Condition maquinas = l.newCondition();
    private boolean[] estadoMaquinas = {true, true, true};

    /**
     * Método que elige una máquina libre y la bloquea. Si no hay una máquina libre, el cliente queda bloqueado hasta que una esté disponible.
     * 
     * @return El índice de la máquina elegida.
     * @throws InterruptedException si el hilo actual es interrumpido mientras espera
     */
    public int elegirMaquina() throws InterruptedException {
        l.lock();
        try {
            while(true) {
                for(int i=0; i<estadoMaquinas.length; i++) {
                    if(estadoMaquinas[i]) {
                        estadoMaquinas[i] = false;
                        return i;
                    }
                }
                maquinas.await();
            }
        } finally {
            l.unlock();
        }
    }

    /**
     * Método que libera una máquina y avisa si hay clientes esperando para usarla.
     * 
     * @param id El índice de la máquina a liberar.
     */
    public void liberarMaquina(int id) {
        l.lock();
        try {
            estadoMaquinas[id] = true;
            maquinas.signal();
        } finally {
            l.unlock();
        }
    }
}