import java.util.Random;

// Clase para los peligros de cada nivel. Nivel 1: Planetas, Nivel 2: Asteroides, Nivel 3: Hoyos negros
class PeligrosExterior {
    protected String modeloPeligro; // Utilizables: [P] [A] [H] [@] [#] [©] [■] [Ø]
    protected String modeloPeligroExtra; // Utilizables: [.] [·] [■] [+] [°]
    protected int longitudPeligros;
    protected static int[][] Ubicacion = new int[30][2];

    public PeligrosExterior(String peligro1, String peligro2, int longPeligro) {
        this.modeloPeligro = peligro1;
        this.modeloPeligroExtra = peligro2;
        this.longitudPeligros = longPeligro;
    }
}

class Planetas extends PeligrosExterior {
    private static final String planeta = "P";
    private static final String planetaExt = "°";
    private static final int longitud = 1;

    public Planetas() {
        super(planeta, planetaExt, longitud);
    }

    public boolean GenEstado() {
        Random ram = new Random();
        return ram.nextBoolean();
    }
}

class Asteroides extends PeligrosExterior {
    private static final String asteroideCaracter = "A";
    private static final String asteroideCaracterExt = "·";
    private static final int longitud = 0;

    public Asteroides() {
        super(asteroideCaracter, asteroideCaracterExt, longitud);
    }
}

class HoyosNegros extends PeligrosExterior {
    private static final String blackHole = "H";
    private static final String blackHoleExt = "*";
    private static final int longitud = 1;

    public HoyosNegros() {
        super(blackHole, blackHoleExt, longitud);
    }
}