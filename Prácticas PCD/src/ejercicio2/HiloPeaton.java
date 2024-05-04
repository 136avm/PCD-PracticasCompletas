package ejercicio2;

/**
 * Clase que representa un peatón que cruza la intersección, se comporta de manera similar a los coches.
 * 
 * Intentará cruzar si le es posible e irá despertando otros hilos dependiendo de la circunstancia, para luego volver a empezar.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class HiloPeaton extends Thread{

    public void run() {
        try {
            while (true) {

                Cruce.mutex.lock();
                //Si no es nuestro turno o hay otra gente pasando o ya está el cruce lleno, nos esperamos a que nos avisen
                if(Cruce.cochesEO > 0 || Cruce.cochesNS > 0 || Cruce.peatones >= Cruce.MAX_PEATONES || Cruce.turno != 0) {
                    Cruce.peatonesEsp++;
                    Cruce.mutex.unlock();

                    Cruce.colaPE.acquire();
                    Cruce.peatonesEsp--;
                    Cruce.mutex.lock();

                }

                //Cuando nos avisen o hayamos podido entrar, entramos y vemos si podemos levantar a alguien más
                Cruce.peatones++;
                System.out.println("Peatón cruzando");
                if(Cruce.peatones < Cruce.MAX_PEATONES && Cruce.turno == 0 && Cruce.peatonesEsp > 0) {
                    Cruce.colaPE.release();
                }
                Cruce.mutex.unlock();
                //Empezamos a cruzar
                Thread.sleep(Cruce.TIEMPO_PEATON);


                Cruce.mutex.lock();
                //Cuando terminamos de cruzar despertamos a otro peatón si es que está esperando y es nuestro turno
                if(Cruce.peatonesEsp > 0 && Cruce.turno == 0 ) {
                    Cruce.colaPE.release();
                }
                Cruce.mutex.unlock();


                Cruce.mutex.lock();

                Cruce.peatones--;
                //Si somos los últimos, dependiendo de quienes estén y el turno despertamos a unos u otros
                if(Cruce.peatones == 0 && Cruce.turno !=0 && Cruce.cochesNSesp > 0) {

                    Cruce.colaNS.release();
                }
                Cruce.mutex.unlock();
                // Nos damos la vuelta
                Thread.sleep(Cruce.TIEMPO_VUELTA_PEATON);



            }
        } catch (InterruptedException e) {
            // TODO: handle exception
        }

    }
}