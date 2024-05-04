package ejercicio1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa un array compartido utilizado en el ejercicio 1.
 * Permite la inserción y obtención de elementos de manera segura entre hilos, utilizando un cerrojo ReentrantLock.
 * @author Álvaro aledo tornero
 * @author Antonio Vergara Moya
 */
public class ArrayCompartidoPartes {
    private ArrayList<Integer> array;
    private ReentrantLock l = new ReentrantLock();

    /**
     * Constructor de la clase ArrayCompartidoPartes.
     * Inicializa el array interno como una nueva instancia de ArrayList.
     */
    public ArrayCompartidoPartes() {
        this.array = new ArrayList<Integer>();
    }

    /**
     * Retorna una copia del array interno.
     * 
     * @return Una copia del array interno.
     */
    public ArrayList<Integer> getArray() {
        return new ArrayList<Integer>(array);
    }

    /**
     * Inserta un entero en el array interno.
     * Utiliza un cerrojo para garantizar la exclusión mutua.
     * 
     * @param contenido Entero que se inserta en el array.
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