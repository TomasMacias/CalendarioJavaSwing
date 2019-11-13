package Macias_Castela_Tomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Inicializacion visualmente del calendario. {@link #insertarDias(int, int)}
 * 
 * @author Tomas Macias Castela.
 * @version 2.0
 * 
 */
public class Calendario extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8869075441821100033L;
	JComboBox<String> mes;
	JComboBox<String> year = new JComboBox<String>();

	String[] month = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Septiembre", "Octube",
			"Noviembre", "Diciembre" };
	String[] dia = { "Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom" };
	String[] opExiste = { "Leer", "Modificar", "Borrar" };

	JButton[] botonDia = new JButton[42];
	JPanel[] panelNumDia = new JPanel[42];
	JPanel panelEncabezado, panelDia;
	JLabel[] textoDias = new JLabel[7];
	JLabel mesYearActual;

	int yearPrincipio = 60, yearFinal = 60;
	int yearActual, mesSelec, numDia, maxDia, diaPrincipal, mesReal;

	GregorianCalendar cal = new GregorianCalendar();
	GridBagConstraints gbc = new GridBagConstraints();
	Font texto = new Font("Arial", Font.BOLD, 11);

	File dirMes, dirYear, fileDia;

	Icon iconoNota = new ImageIcon(".//img//escribirCalendario.png");
	Icon iconoBorrar = new ImageIcon(".//img//borrarCalendario.png");
	Icon iconoLeer = new ImageIcon(".//img//leerCalendario.png");

	Calendar calendario = Calendar.getInstance();
	int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

	public Calendario() {
		setModal(true);
		setBounds(0, 0, 450, 350);
		setResizable(false);
		/*
		 * Abrimos el calendario abajo a la izquierda. Para ello conseguimos la
		 * dimension de la pantalla de alto y horizontal.
		 */
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		Rectangle rect = gd.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - this.getWidth();
		int y = (int) rect.getMaxY() - (this.getHeight() + 50);
		setLocation(x, y);

		this.setLayout(new GridBagLayout());

		iniciarComboBox();
		insertarEncabezado();
		insertarPanelBoton();
		insertarDias(mesSelec, yearActual);
		cambiarMes();
		cambiarYear();
	}

	/**
	 * Metodo para iniciarlizar el JComboBox.
	 */
	public void iniciarComboBox() {
		// Sacamos el año actual
		yearActual = cal.get(GregorianCalendar.YEAR);
		// Sacamos el mes actual.
		mesReal = cal.get(GregorianCalendar.MONTH);
		// Seleccionamos el mes.
		mesSelec = mesReal - 1;
		mes = new JComboBox<String>(month);
		// El mes seleccionado por defecto.
		mes.setSelectedIndex(mesSelec);
		// Añadimos la lista de los años.
		for (int i = yearActual - yearPrincipio; i < yearActual + yearFinal; i++) {
			year.addItem(String.valueOf(i));
		}
		// El año seleccionado por defecto.
		year.setSelectedIndex(yearPrincipio);
	}

	/**
	 * Metodo para inicializarEncabezado.
	 */
	public void insertarEncabezado() {
		panelEncabezado = new JPanel();
		panelEncabezado.setLayout(new GridBagLayout());
		// JComboBox mes.
		panelEncabezado.add(mes);
		// Mes y año seleccionado.
		gbc = new GridBagConstraints();
		mesYearActual = new JLabel(mes.getItemAt(mesSelec) + "-" + yearActual);
		mesYearActual.setHorizontalAlignment(SwingUtilities.CENTER);
		gbc.ipadx = 30;
		panelEncabezado.add(mesYearActual, gbc);
		// JComboBox año.
		panelEncabezado.add(year);
		gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(panelEncabezado, gbc);
	}

	/**
	 * Metodo para crear los botones y paneles e insertarlos.
	 */
	public void insertarPanelBoton() {
		gbc = new GridBagConstraints();
		panelDia = new JPanel();
		panelDia.setLayout(new GridLayout(7, 7));
		// Insertar el encabezado.
		for (int i = 0; i < textoDias.length; i++) {
			textoDias[i] = new JLabel();
			textoDias[i].setText(dia[i]);
			textoDias[i].setHorizontalAlignment(SwingUtilities.CENTER);
			panelDia.add(textoDias[i]);
		}
		// Insertar los botones dentro del panel.
		for (int j = 0; j < panelNumDia.length; j++) {
			panelNumDia[j] = new JPanel();
			botonDia[j] = new JButton();
			botonDia[j].setMinimumSize(new Dimension(50, 25));
			botonDia[j].setMaximumSize(new Dimension(50, 25));
			botonDia[j].setPreferredSize(new Dimension(50, 25));
			botonDia[j].setEnabled(false);
			botonDia[j].setBackground(Color.WHITE);
			panelNumDia[j].add(botonDia[j]);
			panelDia.add(panelNumDia[j]);
		}
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(panelDia, gbc);
	}

	/**
	 * Metodo para insertar los dias.
	 * @param mesSelec Mes seleccionado del JComboBox.
	 * @param yearActual Año seleccionado del JComboBox.
	 */
	public void insertarDias(int mesSelec, int yearActual) {
		// Obtenemos el calendario pasando el año, el mes y el dia 1. Al mes se le suma
		// uno porque empieza desde 1 y no desde 0.
		cal = new GregorianCalendar(yearActual, mesSelec + 1, 1);
		// Maximos de dias del mes.
		maxDia = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		// El dia 1 que dia de la semana cae.
		diaPrincipal = cambiarDiaPrincipal(cal.get(GregorianCalendar.DAY_OF_WEEK));

		/*
		 * Incializamos todos los botones por defecto. Color blanco, deshabilitamos los
		 * listeners, ponemos el texto vacio y los dehabilitamos.
		 */
		for (int j = 0; j < botonDia.length; j++) {
			botonDia[j].setText("");
			botonDia[j].setEnabled(false);
			botonDia[j].setBackground(Color.WHITE);
			if (botonDia[j].getActionListeners().length > 0) {
				botonDia[j].removeActionListener(botonDia[j].getActionListeners()[0]);
			}

		}
		dirYear = new File(".//txt//" + String.valueOf(yearActual)); // Creamos la ruta para el directorio año.
		dirMes = new File(dirYear + "//" + String.valueOf(mesSelec)); // Creamos la ruta para el directorio mes.
		String[] fichMes = dirMes.list(); // Obtenemos el listado de la ruta de año/mes.
		if (dirMes.exists()) { // Si existe.
			if (fichMes.length == 0) { // Si está vacio.
				dirMes.delete(); // Borramos el directorio.
			}
		}

		// Insertamos los digitos en sus respectivos dias.
		for (int i = 1; i <= maxDia; i++) {
			botonDia[diaPrincipal].setText(String.valueOf(i)); // Insertamos su correspondiente dia.
			botonDia[diaPrincipal].setEnabled(true); // Habilitamos ese boton.

			if (i == diaActual && mesReal == mesSelec + 1) { // Coloreamos el dia en el que estamos.
				botonDia[diaPrincipal].setBackground(Color.LIGHT_GRAY);
			}
			if (new File(dirMes + "//" + i + ".txt").exists()) { // Si hay un fichero que exista. Modificamos su color.
				botonDia[diaPrincipal].setBackground(Color.GREEN);
			}

			diaPrincipal++;
		}
		accionBoton(mesSelec, yearActual);
	}

	/**
	 * Metodo para cambiar la posicion de dias.
	 * 
	 * @param diaPrincipal Pasamos por parametro el día en el que estamos.
	 * @return retornamos la nueva posicion.
	 */
	public int cambiarDiaPrincipal(int diaPrincipal) {
		if (diaPrincipal == 1) {
			diaPrincipal = 6; // Domingo.
		} else if (diaPrincipal == 2) {
			diaPrincipal = 0; // Lunes.
		} else if (diaPrincipal == 3) {
			diaPrincipal = 1; // Martes.
		} else if (diaPrincipal == 4) {
			diaPrincipal = 2; // Miercoles.
		} else if (diaPrincipal == 5) {
			diaPrincipal = 3; // Jueves.
		} else if (diaPrincipal == 6) {
			diaPrincipal = 4; // Viernes.
		} else if (diaPrincipal == 7) {
			diaPrincipal = 5; // Sabado.
		}
		return diaPrincipal;
	}

	/**
	 * Accion si pulsamos el JComboBox para modificar el mes.
	 */
	public void cambiarMes() {
		mes.addActionListener(e -> {
			if (mes.getSelectedItem() != null) {
				mesSelec = mes.getSelectedIndex();
				mesYearActual.setText(mes.getItemAt(mesSelec) + "-" + yearActual);
				insertarDias(mesSelec, yearActual);

			}
		});
	}

	/**
	 * Accion si pulsamos el JComboBox para modificar el año..
	 */
	public void cambiarYear() {
		year.addActionListener(e -> {
			if (year.getSelectedItem() != null) {
				String y = year.getSelectedItem().toString();
				yearActual = Integer.parseInt(y);
				mesYearActual.setText(mes.getItemAt(mesSelec) + "-" + yearActual);
				insertarDias(mesSelec, yearActual);
			}
		});
	}

	/**
	 * Metodo para realizar la accion de pulsado de un boton.
	 * 
	 * @param mesSelec   Mes seleccionado en el JComboBox.
	 * @param yearActual Año seleccionado en el JComboBox.
	 */
	public void accionBoton(int mesSelec, int yearActual) {
		cal = new GregorianCalendar(yearActual, mesSelec + 1, 1);
		// Maximos de dias del mes.
		maxDia = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		// El dia 1 que dia de la semana cae.
		diaPrincipal = cambiarDiaPrincipal(cal.get(GregorianCalendar.DAY_OF_WEEK));
		for (int i = 1; i <= maxDia; i++) {
			int pos = diaPrincipal;
			botonDia[diaPrincipal].addActionListener(e -> {
				botonDia[pos].setBackground(Color.GREEN);
				escribirNota(botonDia[pos]);
			});
			diaPrincipal++;
		}
	}

	/**
	 * Metodo para comenzar a escribir nuestras notas/avisos en el calendario.
	 * 
	 * @param botonDia El boton que hemos seleccionado.
	 * 
	 */
	public void escribirNota(JButton botonDia) {

		dirYear.mkdir(); // Creamos los directorios si no están creados. Si ya están creados no pasa
							// nada.
		dirMes.mkdir();
		fileDia = new File(dirMes + "//" + botonDia.getText() + ".txt"); // Creamos la ruta del fichero.
		if (fileDia.exists()) {
			int respuesta = JOptionPane.showOptionDialog(null, "Ya hay algo escrito. \n¿Qué deseas hacer?",
					botonDia.getText() + "-" + mesYearActual.getText(), JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, opExiste, opExiste[0]);
			modificacionNota(respuesta, fileDia, botonDia.getText());
		} else {
			try {
				String nota = (String) JOptionPane.showInputDialog(null, "Agregar una nota",
						botonDia.getText() + "-" + mesYearActual.getText(), JOptionPane.INFORMATION_MESSAGE, iconoNota,
						null, "");
				if (nota != null && (nota.length() != 0)) { // Si la nota no es nula y tiene caracteres insertados.
					PrintWriter pw = new PrintWriter(new FileWriter(fileDia, true));
					pw.write(nota);
					pw.write("\n");
					pw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Actualizamos el calendario.
		insertarDias(mesSelec, yearActual);
	}

	/**
	 * Metodo para realizar x tarea dependiendo de la opcion que le llegue.
	 * 
	 * @param respuesta 0: Significa que va a leer lo que hay escrito. 1: Realizas
	 *                  una modificacion en la nota. 2: Borras la nota.
	 * @param fileDia   Le pasas la ruta del fichero donde se encuentra.
	 * 
	 * @param dia El dia donde hemos seleccionado.
	 */
	public void modificacionNota(int respuesta, File fileDia, String dia) {
		switch (respuesta) {
		case 0:
			String cadena;
			String texto = "";
			try {
				BufferedReader br = new BufferedReader(new FileReader(fileDia));
				while ((cadena = br.readLine()) != null) {
					texto += cadena + "\n";
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, texto, dia + "-" + mesYearActual.getText(),
					JOptionPane.INFORMATION_MESSAGE, iconoLeer);
			break;
		case 1:
			try {
				String nota = (String) JOptionPane.showInputDialog(null, "Modificar nota.",
						dia + "-" + mesYearActual.getText(), JOptionPane.INFORMATION_MESSAGE, iconoNota, null, "");
				PrintWriter pw = new PrintWriter(new FileWriter(fileDia, true));
				if (nota != null && (nota.length() != 0)) {
					pw.write(nota);
					pw.write("\n");
				}
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			if (JOptionPane.showConfirmDialog(null, "¿Estás seguro?", "Borrado de nota", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, iconoBorrar) == 0) {
				fileDia.delete();
			}
			break;

		default:
			break;
		}
	}

}
