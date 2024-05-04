package ejercicio2;

/**
 * Clase que se encarga de cambiar el turno cada 5 segundos
 * Se encarga de modificar la variable turno y de imprimir el turno actual
 */
public class Turnador extends Thread{

    public void run() {

        while (true) {
            //Cada cinco segundos cambiamos el turno, si no hay nadie en el cruce, para intentar que no se quede pillado despertamos a uno

            Cruce.mutex.lock();
            Cruce.turno = (Cruce.turno + 1) % 3;

            switch (Cruce.turno){
                case 0: {
                    System.out.println("----Turno de Peatones----");
                    break;
                }
                case 1: {
                    System.out.println("----Turno de Norte-Sur----");
                    break;
                }
                case 2: {
                    System.out.println("----Turno de Este-Oeste----");
                    break;
                }
                default:
                    break;
            }
            Cruce.mutex.unlock();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
