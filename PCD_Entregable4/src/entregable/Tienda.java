package entregable;

import messagepassing.MailBox;

public class Tienda {
	
	// Variables de uso global
	public static MailBox buzonControladorTienda = new MailBox(); 		// Buzón para asignarte caja y tiempo usado para recibir en la tienda
	public static MailBox buzonPedirATienda = new MailBox(); 			// Buzón para pedir caja A usado para recibir en la tienda
	public static MailBox buzonPedirBTienda = new MailBox(); 			// Buzón para pedir caja B usado para recibir en la tienda
	public static MailBox buzonLiberarATienda = new MailBox(); 			// Buzón para liberar caja A usado para recibir en la tienda
	public static MailBox buzonLiberarBTienda = new MailBox(); 			// Buzón para liberar caja B usado para recibir en la tienda
	public static MailBox buzonPedirPantallaTienda = new MailBox(); 	// Buzón para pedir pantalla usado para recibir en la tienda
	public static MailBox buzonLiberarPantallaTienda = new MailBox(); 	// Buzón para liberar pantalla usado para recibir en la tienda
	// ARRAYS DE BUZONES PARA CONECTAR CLIENTE Y CONTROLADOR
	public static MailBox[] buzonControladorCliente = new MailBox[30];
	public static MailBox[] buzonPedirACliente = new MailBox[30];
	public static MailBox[] buzonPedirBCliente = new MailBox[30];
	public static MailBox[] buzonLiberarACliente = new MailBox[30];
	public static MailBox[] buzonLiberarBCliente = new MailBox[30];
	public static MailBox[] buzonPedirPantallaCliente = new MailBox[30];
	public static MailBox[] buzonLiberarPantallaCliente = new MailBox[30];
	
	public static void main(String[] args) {
		for(int i=0; i<30; i++) {
			buzonControladorCliente[i] = new MailBox();
			buzonPedirACliente[i] = new MailBox();
			buzonPedirBCliente[i] = new MailBox();
			buzonLiberarACliente[i] = new MailBox();
			buzonLiberarBCliente[i] = new MailBox();
			buzonPedirPantallaCliente[i] = new MailBox();
			buzonLiberarPantallaCliente[i] = new MailBox();
		}
		
		Cliente[] clientes = new Cliente[30];
		for(int i=0; i<30; i++) {
			clientes[i] = new Cliente(i, buzonControladorCliente[i], buzonPedirACliente[i], buzonPedirBCliente[i], buzonLiberarACliente[i], buzonLiberarBCliente[i], buzonPedirPantallaCliente[i], buzonLiberarPantallaCliente[i]);
		}
		Controlador controlador = new Controlador(buzonControladorTienda, buzonPedirATienda, buzonPedirBTienda, buzonLiberarATienda, buzonLiberarBTienda, buzonPedirPantallaTienda, buzonLiberarPantallaTienda);
		controlador.start();
		for(int i=0; i<30; i++) {
			clientes[i].start();
		}
	}
}
