package ejercicio1;

//Hilo usado para sumar los valores obtenidos tras la ejecución de HiloConsumidor

public class HiloSumador implements Runnable {
    @Override
    public void run() {
        for(int numero : Programa.arrayPartes.getArray()) {
            Programa.resultadoFinal+=numero;
        }
    }
}