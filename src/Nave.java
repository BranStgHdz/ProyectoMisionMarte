import java.util.Random;

// clase de la nave
class Nave {
    // Caracteres de la nave
    protected final String forma = "╩"; // Utilizables: [╔╩╗] [╬] [╔╬╗] [║══║] [╩]
    protected final String bullet = "¤"; // Utilizables: [î] [¤] [¨] [¡] [^**^]
    // Utilidad
    protected String nombreNave;
    protected String nombrePiloto;
    protected static boolean naveConVida = true;
    // Contadores
    protected static int velocidad = 0;
    protected int cantBullets;
    protected int cantCapsulasVida;
    protected static int contEnemigosDestruidos = 0;
    // Limitantes
    protected int velocidadMin = 30;

    // setters
    public void setCantBullets(int cantBalas) {
        this.cantBullets = cantBalas;
    }

    public void setCantCapsulasVida(int vida) {
        this.cantCapsulasVida = vida;
    }

    // getters
    public int getCantBullets() {
        return this.cantBullets;
    }

    public int getCantCapsulasVida() {
        return this.cantCapsulasVida;
    }

    public String getForma() {
        return this.forma;
    }

    public String getBullet() {
        return this.bullet;
    }

    public int getVelocidadMin() {
        return this.velocidadMin;
    }


    // Constructor. Solicitar información del usuario
    public Nave(String nomUsuario, String nomNave) {
        this.nombreNave = nomNave;
        this.nombrePiloto = nomUsuario;
    }

    public Nave() {
    }

    // Método. Asignación. Rango de velocidades de la nave dependiendo de su cantidad de desplazamientos
    public void VelocidadNave(int contCasillas) {
        Random ram = new Random();
        int changes = 0;
        if (contCasillas == 0) {
            changes = 1;
        } else if (contCasillas >= 1 && contCasillas <= 3) {
            changes = 2;
        } else if (contCasillas >= 4 && contCasillas <= 6) {
            changes = 3;
        } else if (contCasillas >= 7 && contCasillas <= 10) {
            changes = 4;
        } else if (contCasillas >= 11) {
            changes = 5;
        }
        switch (changes) {
            // Sin desplazamiento: 30 - 300, al disparar se resetea a sin desplazamiento
            case 1 -> velocidad = ram.nextInt(271) + getVelocidadMin();

            // Desplazamiento leve: 300 - 900, desplazamiento(1 - 3 casillas seguidas)
            case 2 -> velocidad = ram.nextInt(701) + 200;

            // Desplazamiento: 900 - 1900, desplazamiento(4 - 6 casillas seguidas)
            case 3 -> velocidad = ram.nextInt(1001) + 900;

            // Desplazamiento rapido: 1900 - 3500, desplazamiento(7 - 10 casillas seguidas)
            case 4 -> velocidad = ram.nextInt(1601) + 1900;

            // Desplazamiento grave: 3500 - 5000, desplazamiento indefinido (11 - Indefinido, casillas seguidas)
            case 5 -> velocidad = ram.nextInt(1501) + 3500;
        }
    }
}