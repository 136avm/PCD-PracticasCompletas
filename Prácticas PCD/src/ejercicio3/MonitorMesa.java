package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene el monitor que controla las mesas. Contiene métodos para poder elegir una mesa, actualizar
 * sus valores y liberarla. Permite que los clientes puedas bloquearse para esperar a que su mesa esté disponible.
 */

class MonitorMesa {
    ReentrantLock l = new ReentrantLock(true);
    private Condition[] mesas = new Condition[Banco.NMESAS];
    private int[] esperaMesas = new int[Banco.NMESAS];

    public MonitorMesa() {
        for(int i=0; i<mesas.length; i++) {
            mesas[i] = l.newCondition();
            esperaMesas[i] = 0;
        }
    }
    // Selecciona una mesa con menos tiempo de espera
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
    // Cambia el tiempo de espera de una mesa tras tomarla
    public void cambiarEsperaMesa(int espera, int idMesa) {
        l.lock();
        try {
            esperaMesas[idMesa] += espera;
        } finally {
            l.unlock();
        }
    }
    // Metodo para esperar que tu mesa elegida esté disponible para ti
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
    // Libera una mesa y avisa por si hay alguien esperando para usarla
    public void liberarMesa(int idMesa) {
        l.lock();
        try {
            mesas[idMesa].signal();
        } finally {
            l.unlock();
        }
    }
    // Metodo usado para que los clientes puedan imprimir los tiempos de espera de las mesas
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
