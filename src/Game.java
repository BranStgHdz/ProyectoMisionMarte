import javax.swing.*;
import javax.swing.border.BevelBorder; // Proporciona un borde con efecto biselado a un componente de la interfaz de usuario Swing.
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent; // Permite crear evento del teclado.
import java.awt.event.KeyListener; // Define métodos para escuchar eventos de teclado.
import java.util.concurrent.CountDownLatch; // proporciona una forma de coordinar la ejecución de múltiples hilos.

// clase del juego
public class Game extends JFrame {
    // Coordenadas de la nave y bullets el plano
    protected static int posicionNaveX = 30, posicionNaveY = 15; // Cordenadas iniciales de la nave
    protected static int[][] bulletPosition = new int[20][2]; // Posiciones de las balas en el mapa
    // Utilidad
    protected static char button = '\0'; // Inicializa la variable con un valor nulo
    protected static int frame = 0; // variable par contar cantidad de veces que se refresca la pantalla
    protected static int contadorCasillasSeguidas = 0;
    public static boolean finPartida = false;
    // Condicionales
    protected static boolean shoot = false;
    protected int cantBallsForShoot;
    protected static int duracionPartida = 200; // Nivel 1: 200, Nivel 2: 250, Nivel 3: 300
    protected int costeCapturaInteresante = 20; // Nivel 1: 20, Nivel 2: 30 ,Nivel 3: 50
    protected static int compOpcGen = 0;
    // Contadores de objetos en el mapa
    protected int contObjetosInteresCapturados = 0;
    protected int contBulletMap = 0;
    protected int contObjetosInteresMap = 0;
    protected int cantEnemigosMap = 0;
    // Instancias
    public static Nave instanciaNave = new Nave();
    public static Game instanciaGame = new Game(null);
    protected static PeligrosExterior[] peligros = new PeligrosExterior[3];
        // Paneles.
    // Panel. Game
    private JPanel panelPlayMain;
    private JLabel medidorLabel, contadorLabel;
    private JTextArea gameArea;
    // Panel. "Sala de espera"
    protected JPanel panelEspera;
    protected JButton reintentarButton;
    protected JButton continuarButton;
    private JButton regresarMenuButton;
    protected JLabel opcionLabel;
    private ReintentarButtonHandler reintentarButtonHandler;
    private ContinuarButtonHandler continuarButtonHandler;
    private RegresarButtonHandler regresarButtonHandler;
    // Dimension ventana
    private static final int HEIGHT = 500; // Largo
    private static final int WIDTH = 750; // Ancho

    // setters
    public void setCantEnemigosMap(int cantEnemigosMap) {
        this.cantEnemigosMap = cantEnemigosMap;
    }

    public void setContObjetosInteresMap(int objetosInteresantesEnMap) {
        this.contObjetosInteresMap = objetosInteresantesEnMap;
    }

    public void setContBulletMap(int cantidadBalasEnMapa) {
        this.contBulletMap = cantidadBalasEnMapa;
    }

    public void setCantBallsForShoot(int cantidadBalasPorDisparo) {
        this.cantBallsForShoot = cantidadBalasPorDisparo;
    }

    public void setContObjetosInteresCapturados(int contObjetosInteresCapturados) {
        this.contObjetosInteresCapturados = contObjetosInteresCapturados;
    }

    public void setCosteCapturaInteresante(int coste) {
        this.costeCapturaInteresante = coste;
    }

    // getters
    public int getCantEnemigosMap() {
        return this.cantEnemigosMap;
    }

    public int getContObjetosInteresMap() {
        return this.contObjetosInteresMap;
    }

    public int getCantBallsForShoot() {
        return this.cantBallsForShoot;
    }

    public int getContBulletMap() {
        return this.contBulletMap;
    }

    public int getContObjetosInteresCapturados() {
        return this.contObjetosInteresCapturados;
    }

    public Game(String nulo) {
    }

    // Ventana Espera
    public Game() {
        opcionLabel = new JLabel();
        LabelGeneric(opcionLabel);

        reintentarButton = new JButton("Reintentar");
        ButtonGeneric(reintentarButton);
        reintentarButtonHandler = new ReintentarButtonHandler();
        reintentarButton.addActionListener(reintentarButtonHandler);

        continuarButton = new JButton("Continuar");
        ButtonGeneric(continuarButton);
        continuarButtonHandler = new ContinuarButtonHandler();
        continuarButton.addActionListener(continuarButtonHandler);

        regresarMenuButton = new JButton("Regresar al menu");
        ButtonGeneric(regresarMenuButton);
        regresarButtonHandler = new RegresarButtonHandler();
        regresarMenuButton.addActionListener(regresarButtonHandler);

        panelEspera = new JPanel();
        add(panelEspera);
        panelEspera.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelEspera.add(opcionLabel);
        panelEspera.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 25));
        panelEspera.add(continuarButton);
        panelEspera.add(reintentarButton);
        panelEspera.add(regresarMenuButton);
        ValorVentanaPorDefecto();
    }

        // Eventos
    // Evento. Reintentar
    public class ReintentarButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { // Reinicia el arsena del nivel 0
            setVisible(false);
            instanciaNave.setCantBullets(8000);
            instanciaNave.setCantCapsulasVida(2000);
            instanciaGame.setCantBallsForShoot(30);
            instanciaGame.setCosteCapturaInteresante(20);
            duracionPartida = 200;
            instanciaGame.EjecucionPrimaria();
        }
    }

    // Evento. Continuar
    public class ContinuarButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            switch (compOpcGen) { // Asigna valores para cada respectivo nivel a exepcion del nivel 1(nivel 0)
                case 1 -> {
                    instanciaNave.setCantBullets(7000);
                    instanciaNave.setCantCapsulasVida(1200);
                    instanciaGame.setCantBallsForShoot(40);
                    instanciaGame.setCosteCapturaInteresante(30);
                    duracionPartida = 250;
                }
                case 2 -> {
                    instanciaNave.setCantBullets(5000);
                    instanciaNave.setCantCapsulasVida(800);
                    instanciaGame.setCantBallsForShoot(70);
                    instanciaGame.setCosteCapturaInteresante(50);
                    duracionPartida = 300;
                }
            }
            instanciaGame.EjecucionPrimaria();
        }
    }

    // Evento. Regresar al Menu principal
    public class RegresarButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            main(null);
        }
    }

        // Métodos
    // Método. Main
    public static void main(String[] args) {
        ControlVentanaMenu ventanaMenu = new ControlVentanaMenu(); // Llamamos al menu principal para empezar la ejecución
    }

    // Método. Ejecución primaria encargada de ejecutar el cambio de nivel
    protected void EjecucionPrimaria() {
        // Hilo de ejecucion del juego
        Thread hiloJuego = instanciaGame.EjecucionSecundaria();
        // Hilo de comprobación de resultado
        Thread hiloComp = new Thread(() -> {
            try {
                // Este hilo espera a que termine de ejecutarse el hiloJuego para continuar
                hiloJuego.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int nivel = compOpcGen;
            Game VE = new Game();
            nivel = instanciaGame.ComprobarExito(nivel);
            if (compOpcGen == nivel) {
                if (!Nave.naveConVida) {
                    VE.opcionLabel.setText("Desea reintentar??");
                } else {
                    VE.opcionLabel.setText("No a cumplido con los requisitos del nivel " + (nivel + 1));
                }
                nivel = 0;
                VE.panelEspera.setVisible(true);
                VE.continuarButton.setVisible(false);
                Nave.naveConVida = true;
            } else if ((compOpcGen < nivel) && nivel < 3) {
                VE.opcionLabel.setText("Felicidades a pasado al siguiente nivel");
                VE.panelEspera.setVisible(true);
                VE.reintentarButton.setVisible(false);
            } else {
                VE.opcionLabel.setText("Felicidades " + instanciaNave.nombrePiloto + " a completado el juego con tu nave " + instanciaNave.nombreNave + "!!");
                VE.panelEspera.setVisible(true);
                VE.reintentarButton.setVisible(false);
                VE.continuarButton.setVisible(false);
            }
            compOpcGen = nivel;
        });
        // Se comienza a ejecutar los hilos
        hiloJuego.start();
        hiloComp.start();
    }

    //Método. Ejecución secundaria encargada de ejecutar el juego
    protected Thread EjecucionSecundaria() {
        // Asignaciones
        peligros[0] = new Planetas();
        peligros[1] = new Asteroides();
        peligros[2] = new HoyosNegros();

        // Instancia Matriz
        VentanaMatriz matriz = new VentanaMatriz();

        medidorLabel = new JLabel("║ Time: " + frame +
                " ║ Movimientos continuos: " + contadorCasillasSeguidas +
                " ║ velocidad: " + Nave.velocidad + "║");
        LabelGeneric(medidorLabel);

        contadorLabel = new JLabel("║ Balas: " + instanciaNave.getCantBullets() +
                " ║ Vida: " + instanciaNave.getCantCapsulasVida() +
                " ║ Coste de disparo: " + instanciaGame.getCantBallsForShoot() +
                " ║ Peligros erradicados: " + Nave.contEnemigosDestruidos +
                " ║ Objetos Capturados: " + instanciaGame.getContObjetosInteresCapturados() + "║");
        LabelGeneric(contadorLabel);

        gameArea = new JTextArea();
        gameArea.setEditable(false);

        panelPlayMain = new JPanel();

        panelPlayMain.setFocusable(true);
        add(panelPlayMain);
        panelPlayMain.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
        panelPlayMain.add(medidorLabel);
        panelPlayMain.add(gameArea);
        panelPlayMain.add(contadorLabel);
        ValorVentanaPorDefecto();
        finPartida = false;

        return new Thread(() -> {
            do {
                SwingUtilities.invokeLater(() -> {
                    panelPlayMain.add(medidorLabel);
                    panelPlayMain.add(gameArea);
                    panelPlayMain.add(contadorLabel);
                    panelPlayMain.revalidate(); // Actualizar el panel
                });
                StringBuilder sb = new StringBuilder();
                // invoca la terminal donde se ejecuta el mapa
                matriz.Mapa(sb, instanciaNave.getForma(), instanciaNave.getBullet(), frame, instanciaGame, instanciaNave, peligros[compOpcGen], compOpcGen);
                SwingUtilities.invokeLater(() -> {
                    medidorLabel.setText("║ Time: " + frame +
                            " ║ Movimientos continuos: " + contadorCasillasSeguidas +
                            " ║ velocidad: " + Nave.velocidad + "║");
                    gameArea.setText(sb.toString());
                    contadorLabel.setText("║ Balas: " + instanciaNave.getCantBullets() +
                            " ║ Vida: " + instanciaNave.getCantCapsulasVida() +
                            " ║ Coste de disparo: " + instanciaGame.getCantBallsForShoot() +
                            " ║ Peligros erradicados: " + Nave.contEnemigosDestruidos +
                            " ║ Objetos Capturados: " + instanciaGame.getContObjetosInteresCapturados() + "║");
                    panelPlayMain.revalidate(); // Actualizar el panel
                });
                button = '\0';
                if (Nave.naveConVida) {
                    PressButton();    // Abre una ventana que detecta el char ingresado mientras este abierto
                    MovimientoNave(); // Asigna el movimiento de la nave
                }

                frame++; // Suma el "tiempo" de la partida

                if (!Nave.naveConVida && frame != duracionPartida) {
                    JOptionPane.showMessageDialog(null, "La nave ah sido destruida");
                    finPartida = true;
                } else if (Nave.naveConVida && frame == duracionPartida) {
                    JOptionPane.showMessageDialog(null, "Ah llegado al punto de control");
                    finPartida = true;
                }
            } while (!finPartida);
            SwingUtilities.invokeLater(() -> setVisible(false));
        });
    }

    // Método. Indicar el movimiento de la nave
    protected static void MovimientoNave() {
        switch (button) {
            // Izquierda
            case 'a' -> {
                if (posicionNaveX > 4) {
                    posicionNaveX = posicionNaveX - 2;
                    contadorCasillasSeguidas++;
                    instanciaNave.VelocidadNave(contadorCasillasSeguidas);
                } else {
                    contadorCasillasSeguidas = 0;
                }
            }

            // Abajo
            case 's' -> {
                if (posicionNaveY < 19) {
                    posicionNaveY++;
                    contadorCasillasSeguidas++;
                    instanciaNave.VelocidadNave(contadorCasillasSeguidas);
                } else {
                    contadorCasillasSeguidas = 0;
                }
            }

            // Derecha
            case 'd' -> {
                if (posicionNaveX < 58) {
                    posicionNaveX = posicionNaveX + 2;
                    contadorCasillasSeguidas++;
                    instanciaNave.VelocidadNave(contadorCasillasSeguidas);
                } else {
                    contadorCasillasSeguidas = 0;
                }
            }

            // Arriba
            case 'w' -> {
                if ((posicionNaveY > 1)) {
                    posicionNaveY--;
                    contadorCasillasSeguidas++;
                    instanciaNave.VelocidadNave(contadorCasillasSeguidas);
                } else {
                    contadorCasillasSeguidas = 0;
                }
            }

            // Disparar
            case 'i' -> {
                shoot = true;
                contadorCasillasSeguidas = 0;
                instanciaNave.VelocidadNave(contadorCasillasSeguidas);
            }

            // Salir
            case '@' -> //System.out.println("Hasta luego...");
                    frame = duracionPartida;
            default -> {
                contadorCasillasSeguidas = 0;
                instanciaNave.VelocidadNave(contadorCasillasSeguidas);
            }
        }
    }

    // Método. Comprueba requisitos de los niveles
    private int ComprobarExito(int level) {
        System.out.println(instanciaGame.getContObjetosInteresCapturados()+" "+instanciaNave.getCantBullets()+" "+
            instanciaNave.getCantCapsulasVida()+" "+Nave.contEnemigosDestruidos);
        switch (level) {
            case 0 -> {
                if (instanciaGame.getContObjetosInteresCapturados() >= 10 && instanciaNave.getCantBullets() >= 7000 &&
                        instanciaNave.getCantCapsulasVida() >= 1200 && Nave.contEnemigosDestruidos >= 5) {
                    level++;
                }
            }
            case 1 -> {
                if (instanciaGame.getContObjetosInteresCapturados() >= 20 && instanciaNave.getCantBullets() >= 5000 &&
                        instanciaNave.getCantCapsulasVida() >= 400 && Nave.contEnemigosDestruidos >= 10) {
                    level++;
                }
            }
            case 2 -> {
                if (instanciaGame.getContObjetosInteresCapturados() >= 11 && instanciaNave.getCantBullets() >= 3000 &&
                        instanciaNave.getCantCapsulasVida() >= 200 && Nave.contEnemigosDestruidos >= 20) {
                    level++;
                }
            }
        }
        ReiniciarValores();
        return level;
    }

    // Metodo. Reinicia valores acumulados durante la ejecucion
    public static void ReiniciarValores() {
        instanciaGame.setContBulletMap(0);
        instanciaGame.setCantEnemigosMap(0);
        instanciaGame.setContObjetosInteresCapturados(0);
        instanciaGame.setCantEnemigosMap(0);
        instanciaGame.setContObjetosInteresMap(0);
        contadorCasillasSeguidas = 0;
        Nave.contEnemigosDestruidos = 0;
        frame = 0;
        shoot = false;
        posicionNaveX = 30;
        posicionNaveY = 15;
    }

    // Metodo. Valores por defecto de la ventana
    private void ValorVentanaPorDefecto() {
        setTitle("« COLONIZING MARS »"); // Asignacion del nombre a la ventana
        setSize(WIDTH, HEIGHT); // Dimension de la ventana
        setVisible(true); // La hacemos visible para el usuario
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Designamos la "X" para cerrar la ventana
        UIManager.put("OptionPane.messageFont", new Font("Comic Sans MS", Font.PLAIN, 18));
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
    }

    // Metodo. Valores genericos Button
    private void ButtonGeneric(JButton JB) {
        JB.setPreferredSize(new Dimension(250, 40)); // Asignacion de la dimension del boton
        JB.setFont(new Font("Comic Sans MS", Font.PLAIN, 18)); // Asignacion de estilo en las letras
        JB.setBorder(new BevelBorder(BevelBorder.RAISED)); // Asignacion del borde del boton
    }

    // Metodo. Valores genericos Label
    public static void LabelGeneric(JLabel JL) {
        JL.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    // Método. Detectar la tecla presionada mediante un char, haciendo uso de los eventos
    protected static void PressButton() {
        CountDownLatch latch = new CountDownLatch(1); // Crear un CountDownLatch con 1 señalizador
        Frame frame = new Frame("Lector");
        TextField textField = new TextField(1);
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) { // KeyTeyped es un evento que detecta el instante en el que se presiona la tecla
                button = e.getKeyChar(); // Guarda el carácter ingresado en la variable 'button'
                frame.dispose();
                latch.countDown(); // Señalizar que se ha ingresado un carácter
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        frame.add(textField);
        frame.pack();
        frame.setVisible(true);

        try {
            latch.await(); // Esperar hasta que se haya ingresado un carácter
        } catch (InterruptedException ignored) {
        }
    }
}