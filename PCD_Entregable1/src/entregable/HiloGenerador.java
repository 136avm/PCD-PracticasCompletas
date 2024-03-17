package entregable;

public class HiloGenerador implements Runnable {
	@Override
	public void run() {
		//Rellenamos el arrayGrande
		for(int i=0; i<10; i++) {
			for(int j=0; j<11; j++) {
				if(j%2 == 0) {
					Programa.arrayGrande.add((int) (Math.random()*10));
				} else {
					Programa.arrayGrande.add((int) (Math.random()*(3)+1));
				}
			}
		}
	}
}