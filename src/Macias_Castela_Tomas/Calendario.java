package Macias_Castela_Tomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Inicializacion visualmente del calendario. {@link #insertarDias(int, int)}
 * 
 * Obtener informacion para comprobar si existe algun aviso o no.
 * {@link #informacionBoton(JButton)}
 * 
 * @author Tomas Macias Castela.
 * @version 2.1
 * 
 */
public class Calendario extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8869075441821100033L;
	JComboBox<String> mes;
	JComboBox<String> year = new JComboBox<String>();
	// Array de los meses y los dias.
	String[] month = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octube",
			"Noviembre", "Diciembre" };
	String[] dia = { "Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom" };
	// Array con diferentes opciones.
	String[] opExiste = { "Leer", "Modificar", "Borrar" };
	String[] opAviso = { "Leer", "Saltar" };

	JButton[] botonDia = new JButton[42];
	JPanel[] panelNumDia = new JPanel[42];
	JPanel panelEncabezado, panelDia;
	JLabel[] textoDias = new JLabel[7];
	JLabel mesYearActual;

	int yearPrincipio = 60, yearFinal = 60;
	int yearActual, mesActual, mesSelec, maxDiaMes, diaPrincipal;

	GregorianCalendar cal = new GregorianCalendar();
	GridBagConstraints gbc = new GridBagConstraints();
	// Fuente nueva para el texto de los botones.

	// Nombre de directorios y ficheros.
	File dirMes, dirYear, fileDia;

	// Bordes para añadir a los dias.
	Border borde = BorderFactory.createMatteBorder(2, 0, 2, 1, Color.BLACK);
	Border bordeIzq = BorderFactory.createMatteBorder(2, 2, 2, 1, Color.BLACK);
	Border bordeDer = BorderFactory.createMatteBorder(2, 0, 2, 2, Color.BLACK);

	// Conversor de iconos para los JOptionPanel.
	Icon iconoNota = new ImageIcon(".//img//escribirCalendario.png");
	Icon iconoBorrar = new ImageIcon(".//img//borrarCalendario.png");
	Icon iconoLeer = new ImageIcon(".//img//leerCalendario.png");
	Icon iconoAviso = new ImageIcon(".//img//avisoCalendario.png");

	// Obtenemos la información del día en el que estamos.
	Calendar calendario = Calendar.getInstance();
	int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

	/**
	 * Variables para obtener la información del dia siguiente. yActual: Año actual
	 * convertido en String, mActual: Mes actual convertido en String. dActual: Dia
	 * actual convertido en String.
	 * 
	 * Tenemos que sumar 1 al mes porque "Calendar.MONTH" nos da desde 0(Enero)
	 * hasta 11(Diciembre) así realizamos correctamente la comprobacion del día
	 * siguiente.
	 */
	String yActual = String.valueOf(calendario.get(Calendar.YEAR)),
			mActual = String.valueOf(calendario.get(Calendar.MONTH) + 1), dActual = String.valueOf(diaActual);

	// Obtenemos el dia siguiente.
	LocalDate fecha = LocalDate.parse(yActual + "-" + mActual + "-" + dActual);
	LocalDate diaSiguiente = fecha.plusDays(1);

	public Calendario() {
		setModal(true);
		setBounds(0, 0, 450, 350);
		setResizable(false);
		getContentPane().setBackground(Color.LIGHT_GRAY);
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
		// Realizamos la comprobación de si tenemos una nota guardada para el dia
		// siguiente.
		if (new File(".//txt//" + diaSiguiente.getYear() + "//" + (diaSiguiente.getMonthValue() - 1) + "//"
				+ (diaSiguiente.getDayOfMonth()) + ".txt").exists()) {
			avisoNota();

		}
		// Inicialización de los componentes.
		iniciarComboBox();
		insertarEncabezado();
		insertarPanelBoton();
		insertarDias(mesSelec, yearActual);
		// ActionListener de los JComboBox.
		cambiarMesYear();
	}

	/**
	 * Metodo para iniciarlizar el JComboBox.
	 */
	public void iniciarComboBox() {
		// Sacamos el año actual.
		yearActual = cal.get(GregorianCalendar.YEAR);
		// Sacamos el mes actual.
		mesActual = cal.get(GregorianCalendar.MONTH);

		// Seleccionamos el mes.
		mesSelec = mesActual;
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
		panelEncabezado.setBackground(Color.LIGHT_GRAY);
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
		panelDia.setBackground(Color.LIGHT_GRAY);
		panelDia.setLayout(new GridLayout(7, 7));
		// Insertar el encabezado.
		for (int i = 0; i < textoDias.length; i++) {
			textoDias[i] = new JLabel();
			textoDias[i].setText(dia[i]);
			textoDias[i].setHorizontalAlignment(SwingUtilities.CENTER);
			gbc.ipady = 10;
			if (i == 0) {
				textoDias[i].setBorder(bordeIzq);
			} else if (i == 6) {
				textoDias[i].setBorder(bordeDer);
			} else {
				textoDias[i].setBorder(borde);
			}

			panelDia.add(textoDias[i]);
		}
		// Insertar los botones dentro del panel.
		for (int j = 0; j < panelNumDia.length; j++) {
			panelNumDia[j] = new JPanel();
			panelNumDia[j].setBackground(Color.LIGHT_GRAY);
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
	 * Metodo para insertar los dias. Primeramente obtenemos el calendario pasando
	 * por parametros nuestro mes y año seleccionado del JComboBox. Obtenemos el
	 * maximo de dias y que dia de la semana comienza el mes. Borramos la
	 * informacion si contiene datos. Insertamos los dias en su respectivo boton.
	 * 
	 * @param mesSelec   Mes seleccionado del JComboBox.
	 * @param yearActual Año seleccionado del JComboBox.
	 */
	public void insertarDias(int mesSelec, int yearActual) {
		/*
		 * Obtenemos el calendario pasando el año, el mes y el dia 1. Al mes se le suma
		 * uno porque empieza desde 1 y no desde 0.
		 */
		cal = new GregorianCalendar(yearActual, mesSelec, 1);
		// Maximos de dias del mes. [1-n].
		maxDiaMes = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		// El dia de la semana comienza el mes [0-6].
		diaPrincipal = cambiarDiaPrincipal(cal.get(GregorianCalendar.DAY_OF_WEEK));
		/*
		 * Incializamos todos los botones por defecto y los dehabilitamos. Color rosa,
		 * deshabilitamos los listeners, ponemos el texto vacio.
		 */
		for (int j = 0; j < botonDia.length; j++) {
			botonDia[j].setText("");
			botonDia[j].setEnabled(false);
			botonDia[j].setBackground(Color.PINK);
			if (botonDia[j].getMouseListeners().length > 0) {
				botonDia[j].removeMouseListener(botonDia[j].getMouseListeners()[0]);
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

		String[] fichYear = dirYear.list();
		if (dirYear.exists()) { // Si existe.
			if (fichYear.length == 0) { // Si está vacio.
				dirYear.delete(); // Borramos el directorio.
			}
		}

		// Insertamos los digitos en sus respectivos dias.
		for (int i = 1; i <= maxDiaMes; i++) {
			botonDia[diaPrincipal].setText(String.valueOf(i)); // Insertamos su correspondiente dia.
			botonDia[diaPrincipal].setEnabled(true); // Habilitamos ese boton.
			botonDia[diaPrincipal].setBackground(Color.WHITE); // Cambiamos el color a blanco.

			if (i == diaActual && mesActual == mesSelec) {
				botonDia[diaPrincipal].setBackground(new Color(3, 151, 211)); // Coloreamos el dia en el que estamos.
			}
			if (new File(dirMes + "//" + i + ".txt").exists()) {
				botonDia[diaPrincipal].setBackground(new Color(14, 246, 74)); // Si hay un fichero que exista.
																				// Modificamos su color.
			}

			diaPrincipal++;
		}
		accionRaton(mesSelec, yearActual);
	}

	/**
	 * Metodo para realizar la accion del raton dependiendo si hace click o pasa por
	 * encima y sale.
	 * 
	 * @param mesSelec   Mes seleccionado en el JComboBox.
	 * @param yearActual Año seleccionado en el JComboBox.
	 */
	public void accionRaton(int mesSelec, int yearActual) {
		cal = new GregorianCalendar(yearActual, mesSelec, 1);
		// Maximos de dias del mes.
		maxDiaMes = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		// El dia 1 que dia de la semana cae.
		diaPrincipal = cambiarDiaPrincipal(cal.get(GregorianCalendar.DAY_OF_WEEK));
		for (int i = 1; i <= maxDiaMes; i++) {
			int pos = diaPrincipal;
			int dA = i;
			botonDia[diaPrincipal].addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				// Si dejamos de hacer foco con el raton.
				@Override
				public void mouseExited(MouseEvent e) {
					if (dA == diaActual && mesActual == mesSelec) { // Coloreamos el dia en el que estamos.
						botonDia[pos].setBackground(new Color(3, 151, 211));
					} else if (new File(dirMes + "//" + dA + ".txt").exists()) { // Si hay un fichero que exista.
						botonDia[pos].setBackground(new Color(14, 246, 74)); // Modificamos su color.
					} else {
						botonDia[pos].setBackground(Color.WHITE); // Coloreamos los demas a su color natural.
					}
				}

				// Si hacemos foco en un boton.
				@Override
				public void mouseEntered(MouseEvent e) {
					botonDia[pos].setBackground(Color.CYAN);
				}

				// Si pulsamos sobre un boton.
				@Override
				public void mouseClicked(MouseEvent e) {
					botonDia[pos].setBackground(Color.GREEN);
					informacionBoton(botonDia[pos]);
				}
			});
			diaPrincipal++;
		}
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
	 * Accion si realizamos un cambio en el JComboBox del mes o del año.
	 */
	public void cambiarMesYear() {
		mes.addActionListener(e -> {
			if (mes.getSelectedItem() != null) {
				mesSelec = mes.getSelectedIndex();
				mesYearActual.setText(mes.getItemAt(mesSelec) + "-" + yearActual);
				insertarDias(mesSelec, yearActual);
			}
		});

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
	 * Metodo que obtenemos la información que contiene ese boton. Si contiene nota
	 * o está vacio. Lo sabremos por como está coloreado el boton.
	 * 
	 * @param botonDia El boton que hemos seleccionado.
	 * 
	 */
	public void informacionBoton(JButton botonDia) {
		/*
		 * Creamos los directorios si no están creados. Si ya están creados no pasa
		 * nada.
		 */
		dirYear.mkdir();
		dirMes.mkdir();

		fileDia = new File(dirMes + "//" + botonDia.getText() + ".txt"); // Creamos la ruta del fichero.

		if (fileDia.exists()) {
			int respuesta = JOptionPane.showOptionDialog(this, "Ya hay algo escrito. \n¿Qué deseas hacer?",
					botonDia.getText() + "-" + mesYearActual.getText(), JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, opExiste, opExiste[0]);
			modificacionNota(respuesta, fileDia, botonDia.getText());
		} else {
			try {
				String nota = (String) JOptionPane.showInputDialog(this, "Agregar una nota",
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
	 * @param dia       El dia donde hemos seleccionado.
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
			JOptionPane.showMessageDialog(this, texto, dia + "-" + mesYearActual.getText(),
					JOptionPane.INFORMATION_MESSAGE, iconoLeer);
			break;
		case 1:
			try {
				String nota = (String) JOptionPane.showInputDialog(this, "Modificar nota.",
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
			if (JOptionPane.showConfirmDialog(this, "¿Estás seguro?", "Borrado de nota", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, iconoBorrar) == 0) {
				fileDia.delete();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Metodo para mostrar un aviso si al día siguiente tienes una nota guardada. 0:
	 * leer nota. 1: Saltar.
	 * 
	 * Debemos de restar 1 al mes ya que diaSiguiente se compone de [1-12] mientras
	 * que nuestros ficheros se guardan de [0-11].
	 *
	 */
	public void avisoNota() {
		File aviso = new File(".//txt//" + diaSiguiente.getYear() + "//" + (diaSiguiente.getMonthValue() - 1) + "//"
				+ diaSiguiente.getDayOfMonth() + ".txt");
		String fecha = diaSiguiente.getDayOfMonth() + "-" + month[diaSiguiente.getMonthValue() - 1] + "-"
				+ diaSiguiente.getYear();

		if (aviso.exists()) {
			// Si la respuesta es leer.
			if (JOptionPane.showOptionDialog(this, "Tienes algo pendiente para mañana. \n¿Que deseas hacer?", "¡Aviso!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, iconoAviso, opAviso, opAviso[0]) == 0) {
				String cadena;
				String texto = "";
				try {
					BufferedReader br = new BufferedReader(new FileReader(aviso));
					while ((cadena = br.readLine()) != null) {
						texto += cadena + "\n";
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(this, texto, fecha, JOptionPane.INFORMATION_MESSAGE, iconoLeer);
			}
		}
	}

}
