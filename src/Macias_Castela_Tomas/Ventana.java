package Macias_Castela_Tomas;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Como abrir el calendario. {@link #pulsarBoton()}
 * 
 * {@code
 * botonCalendario.addActionListener(e -> {
			Calendario calendar = new Calendario(); 
			calendar.setVisible(true);
 * 		}); 
 * }
 * 
 * @author Tomas Macias Castela.
 * 
 */
public class Ventana {

	JFrame vent;
	JButton botonCalendario;

	public Ventana() {
		this.vent = new JFrame("Calendario");
		vent.setBounds(0, 0, 150, 75);
		vent.setResizable(false);
		vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void inicializar() {
		vent.setVisible(true);

		vent.setLayout(new GridLayout());
		componentes();
		pulsarBoton();
	}

	public void componentes() {
		botonCalendario = new JButton();
		botonCalendario.setText("Calendario");
		botonCalendario.setIcon(new ImageIcon("./img/calendar.png"));
		botonCalendario.setHorizontalAlignment(SwingUtilities.LEFT);
		vent.add(botonCalendario);
		
	}

	public void pulsarBoton() {
		botonCalendario.addActionListener(e -> {
			Calendario calendar = new Calendario();
			calendar.setVisible(true);
		});
	}
}
