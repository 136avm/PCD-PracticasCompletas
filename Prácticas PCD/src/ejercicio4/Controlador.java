package ejercicio4;

import messagepassing.MailBox;
import messagepassing.Selector;

/**
 * Clase Controlador
 * Clase encargada de asignar cajas y tiempo de compra a los clientes, y de gestionar el uso de las cajas y la pantalla.
 * Funciona continuamente sin parar.
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class Controlador extends Thread {
	// Propiedades
	private boolean pantallaUsada;
	private boolean[] cajasOcupadas;
	private Selector s;
	private MailBox buzonControladorControlador; 		// Buzón para asignarte caja y tiempo usado para recibir en el controlador
	private MailBox buzonPedirAControlador; 			// Buzón para pedir caja A usado para recibir en el controlador
	private MailBox buzonPedirBControlador; 			// Buzón para pedir caja B usado para recibir en el controlador
	private MailBox buzonLiberarAControlador; 			// Buzón para liberar caja A usado para recibir en el controlador
	private MailBox buzonLiberarBControlador; 			// Buzón para liberar caja B usado para recibir en el controlador
	private MailBox buzonPedirPantallaControlador; 		// Buzón para pedir pantalla usado para recibir en el controlador
	private MailBox buzonLiberarPantallaControlador; 	// Buzón para liberar pantalla usado para recibir en el controlador
	
	// Constructor
	/**
	 * Constructor de la clase Controlador.
	 * @param buzon1 Buzón para asignar caja y tiempo utilizado para recibir en el controlador.
	 * @param buzon2 Buzón para pedir caja A utilizado para recibir en el controlador.
	 * @param buzon3 Buzón para pedir caja B utilizado para recibir en el controlador.
	 * @param buzon4 Buzón para liberar caja A utilizado para recibir en el controlador.
	 * @param buzon5 Buzón para liberar caja B utilizado para recibir en el controlador.
	 * @param buzon6 Buzón para pedir pantalla utilizado para recibir en el controlador.
	 * @param buzon7 Buzón para liberar pantalla utilizado para recibir en el controlador.
	 */
	public Controlador(MailBox buzon1, MailBox buzon2, MailBox buzon3, MailBox buzon4, MailBox buzon5, MailBox buzon6, MailBox buzon7) {
		this.pantallaUsada = false;
		this.cajasOcupadas = new boolean[2];
		this.cajasOcupadas[0] = false; this.cajasOcupadas[1] = false;
		this.buzonControladorControlador = buzon1;
		this.buzonPedirAControlador = buzon2;
		this.buzonPedirBControlador = buzon3;
		this.buzonLiberarAControlador = buzon4;
		this.buzonLiberarBControlador = buzon5;
		this.buzonPedirPantallaControlador = buzon6;
		this.buzonLiberarPantallaControlador = buzon7;
		this.s = new Selector();
		s.addSelectable(buzonControladorControlador, false);
		s.addSelectable(buzonPedirAControlador, false);
		s.addSelectable(buzonPedirBControlador, false);
		s.addSelectable(buzonLiberarAControlador, false);
		s.addSelectable(buzonLiberarBControlador, false);
		s.addSelectable(buzonPedirPantallaControlador, false);
		s.addSelectable(buzonLiberarPantallaControlador, false);
	}
	
	// Método para que el controlador trabaje
	/**
	 * Método que simula el comportamiento de trabajo continuo del controlador.
	 * El controlador asigna cajas y tiempo de compra a los clientes, y gestiona el uso de las cajas y la pantalla.
	 * Funciona en un bucle continuo sin parar.
	 */
	public void run() {
		while(true) {
			//El buzón con el que se selecciona caja y tiempo de compra se puede leer siempre
			buzonControladorControlador.setGuardValue(true);
			//Los buzones de pedir la caja A y B solo se pueden leer si no están ocupadas
			buzonPedirAControlador.setGuardValue(cajasOcupadas[0]==false);
			buzonPedirBControlador.setGuardValue(cajasOcupadas[1]==false);
			//Los buzones de liberar la caja A y B solo se pueden leer si están ocupadas
			buzonLiberarAControlador.setGuardValue(cajasOcupadas[0]==true);
			buzonLiberarBControlador.setGuardValue(cajasOcupadas[1]==true);
			//El buzón de pedir la pantalla solo se puede leer si no está ocupada
			buzonPedirPantallaControlador.setGuardValue(pantallaUsada==false);
			//El buzón de liberar la pantalla solo se puede leer si está ocupada
			buzonLiberarPantallaControlador.setGuardValue(pantallaUsada==true);
			//Selecciona un buzón y actua acorde a ello
			switch(s.selectOrBlock()) {
			case 1:
				Object idObj = buzonControladorControlador.receive();
				int id = (int)idObj;
				int tiempo = (int) (Math.random() * 10) + 1;
				String caja = "B";
				if(tiempo>=5) {caja = "A";}
				String res = caja+":"+tiempo;
				Tienda.buzonControladorCliente[id].send(res);
				break;
			case 2:
				Object idObj2 = buzonPedirAControlador.receive();
				int id2 = (int)idObj2;
				Tienda.buzonPedirACliente[id2].send(id2);
				cajasOcupadas[0] = true;
				break;
			case 3:
				Object idObj3 = buzonPedirBControlador.receive();
				int id3 = (int)idObj3;
				Tienda.buzonPedirBCliente[id3].send(id3);
				cajasOcupadas[1] = true;
				break;
			case 4:
				Object idObj4 = buzonLiberarAControlador.receive();
				int id4 = (int)idObj4;
				Tienda.buzonLiberarACliente[id4].send(id4);
				cajasOcupadas[0] = false;
				break;
			case 5:
				Object idObj5 = buzonLiberarBControlador.receive();
				int id5 = (int)idObj5;
				Tienda.buzonLiberarBCliente[id5].send(id5);
				cajasOcupadas[1] = false;
				break;
			case 6:
				Object idObj6 = buzonPedirPantallaControlador.receive();
				int id6 = (int)idObj6;
				Tienda.buzonPedirPantallaCliente[id6].send(id6);
				pantallaUsada = true;
				break;
			case 7:
				Object idObj7 = buzonLiberarPantallaControlador.receive();
				int id7 = (int)idObj7;
				Tienda.buzonLiberarPantallaCliente[id7].send(id7);
				pantallaUsada = false;
				break;
			}
		}
	}
}