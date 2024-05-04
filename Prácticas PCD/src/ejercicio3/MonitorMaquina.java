package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Clase que contiene el monitor que controla las máquinas. Contiene métodos para poder elegir una máquina y liberarla.
 *  Permite que los clientes se queden bloqueados si no hay máquinas disponibles y que puedan despertar a otro tras terminar de usar su máquina
 */

class MonitorMaquina {
    ReentrantLock l = new ReentrantLock(true);
    private Condition maquinas = l.newCondition();
    private boolean[] estadoMaquinas = {true, true, true};
    // Selecciona una máquina libre y la bloquea, si no hay una libre se queda esperando
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
    // Libera una máquina y avisa por si hay alguien esperando para usarla
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
