package entregable;

import messagepassing.MailBox;

public class Cliente extends Thread {
	// Propiedades
	private final int id; 							// Id para usar buzon en el array de buzones
	private String caja;							// Caja asignada
	private final int tiempoCompra; 				// Tiempo haciendo la compra (aleatorio por el cliente)
	private int tiempoPago; 						// Tiempo para pagar en caja (asignado por el controlador)
	private MailBox buzonControladorCliente; 		// Buzón para asignarte caja y tiempo usado para recibir en el cliente
	private MailBox buzonPedirACliente; 			// Buzón para pedir caja A usado para recibir en el cliente
	private MailBox buzonPedirBCliente; 			// Buzón para pedir caja B usado para recibir en el cliente
	private MailBox buzonLiberarACliente; 			// Buzón para liberar caja A usado para recibir en el cliente
	private MailBox buzonLiberarBCliente; 			// Buzón para liberar caja B usado para recibir en el cliente
	private MailBox buzonPedirPantallaCliente; 		// Buzón para pedir pantalla usado para recibir en el cliente
	private MailBox buzonLiberarPantallaCliente; 	// Buzón para liberar pantalla usado para recibir en el cliente
	
	// Constructor
	public Cliente(int id, MailBox buzon1, MailBox buzon2, MailBox buzon3, MailBox buzon4, MailBox buzon5, MailBox buzon6, MailBox buzon7) {
		this.id = id;
		this.tiempoCompra = (int)(Math.random() * 3)+1;
		this.buzonControladorCliente = buzon1;
		this.buzonPedirACliente = buzon2;
		this.buzonPedirBCliente = buzon3;
		this.buzonLiberarACliente = buzon4;
		this.buzonLiberarBCliente = buzon5;
		this.buzonPedirPantallaCliente = buzon6;
		this.buzonLiberarPantallaCliente = buzon7;
	}
	
	// Método para que el cliente compre
	public void run() {
		for(int i=0; i<5; i++) {
			// Esperar el tiempo que el cliente hace la compra
			try {
				Thread.sleep(tiempoCompra*1000);
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
				Thread.sleep(tiempoPago*1000);
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
			System.out.println("Persona " + id + " ha usado la caja " + caja +
								"\nTiempo de pago: " + tiempoPago +
								"\nThread.sleep(" + tiempoPago + ")" +
								"\n Persona " + id + " liberando la caja " + caja);
			// Libero la pantalla
			Tienda.buzonLiberarPantallaTienda.send(id);
			buzonLiberarPantallaCliente.receive();
		}
	}
}
