import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlVentanaMenu extends JFrame {
        // Menus
    // Menu. Principal
    private JPanel panelMenu;
    private JButton playButton, controlsButton, exitButton;
    private JLabel headingGameLabel, footerGameLabel;
    // Menu. Controls
    private JPanel panelControls;
    private JLabel controlsLabel, controlsLabel2;
    private JButton returnMenuButton;
    // Menu. Play
    private JPanel panelPlay;
    private JLabel playerLabel, naveLabel;
    private JTextField playerTextField, naveTextField;
    private JButton nextButton;
    // Controladores de eventos
    private PlayButtonHandler playButtonHandler;
    private ControlsButtonHandler controlsButtonHandler;
    private ExitButtonHandler exitButtonHandler;
    // Dimension de ventana
    private static final int HEIGHT = 500; // Largo
    private static final int WIDTH = 650; // Ancho

    public ControlVentanaMenu() {
        headingGameLabel = new JLabel("╔════════ BIENVENIDO A COLONIZING MARS ════════╗");
        headingGameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18)); // Asignación de tipografía

        footerGameLabel = new JLabel("╚══════════════════════════════════════╝");
        footerGameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        playButton = new JButton("Play"); // Declaración del botón
        ButtonGeneric(playButton);
        playButtonHandler = new PlayButtonHandler(); // Declaración de Listener
        playButton.addActionListener(playButtonHandler); // Asignación del Listener al botón

        controlsButton = new JButton("Controles");
        ButtonGeneric(controlsButton);
        controlsButtonHandler = new ControlsButtonHandler();
        controlsButton.addActionListener(controlsButtonHandler);

        exitButton = new JButton("Exit");
        ButtonGeneric(exitButton);
        exitButtonHandler = new ExitButtonHandler();
        exitButton.addActionListener(exitButtonHandler);

        panelMenu = new JPanel();
        add(panelMenu);
        panelMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 0)); // Declaración de FlowLayout y sus parametros
        panelMenu.add(headingGameLabel);
        panelMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 50));
        panelMenu.add(playButton);
        panelMenu.add(controlsButton);
        panelMenu.add(exitButton);
        panelMenu.add(footerGameLabel);
        ValoresMenu();
    }

    // Clase de excepción vació, imprime mensaje
    public static class PlayerTextFieldEmptyException extends Exception {
        public PlayerTextFieldEmptyException(String message) {
            super(message);
        }
    }
        // Eventos
    // Evento. PlayButton, para iniciar juego
    public class PlayButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            panelMenu.setVisible(false);

            playerLabel = new JLabel("Nombre usuario: ");
            playerLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
            naveLabel = new JLabel("Nombre nave: ");
            naveLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
            playerTextField = new JTextField("", 20);
            playerTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
            naveTextField = new JTextField("", 20);
            naveTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));

            nextButton = new JButton("START!!");
            ButtonGeneric(nextButton);
            nextButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    boolean allowed = true;
                    setVisible(false);

                    if (playerTextField.getText().isEmpty() || naveTextField.getText().isEmpty()) {
                        try {
                            throw new PlayerTextFieldEmptyException("El campo del jugador o la nave está vacío");
                        } catch (PlayerTextFieldEmptyException e) {
                            e.printStackTrace();
                            allowed = false;
                            Game.main(null);
                        }
                    }

                    if (allowed) {
                        Nave player = new Nave(playerTextField.getText(), naveTextField.getText());

                        Game.instanciaNave.nombreNave = player.nombreNave;
                        Game.instanciaNave.nombrePiloto = player.nombrePiloto;

                        // Asignación de arsenal del nivel 1
                        Game.ReiniciarValores();
                        Game.instanciaNave.setCantBullets(8000);
                        Game.instanciaNave.setCantCapsulasVida(2000);
                        Game.instanciaGame.setCantBallsForShoot(30);
                        Game.instanciaGame.setCosteCapturaInteresante(20);
                        Game.duracionPartida = 180;

                        Game.instanciaGame.EjecucionPrimaria();
                    }
                }
            });

            panelPlay = new JPanel();
            add(panelPlay);
            panelPlay.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
            panelPlay.add(playerLabel);
            panelPlay.add(playerTextField);
            panelPlay.add(naveLabel);
            panelPlay.add(naveTextField);
            panelPlay.add(nextButton);
        }
    }

    // Evento. Muestra controles para el juego al usuario
    public class ControlsButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            panelMenu.setVisible(false);

            controlsLabel2 = new JLabel("Controles");
            controlsLabel2.setFont(new Font("Tahoma", Font.PLAIN, 18));
            controlsLabel = new JLabel("|Arriba = w  Abajo = s  Izquierda = a  Derecha = d|   |Disparar = i|");
            controlsLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

            returnMenuButton = new JButton("Regresar al Menu");
            ButtonGeneric(returnMenuButton);
            returnMenuButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    panelControls.setVisible(false);
                    panelMenu.setVisible(true);
                }
            });

            panelControls = new JPanel();
            add(panelControls);
            panelControls.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 0));
            panelControls.add(controlsLabel2);
            panelControls.add(controlsLabel);
            panelControls.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 50));
            panelControls.add(returnMenuButton);
        }
    }

    // Evento. Abandona el juego
    public class ExitButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            // Configuración de apariencia de la ventana de diálogo
            JOptionPane.showMessageDialog(null, "Hasta la proxima....");
            System.exit(0);
        }
    }

        // Métodos
    // Método. Valores genericos al botón
    private void ButtonGeneric(JButton JB) {
        JB.setPreferredSize(new Dimension(250, 40)); // Asignación de la dimension del botón
        JB.setFont(new Font("Comic Sans MS", Font.PLAIN, 18)); // Asignación de estilo en las letras
        JB.setBorder(new BevelBorder(BevelBorder.RAISED)); // Asignación del borde del botón
    }

    // Método. Valores predeterminados al menu principal
    private void ValoresMenu() {
        setTitle("« COLONIZING MARS »"); // Asignación del nombre a la ventana
        setSize(WIDTH, HEIGHT); // Dimension de la ventana
        setVisible(true); // La hacemos visible para el usuario
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Designamos la "X" para cerrar la ventana
    }
}