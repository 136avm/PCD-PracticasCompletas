package entregable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

class Contador {
	private int contador = 0;

	public int getContador() {
		return this.contador;
	}

	public synchronized void incrementar() {
		this.contador++;
	}

	public synchronized void decrementar() {
		this.contador--;
	}
}

class HiloCoche extends Thread {
	private Direcciones direccion = Direcciones.NORTE_SUR;

	@Override
	public void run() {
		while (true) {
			if (direccion.equals(Direcciones.NORTE_SUR)) {
				if (Cruce.turno == 0 && Cruce.contNS.getContador() < Cruce.MAX_VEHICULOS
						&& Cruce.contPE.getContador() == 0) {
					Cruce.contNS.incrementar();
					System.out.println("Vehículo cruzando en la dirección Norte-Sur...");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Cruce.contNS.decrementar();
					Cruce.mutexNS.release();
					direccion = Direcciones.ESTE_OESTE;
					try {
						Thread.sleep(7000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Cruce.mutexNS.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else if (direccion.equals(Direcciones.ESTE_OESTE)) {
				if (Cruce.turno == 1 && Cruce.contEO.getContador() < Cruce.MAX_VEHICULOS
						&& Cruce.contNS.getContador() == 0) {
					Cruce.contEO.incrementar();
					System.out.println("Vehículo cruzando en la dirección Este-Oeste...");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Cruce.contEO.decrementar();
					Cruce.mutexEO.release();
					direccion = Direcciones.NORTE_SUR;
					try {
						Thread.sleep(7000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Cruce.mutexEO.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

class HiloPeaton extends Thread {
	@Override
	public void run() {
		while (true) {
			if (Cruce.turno == 2 && Cruce.contPE.getContador() < Cruce.MAX_PEATONES
					&& Cruce.contEO.getContador() == 0) {
				Cruce.contPE.incrementar();
				System.out.println("Peatón cruzando...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Cruce.contPE.decrementar();
				Cruce.mutexPE.release();
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Cruce.mutexPE.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class TurnosCruce extends Thread {
	public void run() {
		while (true) {
			try {
				if (Cruce.contEO.getContador() == 0 && Cruce.contNS.getContador() == 0
						&& Cruce.contPE.getContador() == 0) {
					Thread.sleep(5000);
					Cruce.turno = (Cruce.turno + 1) % 3;
					switch (Cruce.turno) {
					case 0:
						Cruce.mutexNS.release(Cruce.mutexNS.getQueueLength());
						break;
					case 1:
						Cruce.mutexEO.release(Cruce.mutexEO.getQueueLength());
						break;
					case 2:
						Cruce.mutexPE.release(Cruce.mutexPE.getQueueLength());
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Cruce {

	public static final int MAX_VEHICULOS = 4;
	public static final int MAX_PEATONES = 10;
	public static int turno = 0; // 0 para NS, 1 para EO, 2 para PE
	public static Contador contNS = new Contador();
	public static Contador contEO = new Contador();
	public static Contador contPE = new Contador();
	public static Semaphore mutexNS = new Semaphore(0);
	public static Semaphore mutexEO = new Semaphore(0);
	public static Semaphore mutexPE = new Semaphore(0);

	public static void main(String[] args) {

		List<Thread> trafico = new LinkedList<Thread>();

		for (int i = 0; i < 100; i++) {
			trafico.add(new HiloPeaton());
		}

		for (int i = 0; i < 50; i++) {
			trafico.add(new HiloCoche());
		}

		new TurnosCruce().start();

		for (Thread elem : trafico) {
			elem.start();
		}

	}
}