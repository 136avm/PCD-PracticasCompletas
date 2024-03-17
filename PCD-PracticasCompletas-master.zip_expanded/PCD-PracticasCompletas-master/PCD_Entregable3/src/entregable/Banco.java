package entregable;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;



class HiloCliente extends Thread {
	private final int tiempoX;
	private final int tiempoY;
	private final int id;
	private int idMaquina;
	private int idMesa;
	ReentrantLock l = new ReentrantLock();

	public HiloCliente(int id) {
		this.id = id;
		this.tiempoX = (int) (Math.random() * (12000 - 8000 + 1)) + 8000;
		this.tiempoY = (int) (Math.random() * (60000 - 30000 + 1)) + 30000;
	}

	public void run() {
		try {
			this.idMaquina = Banco.maquinas.elegirMaquina();
			Thread.sleep(tiempoX);
			Banco.maquinas.liberarMaquina(idMaquina);
			
			this.idMesa = Banco.mesas.elegirMesa();
			l.lock();
			try {
				System.out.println("-----------------------------------------------------------------"+
						"\nCliente " + id + " ha solicitado su servicio en la máquina: " + idMaquina +
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

class MonitorMaquina {
	ReentrantLock l = new ReentrantLock(true);
	private Condition maquinas = l.newCondition();
	private boolean[] estadoMaquinas = {true, true, true};
	
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
	
	public void cambiarEsperaMesa(int espera, int idMesa) {
		l.lock();
		try {
			esperaMesas[idMesa] += espera;
		} finally {
			l.unlock();
		}
	}
	
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
	
	public void liberarMesa(int idMesa) {
		l.lock();
		try {
			mesas[idMesa].signal();
		} finally {
			l.unlock();
		}
	}
	
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