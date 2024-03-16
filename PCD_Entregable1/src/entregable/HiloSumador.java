package entregable;

public class HiloSumador implements Runnable {
	@Override
	public void run() {
		for(int numero : Programa.arrayPartes.getArray()) {
			Programa.resultadoFinal+=numero;
		}
	}
}