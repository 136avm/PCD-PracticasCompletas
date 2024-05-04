package ejercicio2;

/**
 * Clase que representa un coche que cruza.
 * 
 * Dependiendo de su dirección actual, manejará distintos semáforos manteniendo la misma lógica entre ambas posibles direcciones.
 * Intentará cruzar si le es posible e irá despertando otros hilos dependiendo de la circunstancia, para luego dar la vuelta y cambiar de dirección.
 * 
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class HiloCoche extends Thread {
    private Direcciones direccion = Direcciones.NORTE_SUR;

    @Override
    public void run() {
        try {
            while (true) {
                if (direccion.equals(Direcciones.NORTE_SUR)) {

                    Cruce.mutex.lock();
                    //Si no es nuestro turno, o no entramos o hay otra gente nos esperamos
                    if(Cruce.cochesEO > 0 || Cruce.peatones > 0 || Cruce.cochesNS >= Cruce.MAX_VEHICULOS || Cruce.turno != 1) {
                        Cruce.cochesNSesp++;
                        Cruce.mutex.unlock();

                        Cruce.colaNS.acquire();
                        Cruce.mutex.lock();
                        Cruce.cochesNSesp--;
                    }
                    //Al entrar avisamos y despertamos a alguien si hay hueco
                    Cruce.cochesNS++;
                    System.out.println("Coche cruzando de Norte a Sur");
                    if(Cruce.cochesNS < Cruce.MAX_VEHICULOS && Cruce.turno == 1 && Cruce.cochesNSesp > 0) {
                        Cruce.colaNS.release();
                    }

                    Cruce.mutex.unlock();

                    Thread.sleep(Cruce.TIEMPO_COCHE);


                    Cruce.mutex.lock();
                    //Al salir si podemos despertamos al siguiente de los nuestros
                    if(Cruce.cochesNSesp > 0 && Cruce.turno == 1) {
                        Cruce.colaNS.release();
                    }
                    Cruce.mutex.unlock();

                    Cruce.mutex.lock();

                    Cruce.cochesNS--;
                    //Si somos los últimos y no es nuestro turno, despertamos a los siguientes que les toque
                    if(Cruce.cochesNS == 0 && Cruce.turno !=1 && Cruce.cochesEOesp > 0) {
                        Cruce.colaEO.release();
                    }
                    Cruce.mutex.unlock();
                    //Nos damos la vuelta
                    Thread.sleep(Cruce.TIEMPO_VUELTA_COCHE);

                    direccion = Direcciones.ESTE_OESTE;
                } else {
                    //Mismo código, dirección cambiada
                    Cruce.mutex.lock();

                    if(Cruce.cochesNS > 0 || Cruce.peatones > 0 || Cruce.cochesEO >= Cruce.MAX_VEHICULOS || Cruce.turno != 2) {
                        Cruce.cochesEOesp++;
                        Cruce.mutex.unlock();

                        Cruce.colaEO.acquire();
                        Cruce.mutex.lock();

                        Cruce.cochesEOesp--;
                    }
                    Cruce.cochesEO++;
                    System.out.println("Coche cruzando de Este a Oeste");
                    if(Cruce.cochesEO < Cruce.MAX_VEHICULOS && Cruce.turno == 2 && Cruce.cochesEOesp > 0) {
                        Cruce.colaEO.release();
                    }
                    Cruce.mutex.unlock();

                    Thread.sleep(Cruce.TIEMPO_COCHE);


                    Cruce.mutex.lock();
                    Cruce.cochesEO--;
                    if(Cruce.cochesEOesp > 0 && Cruce.turno == 2) {
                        Cruce.colaEO.release();
                    }
                    Cruce.mutex.unlock();


                    Cruce.mutex.lock();


                    if(Cruce.cochesEO == 0 && Cruce.turno !=2 && Cruce.peatonesEsp > 0) {
                        Cruce.colaPE.release();
                    }
                    Cruce.mutex.unlock();

                    Thread.sleep(Cruce.TIEMPO_VUELTA_COCHE);

                    direccion = Direcciones.NORTE_SUR;
                }
            }
        } catch (InterruptedException e) {

        }

    }

}