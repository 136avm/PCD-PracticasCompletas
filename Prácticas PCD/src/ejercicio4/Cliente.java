package ejercicio4;

import messagepassing.MailBox;

/**
 * Clase Cliente
 * Simula el comportamiento de un cliente normal en una tienda. Va siguiendo secuencialmente las acciones normales a
 * medida que el controlador le va dando acceso a las secciones críticas. Compra, pide caja, la usa, la libera e imprime
 * por pantalla su información.
 * @author Álvaro Aledo Tornero
 * @author Antonio Vergara Moya
 */
public class Cliente extends Thread {
	// Propiedades
	private final int id; 							// Id para usar buzon en el array de buzones
	private String caja;							// Caja asignada
	private int tiempoCompra; 				// Tiempo haciendo la compra (aleatorio por el cliente)
	private int tiempoPago; 						// Tiempo para pagar en caja (asignado por el controlador)
	private MailBox buzonControladorCliente; 		// Buzón para asignarte caja y tiempo usado para recibir en el cliente
	private MailBox buzonPedirACliente; 			// Buzón para pedir caja A usado para recibir en el cliente
	private MailBox buzonPedirBCliente; 			// Buzón para pedir caja B usado para recibir en el cliente
	private MailBox buzonLiberarACliente; 			// Buzón para liberar caja A usado para recibir en el cliente
	private MailBox buzonLiberarBCliente; 			// Buzón para liberar caja B usado para recibir en el cliente
	private MailBox buzonPedirPantallaCliente; 		// Buzón para pedir pantalla usado para recibir en el cliente
	private MailBox buzonLiberarPantallaCliente; 	// Buzón para liberar pantalla usado para recibir en el cliente
	
	// Constructor
	/**
	 * Constructor de la clase Cliente.
	 * @param id El identificador único del cliente.
	 * @param buzon1 Buzón para asignar caja y tiempo utilizado para recibir en el cliente.
	 * @param buzon2 Buzón para pedir caja A utilizado para recibir en el cliente.
	 * @param buzon3 Buzón para pedir caja B utilizado para recibir en el cliente.
	 * @param buzon4 Buzón para liberar caja A utilizado para recibir en el cliente.
	 * @param buzon5 Buzón para liberar caja B utilizado para recibir en el cliente.
	 * @param buzon6 Buzón para pedir pantalla utilizado para recibir en el cliente.
	 * @param buzon7 Buzón para liberar pantalla utilizado para recibir en el cliente.
	 */
	public Cliente(int id, MailBox buzon1, MailBox buzon2, MailBox buzon3, MailBox buzon4, MailBox buzon5, MailBox buzon6, MailBox buzon7) {
		this.id = id;
		this.tiempoCompra = 0;
		this.buzonControladorCliente = buzon1;
		this.buzonPedirACliente = buzon2;
		this.buzonPedirBCliente = buzon3;
		this.buzonLiberarACliente = buzon4;
		this.buzonLiberarBCliente = buzon5;
		this.buzonPedirPantallaCliente = buzon6;
		this.buzonLiberarPantallaCliente = buzon7;
	}
	
	// Método para que el cliente compre
	// A cada acción manda una solicitud al controlador para que los clientes vayan ordenadamente y con exclusión mutua
	/**
	 * Método que simula el comportamiento de compra del cliente.
	 * El cliente realiza varias acciones secuenciales como hacer la compra, solicitar una caja, pagar y liberar la caja.
	 * También imprime por pantalla su información después de cada acción.
	 */
	public void run() {
		for(int i=0; i<5; i++) {

			this.tiempoCompra = (int)(Math.random() * 3)+1;
			// Esperar el tiempo que el cliente hace la compra
			try {
				Thread.sleep(tiempoCompra * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Solicitar ponerme en caja
			Tienda.buzonControladorTienda.send(id);
			Object resultadosControlador = buzonControladorCliente.receive();
			String resultadosString = (String)resultadosControlador;
			// Analizar los datos del controlador
			String datos[] = resultadosString.split(":");
			this.caja = datos[0];
			this.tiempoPago = Integer.parseInt(datos[1]);
			// Pedir la caja que me han asignado
			if(caja.equals("A")) {
				Tienda.buzonPedirATienda.send(id);
				buzonPedirACliente.receive();
			} else if(caja.equals("B")) {
				Tienda.buzonPedirBTienda.send(id);
				buzonPedirBCliente.receive();
			}
			// Me espero porque tengo que pagar
			try {
				Thread.sleep(tiempoPago * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Libero mi caja
			if(caja.equals("A")) {
				Tienda.buzonLiberarATienda.send(id);
				buzonLiberarACliente.receive();
			} else if(caja.equals("B")) {
				Tienda.buzonLiberarBTienda.send(id);
				buzonLiberarBCliente.receive();
			}
				// Imprimo por pantalla mi información
			Tienda.buzonPedirPantallaTienda.send(id);
			buzonPedirPantallaCliente.receive();
			System.out.println("--------------------------------------------------------------" +
								"\nPersona " + id + " ha usado la caja " + caja +
								"\nTiempo de pago: " + tiempoPago +
								"\nThread.sleep(" + tiempoPago + ")" +
								"\nPersona " + id + " liberando la caja " + caja +
								"\n--------------------------------------------------------------");
			// Libero la pantalla
			Tienda.buzonLiberarPantallaTienda.send(id);
			buzonLiberarPantallaCliente.receive();
		}
	}
}