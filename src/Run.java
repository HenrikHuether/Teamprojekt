import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Main Class
 * 
 * @author henri
 *
 */
@SuppressWarnings("serial")
public class Run extends JFrame implements ActionListener {

	/*
	 * #############################################################################
	 * #############################################################################
	 * ###### Allgemeine Variablen
	 */
	Daten daten = new Daten();
	// Fenster einstellungen
	int WindowSizeX1 = 0;
	int WindowSizeY1 = 0;
	int WindowSizeX2 = 800;
	int WindowSizeY2 = 800;

	/**
	 * Farbschema: Default,Schwarz, Blau, Rot
	 */
	public void ColorDesign() {
		String Ausgewaehlt = "Default";
		Color Schwarz = new Color(0, 0, 0);
		String Blau = "#1736E6";
		String Rot = "#E30302";
	}

	public Run() {
		starte_FensterLogin();
		// starte_FensterStartmenue();
		this.setWindowDimension(WindowSizeX1, WindowSizeY1, WindowSizeX2, WindowSizeY2);
	}

	public void setWindowDimension(int x1, int y1, int x2, int y2) {
		setBounds(x1, y1, x2, y2);
	}

	// Main Methode
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		Run run = new Run();
	}

	/*
	 * #############################################################################
	 * #############################################################################
	 * ###### Action Listener
	 */
	@Override

	public void actionPerformed(ActionEvent e) {

		// Fenster login
		if (e.getSource() == login.btnLogin) {
			if (!login.isUserInputValid()) {
				JOptionPane.showMessageDialog(null, "You have to enter a name and password!", "Login error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (login.isAccountValid()) {
					login.dispose();
					String user = login.getUser();
					starte_FensterStartmenue();
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect account", "Login", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == login.btnRegister) {
			if (!login.isUserInputValid()) {
				JOptionPane.showMessageDialog(null, "You have to enter a name and password!", "Login error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (login.registerUser()) {
					login.dispose();
					String user = login.getUser();
					starte_FensterStartmenue();
				} else {
					JOptionPane.showMessageDialog(null, "Cannot register user", "Register", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
		// FensterStarmenue
		if (e.getSource() == Startmenue.rdbtnTonAus) {

			if (Startmenue.rdbtnTonAus.isSelected() == false) {
				System.out.println("Resume playing Audio");
				// playAudio();
				playRandomAudio();
			} else {
				System.out.println("Stop Playing Audio");
				this.stopAudio();
			}
		}
		if (e.getSource() == Startmenue.btnZumSpiel) {
			Restart();
			starte_FensterSpiel();
		}
		if (e.getSource() == Startmenue.btnDeinProfil) {
			starte_Spielerprofil();
		}
		if (e.getSource() == Startmenue.btnEinstellungen) {
			starte_FensterEinstellungen();
		}
		// FensterSpiel
		try {// falls Fenster nicht Konstuiert ist
			if (e.getSource() == Spiel.btnNext) {
				GameWindow_nextQuestion();
			}
			if (e.getSource() == Spiel.buttonBack) {
				System.out.println("beende spiel");
				allePanelsSchliessen();
				starte_FensterStartmenue();
			}
		} catch (Exception e4) {
			// Fenster noch nicht konsturiert
		}

		// FensterSpielerprofil
		if (e.getSource() == Spielerprofil.buttonback) {
			allePanelsSchliessen();
			starte_FensterStartmenue();
		}
		// FensterHighscore
		try {
			if (e.getSource() == Highscore.btnZurckZumMen) {
				allePanelsSchliessen();
				starte_FensterStartmenue();
			}
		} catch (Exception e2) {
			System.out.println("Execpt 2");
		}

		// FensterEinstellungen
		if (e.getSource() == Einstellungen.buttonBack) {
			allePanelsSchliessen();
			starte_FensterStartmenue();

		}
	}

	/*
	 * #############################################################################
	 * #############################################################################
	 * ###### Fenster Logik
	 */
	public void allePanelsSchliessen() {
		try {
			this.remove(login.contentPane);
			this.remove(Startmenue.contentPane);
			this.remove(Einstellungen.contentPane);
			this.remove(Spielerprofil.contentPane);
			this.remove(Spiel.contentPane);
			this.remove(Highscore.contentPane);

		} catch (Exception e) {
			System.out.println("Warnung:Es sind noch nicht alle Fenster erstellt worden.");
		}

		this.repaint();
	}

	boolean ersteIterationFuerLogin = true;
	FensterLogin login = new FensterLogin();

	public void starte_FensterLogin() {
		System.out.println("Create Login");
		allePanelsSchliessen();
		this.add(login.contentPane);
		this.setVisible(true);
		login.btnLogin.addActionListener(this);
		login.btnRegister.addActionListener(this);
		ersteIterationFuerLogin = false;
	}

	boolean ersteIterationFuerStartmenue = true;
	FensterStartmenue Startmenue = new FensterStartmenue(daten);
	public void starte_FensterStartmenue() {
		//playAudio();
		if(isAudioPlaying==false) {
			playRandomAudio();
		}
		
		System.out.println("Create Startmenue");
		allePanelsSchliessen();
		this.add(Startmenue.contentPane);
		this.setVisible(true);
		if (ersteIterationFuerStartmenue) {
			// ActionListener
			Startmenue.btnZumSpiel.addActionListener(this);
			Startmenue.btnDeinProfil.addActionListener(this);
			Startmenue.btnEinstellungen.addActionListener(this);
			Startmenue.rdbtnTonAus.addActionListener(this);
			ersteIterationFuerStartmenue = false;
		}
		setColor(Startmenue.contentPane);

	}

	private void setColor(JPanel panel) {
		if (Einstellungen.color != null) {
			panel.setBackground(Einstellungen.color);
		}
	}

	boolean ersteIterationFuerSpiel = true;
	FensterSpiel Spiel = new FensterSpiel();

	String lerneinheit;

	public void starte_FensterSpiel() {
		lerneinheit = Startmenue.holeAusgewaehltesSpiel();
		System.out.println("Create Spiel");
		allePanelsSchliessen();
		this.add(Spiel.contentPane);
		this.setVisible(true);

		Spiel.btnNext.removeActionListener(this);
		Spiel.buttonBack.removeActionListener(this);

		Spiel.btnNext.addActionListener(this);
		Spiel.buttonBack.addActionListener(this);
		/**
		 * if(ersteIterationFuerSpiel) { // ActionListener
		 * Spiel.btnNext.addActionListener(this);
		 * Spiel.buttonBack.addActionListener(this); ersteIterationFuerSpiel=false; }
		 */
		ladeLerneinheit();

	}

	public void ladeLerneinheit() {
		System.out.println("Lade Lerneinheit");
		String dateiname = daten.getFilenameForLerneinheit(lerneinheit);
		if (dateiname != null) {
			System.out.println("Lade Spieldaten: " + "src\\Lerneinheiten\\" + dateiname);
			daten.loadTxtFile("src\\Lerneinheiten\\" + dateiname);
			// daten.loadTxtFile("src/Lerneinheiten/" + dateiname);
			GameWindow_nextQuestion();// erste Frage einblenden
		} else {
			System.err.println("Keine Datei für Lerneinheit " + lerneinheit);
		}
		setColor(Spiel.contentPane);
	}

	boolean ersteIterationFuerSpielerprofil = true;// Operationen wie action Listener nur einmal ausführen!
	FensterSpielerprofil Spielerprofil = new FensterSpielerprofil();

	public void starte_Spielerprofil() {
		System.out.println("Create Spielerprofil");
		allePanelsSchliessen();
		this.add(Spielerprofil.contentPane);
		if (ersteIterationFuerSpielerprofil) {
			Spielerprofil.buttonback.addActionListener(this);
			ersteIterationFuerSpielerprofil = false;
		}
		this.setVisible(true);
		setColor(Spielerprofil.contentPane);
	}

	boolean ersteIterationFuerHighscore = true;// Operationen wie action Listener nur einmal ausführen!
	FensterHighscore Highscore;

	public void starte_FensterHighscore() {
		System.out.println("Create Highscore");
		Highscore = new FensterHighscore(AnzahlRichtigeAntworten, maxAnzahlFragen);
		allePanelsSchliessen();
		this.add(Highscore.contentPane);
		if (ersteIterationFuerHighscore) {
			Highscore.btnZurckZumMen.addActionListener(this);
			ersteIterationFuerHighscore = false;
		}
		this.setVisible(true);
		// ActionListener
		setColor(Highscore.contentPane);

	}

	boolean ersteIterationFuerEinstellungen = true;// Operationen wie action Listener nur einmal ausführen!
	FensterEinstellungen Einstellungen = new FensterEinstellungen();

	public void starte_FensterEinstellungen() {
		System.out.println("Create Einstellungen");
		allePanelsSchliessen();
		this.add(Einstellungen.contentPane);

		this.setVisible(true);
		// ActionListener
		if (ersteIterationFuerEinstellungen) {
			Einstellungen.buttonBack.addActionListener(this);
			ersteIterationFuerEinstellungen = false;
		}

	}

	/**
	 * #############################################################################
	 * #############################################################################
	 * ###### Spiel Logik
	 */
	// Variablen
	int aktuelleFrageIndex = 0;
	int maxAnzahlFragen = daten.getMaxAnzahlFragen();
	String userAntwort, Loesung;
	// Variablen f�r Highscore statistiken
	int AnzahlFalscheAntworten = 0;
	int AnzahlRichtigeAntworten = 0;

	public void Restart() {
		System.out.println("Restart");
		daten = new Daten();// Reset Data Class
		ladeLerneinheit();
		Spiel.lblNameLerneinheit.setText("Aktuelle Lerneinheit: " + Startmenue.holeAusgewaehltesSpiel());
		aktuelleFrageIndex = 0;
		maxAnzahlFragen = daten.getMaxAnzahlFragen();
		String userAntwort, Loesung;
		// Variablen f�r Highscore statistiken
		AnzahlFalscheAntworten = 0;
		AnzahlRichtigeAntworten = 0;
		// Reset Daten
		daten.Namen = new String[daten.maxAnzahl];
		daten.Fragen = new String[daten.maxAnzahl];
		daten.Antworten = new String[daten.maxAnzahl];
		daten.AntwortAlternativen = new String[daten.maxAnzahl][daten.maxAntwortAlternativen];
		// Reset Spiel
		Spiel.progressBar.setValue(0);
	}

	public void GameWindow_nextQuestion() {
		System.out.println("aktuelleFrageIndex " + aktuelleFrageIndex);
		/**
		 * Bugfix: Game lässt sich nicht neu starten
		 */
		if (maxAnzahlFragen == 0) {
			System.out.println("Max anzahl fragen" + maxAnzahlFragen);
			maxAnzahlFragen = daten.getFirstFreeIndex(daten.Fragen);
		}
		if (aktuelleFrageIndex >= maxAnzahlFragen) {
			System.out.println("Spiel: zeige Highscore" + aktuelleFrageIndex + ">=" + maxAnzahlFragen);
			starte_FensterHighscore();
		}

		// Highscore
		if (Spiel.btnTree.isSelected()) {
			userAntwort = Spiel.btnTree.getText();
		}
		if (Spiel.btnHouse.isSelected()) {
			userAntwort = Spiel.btnHouse.getText();
		}
		if (Spiel.btnStreet.isSelected()) {
			userAntwort = Spiel.btnStreet.getText();
		}
		if (Spiel.btnPhone.isSelected()) {
			userAntwort = Spiel.btnPhone.getText();
		}

		// Bugfix: keine Antwort ausgewählt
		if (Spiel.btnTree.isSelected() == false && Spiel.btnHouse.isSelected() == false
				&& Spiel.btnStreet.isSelected() == false && Spiel.btnPhone.isSelected() == false) {
			userAntwort = "keine Antowrt ausgew�hlt!";
		}
		// Bei Init überspringen
		if (aktuelleFrageIndex != 0) {
			Loesung = daten.Antworten[aktuelleFrageIndex - 1];
			// System.out.println("Antwort ist: " + userAntwort);
			// System.out.println("Loesung ist :" + Loesung);
			// Progress bar Aktualisieren
			Spiel.progressBar.setMaximum(maxAnzahlFragen);
			Spiel.progressBar.setValue(aktuelleFrageIndex);
			// Falls Frage falsch beantwortet
			if (userAntwort.equals(Loesung) == false) {
				AnzahlFalscheAntworten++;
				Spiel.lblStatusLetzteAntwort.setText("Falsch. Richtige Antwort war: " + Loesung);
				Spiel.lblStatusLetzteAntwort.setForeground(Color.RED);
				// lblStatusLetzteAntwort.setText("Falsch. Richtige Antwort
				// war:"+L�sung+"(deine
				// Antwort: "+userAntwort+")");
			}
			// Falls Frage richtig beantwortet
			if (userAntwort.equals(Loesung)) {
				AnzahlRichtigeAntworten++;
				Spiel.lblStatusLetzteAntwort.setText("Richtig!");
				Spiel.lblStatusLetzteAntwort.setForeground(Color.GREEN);
			}
		}
		if (aktuelleFrageIndex < maxAnzahlFragen) {
			Spiel.btnTree.setText(daten.AntwortAlternativen[aktuelleFrageIndex][0]);
			Spiel.btnHouse.setText(daten.AntwortAlternativen[aktuelleFrageIndex][1]);
			Spiel.btnStreet.setText(daten.AntwortAlternativen[aktuelleFrageIndex][2]);
			Spiel.btnPhone.setText(daten.AntwortAlternativen[aktuelleFrageIndex][3]);
			Spiel.btnPencil.setVisible(false);
			Spiel.btnRabbit.setVisible(false);
			Spiel.txtWelchesWortIst.setText(daten.Fragen[aktuelleFrageIndex]);
			Spiel.FragenVerbleibend.setText(aktuelleFrageIndex + " von " + maxAnzahlFragen);
			aktuelleFrageIndex++;
			// System.out.println("Nächste Frage");

		}

	}

	/**
	 * #############################################################################
	 * #############################################################################
	 * ###### Hilfsmethoden
	 */

	/*
	 * Audio Player
	 */

	// File track1 = new File("enemy_missle.mp3");
	Clip Audio;
	AudioInputStream audioIn;
	boolean isAudioPlaying=false;
	public void playRandomAudio() {
		stopAudio();
		int AnzahlTracks = 5;
		int random = (int) (Math.random() * AnzahlTracks) + 1;
		System.out.println("Play Track " + random);
		try {
			// Menu Music
			switch (random) {
			case 1:
				audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK1.wav"));
				break;
			case 2:
				audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK2.wav"));
				break;
			case 3:
				audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK3.wav"));
				break;
			case 4:
				audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK4.wav"));
				break;
			case 5:
				audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK5.wav"));
				break;
			}
			// audioIn =
			// AudioSystem.getAudioInputStream(Run.class.getResource("TRACK2.wav"));
			Audio = AudioSystem.getClip();
			Audio.open(audioIn);
			Audio.start();
			isAudioPlaying=true;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void playAudio() {
		try {
			// Menu Music
			audioIn = AudioSystem.getAudioInputStream(Run.class.getResource("TRACK2.wav"));
			Audio = AudioSystem.getClip();
			Audio.open(audioIn);
			Audio.start();
			isAudioPlaying=true;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void stopAudio() {
		try {
			Audio.stop();
			isAudioPlaying=false;
		}catch(Exception e) {
			System.out.println("No Audio to stop.");
		}
		
	}

}
