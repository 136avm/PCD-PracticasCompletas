package ejercicio1;

/**
 * Hilo usado para sumar los valores obtenidos tras la ejecución de HiloConsumidor.
 * @author Álvaro aledo tornero
 * @author Antonio Vergara Moya
 */
public class HiloSumador implements Runnable {
    /**
     * Método principal del hilo que realiza la suma de los valores obtenidos.
     */
    @Override
    public void run() {
        for(int numero : Programa.arrayPartes.getArray()) {
            Programa.resultadoFinal+=numero;
        }
    }
}
