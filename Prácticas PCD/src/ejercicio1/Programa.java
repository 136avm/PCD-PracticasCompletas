package ejercicio1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 * Clase que simula un array de enteros. Hace uso de un cerrojo para sincronizar la escritura
 */
class ArrayCompartidoPartes {
    private ArrayList<Integer> array;
    private ReentrantLock l = new ReentrantLock();

    /**
     * Contructor de la clase ArrayCompartidoClase
     */
    public ArrayCompartidoPartes() {
        this.array = new ArrayList<Integer>();
    }

    /**
     *
     * @return Una copia del array de la clase
     */
    public ArrayList<Integer> getArray() {
        return new ArrayList<Integer>(array);
    }

    /**
     * Inserta un entero al atributo array de la clase. Hace uso de un cerrojo para garantizar la exclusión mutua
     * @param contenido entero que se inserta en el array
     */
    public void insertar(int contenido) {
        l.lock();
        try {
            array.add(contenido);
        } finally {
            l.unlock();
        }
    }

    @Override
    public String toString() {
        return array.toString();
    }

}

public class Programa {
    //Creamos variables globales del programa
    public static ArrayList<Integer> arrayGrande = new ArrayList<Integer>();
    public static ArrayCompartidoPartes arrayPartes = new ArrayCompartidoPartes();
    public static int resultadoFinal;

    public static void main(String[] args) {
        //Creamos todos los hilos
        Thread generador = new Thread(new HiloGenerador());
        Thread consumidor1 = new Thread(new HiloConsumidor(0));
        Thread consumidor2 = new Thread(new HiloConsumidor(11));
        Thread consumidor3 = new Thread(new HiloConsumidor(22));
        Thread consumidor4 = new Thread(new HiloConsumidor(33));
        Thread consumidor5 = new Thread(new HiloConsumidor(44));
        Thread consumidor6 = new Thread(new HiloConsumidor(55));
        Thread consumidor7 = new Thread(new HiloConsumidor(66));
        Thread consumidor8 = new Thread(new HiloConsumidor(77));
        Thread consumidor9 = new Thread(new HiloConsumidor(88));
        Thread consumidor10 = new Thread(new HiloConsumidor(99));
        Thread sumador = new Thread(new HiloSumador());

        //Iniciamos el hilo generador que rellena el arrayGrande con los números aleatorios y sus operaciones.
        generador.start();

        //Esperamos a que se genere el array para ejecutar a los consumidores y operar sobre él.
        try {
            generador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Llamamos a los consumidores.
        consumidor1.start();
        consumidor2.start();
        consumidor3.start();
        consumidor4.start();
        consumidor5.start();
        consumidor6.start();
        consumidor7.start();
        consumidor8.start();
        consumidor9.start();
        consumidor10.start();

        //Esperamos a que se los consumidores terminen de operar
        try {
            consumidor1.join();
            consumidor2.join();
            consumidor3.join();
            consumidor4.join();
            consumidor5.join();
            consumidor6.join();
            consumidor7.join();
            consumidor8.join();
            consumidor9.join();
            consumidor10.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Llamamos al sumador
        sumador.start();

        //Esperamos a que termine de sumar para imprimir los resultados
        try {
            sumador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Imprimimos el arrayGrande
        System.out.println("\nEl array principal es:");
        System.out.println(arrayGrande);
        //Imprimimos el arrayPartes
        System.out.println("\nEl array por partes es:");
        System.out.println(arrayPartes.getArray());
        //Imprimimos el resultado final
        System.out.println("\nEl resultado final es: " + resultadoFinal);

    }
}