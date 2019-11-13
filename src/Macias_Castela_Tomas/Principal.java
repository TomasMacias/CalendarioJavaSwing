package Macias_Castela_Tomas;

import java.awt.EventQueue;
/**
 * 
 * @author Tomas Macias Castela
 *
 */
public class Principal {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Ventana vent = new Ventana();
				vent.inicializar();
				
			}
		});
	}
	
	
}
