package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

//Hilo usado para operar en un fragmento de arrayGrande y escrbir su resultado en arrayParte con exclusión Mutua

public class HiloConsumidor implements Runnable {
    //Atributos
    private final int inicio;
    private int numero = 0;
    private int operador = 0;
    private int resultado = 0;
    private ReentrantLock l = new ReentrantLock();

    //Constructor
    public HiloConsumidor(int inicio) {
        this.inicio = inicio;
    }

    @Override
    public void run() {
        this.resultado = Programa.arrayGrande.get(inicio);
        for(int i=inicio; i<=inicio+8; i+=2) {
            operador = Programa.arrayGrande.get(i+1);
            numero = Programa.arrayGrande.get(i+2);
            switch (operador) {
                case 1:
                    resultado+=numero;
                    break;
                case 2:
                    resultado-=numero;
                    break;
                case 3:
                    resultado*=numero;
                    break;
                default:
                    break;
            }
        }
        l.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " ingresa resultado: " + resultado);
            Programa.arrayPartes.insertar(resultado);
        } finally {
            l.unlock();
        }
    }
}