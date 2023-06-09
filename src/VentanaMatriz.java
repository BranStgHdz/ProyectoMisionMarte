import java.util.Random;

class VentanaMatriz {
    // Mapa del "juego"
    protected static String[][] map = new String[20][60];
    // Instancias
    protected static UbicacionAleatoria genUbicacion = new UbicacionAleatoria();
    private final RastreadorObjetos colisionInteresante = new ColisionInteresante();
    private final RastreadorObjetos colisionNoInteresante = new ColisionNoInteresante();
    // Caracteres del mapa
    protected final String caracterMap = " "; // Utilizables: [.] [·] [³]
    protected final String caracterInteresante = "·"; // Utilizables: [æ] [■] [·] [×] [*] [+]
    // Utilidad
    protected boolean[] idEstadoPlanetas = new boolean[20];

    // Métodos
    // Método. Invoca la reducción de objetos interesantes
    private static void invokeReductorInteresante(Game G, RastreadorObjetos RO) {
        RO.ReductorInteresante(G);
    }

    // Método. "ventana gameMain"
    public void Mapa(StringBuilder sb, String nv, String bullet, int Time, Game instanciaGame, Nave instanciaNave, PeligrosExterior Amenaza, int nivel) {
        int[] contador = new int[20];
        int[][] temp;
        boolean[] tempEstado = new boolean[20];
        int cont, contTemp = 0, compEliminaPosicionA = 0, compEliminaPosicionB = 0;
        // Asigna el relleno inicial a la matriz
        if (Time == 0) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = caracterMap;
                }
            }
        }
        // Asigna el relleno a la matriz
        if (Time > 0) {
            // Cuenta la colisión con la nave de objetos interesantes en X
            // Comprueba el movimiento <- o -> para normalizar el movimiento para dos casillas
            if (Game.button == 'd' && (map[Game.posicionNaveY][Game.posicionNaveX].equals(caracterInteresante)
                    || map[Game.posicionNaveY][Game.posicionNaveX - 1].equals(caracterInteresante))) {
                for (int j = Game.posicionNaveX; j >= Game.posicionNaveX - 1; j--) {
                    if (map[Game.posicionNaveY][j].equals(caracterInteresante)) {
                        map[Game.posicionNaveY][j] = caracterMap;
                        invokeReductorInteresante(instanciaGame, colisionInteresante);
                        instanciaNave.setCantCapsulasVida(instanciaNave.getCantCapsulasVida() - instanciaGame.costeCapturaInteresante);
                    }
                }
            } else if (Game.button == 'a' && map[Game.posicionNaveY][Game.posicionNaveX].equals(caracterInteresante)
                    || map[Game.posicionNaveY][Game.posicionNaveX + 1].equals(caracterInteresante)) {
                for (int j = Game.posicionNaveX; j <= Game.posicionNaveX + 1; j++) {
                    if (map[Game.posicionNaveY][j].equals(caracterInteresante)) {
                        map[Game.posicionNaveY][j] = caracterMap;
                        invokeReductorInteresante(instanciaGame, colisionInteresante);
                        instanciaNave.setCantCapsulasVida(instanciaNave.getCantCapsulasVida() - instanciaGame.costeCapturaInteresante);
                    }
                }
            }
            // Cuenta la colisión con la nave de objetos interesantes en Y
            if (map[Game.posicionNaveY][Game.posicionNaveX].equals(caracterInteresante)) {
                invokeReductorInteresante(instanciaGame, colisionInteresante);
                instanciaNave.setCantCapsulasVida(instanciaNave.getCantCapsulasVida() - instanciaGame.costeCapturaInteresante);
            }

            // Rellena la matriz con los caracterMap
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (!map[i][j].equals(caracterInteresante)) {
                        map[i][j] = caracterMap;
                    }
                }
            }
        }

        // Genera un objeto de interés en la matriz
        if (Time % 5 == 0 && instanciaGame.contObjetosInteresMap < 20) { // Limita la cantidad de objetos interesantes en em map
            genUbicacion.ObjetoInteres(map[0].length, map.length);
            map[genUbicacion.posicion.getAleatorioY()][genUbicacion.posicion.getAleatorioX()] = caracterInteresante;
            instanciaGame.setContObjetosInteresMap(instanciaGame.getContObjetosInteresMap() + 1);
        }

        // Movimiento de peligro por la matriz
        if ((instanciaGame.getCantEnemigosMap() > 0)) {
            for (int i = 0; i < instanciaGame.getCantEnemigosMap(); i++) {
                if (PeligrosExterior.Ubicacion[i][1] < (map.length - 2) && PeligrosExterior.Ubicacion[i][1] > 0) { // Limita el rango de movimiento del Peligro(1 a 17)
                    // Moviliza al peligro 1 posición abajo en el mismo eje x
                    PeligrosExterior.Ubicacion[i][1]++;
                    // Elimina objeto de interés del mapa al colisionar con el "peligro"
                    if (map[PeligrosExterior.Ubicacion[i][1] - 1][PeligrosExterior.Ubicacion[i][0]].equals(Amenaza.modeloPeligro) &&
                            map[PeligrosExterior.Ubicacion[i][1]][PeligrosExterior.Ubicacion[i][0]].equals(caracterInteresante)) {
                        invokeReductorInteresante(instanciaGame, colisionNoInteresante);
                    }
                    map[PeligrosExterior.Ubicacion[i][1]][PeligrosExterior.Ubicacion[i][0]] = Amenaza.modeloPeligro; // Asigna modelo a la matriz
                    // Si la nave colisiona con el peligro, la nave es "destruida"
                    if (PeligrosExterior.Ubicacion[i][1] == Game.posicionNaveY && PeligrosExterior.Ubicacion[i][0] == Game.posicionNaveX) {
                        Nave.naveConVida = false;
                    }

                    if (idEstadoPlanetas[i]) { // Si el estado de esta es true, genera una órbita
                        for (int j = PeligrosExterior.Ubicacion[i][1] - Amenaza.longitudPeligros; j <= PeligrosExterior.Ubicacion[i][1] + Amenaza.longitudPeligros; j++) {
                            for (int k = PeligrosExterior.Ubicacion[i][0] - Amenaza.longitudPeligros; k <= PeligrosExterior.Ubicacion[i][0] + Amenaza.longitudPeligros; k++) {
                                if (!map[j][k].equals(Amenaza.modeloPeligro)) {
                                    if (map[j][k].equals(caracterInteresante)) { // Si el modeloPeligroExtra colisiona con un objeto de interés lo elimina
                                        invokeReductorInteresante(instanciaGame, colisionNoInteresante);
                                    }
                                    map[j][k] = Amenaza.modeloPeligroExtra;
                                    if (instanciaGame.getContBulletMap() > 0) { // Sí hay bullets en el mapa: Detecta la colisión de estas con los Peligros extra
                                        for (int l = 0; l < instanciaGame.getContBulletMap(); l++) {
                                            if (Game.bulletPosition[l][0] == k && (Game.bulletPosition[l][1] - 1) == j) {
                                                map[j][k] = bullet;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Movimiento de la bala por la matriz
        if ((instanciaGame.getContBulletMap() > 0)) {
            for (int j = 0; j < instanciaGame.getContBulletMap(); j++) {
                if (Game.bulletPosition[j][1] > 0) { // Comprueba si la bullet no ha rebasado su límite en el arreglo
                    // Moviliza la bullet 1 posición arriba en el mismo eje x
                    Game.bulletPosition[j][1]--;
                    // Si la bala golpea un objeto interesante lo elimina
                    if (map[Game.bulletPosition[j][1]][Game.bulletPosition[j][0]].equals(caracterInteresante)) {
                        invokeReductorInteresante(instanciaGame, colisionNoInteresante);
                    }
                    for (int k = 0; k < instanciaGame.getCantEnemigosMap(); k++) {
                        if (nivel == 0) { // Condición solo disponible para los planetas(Nivel 1)
                            if ((Game.bulletPosition[j][1] == PeligrosExterior.Ubicacion[k][1] ||
                                    (Game.bulletPosition[j][1] + 1) == PeligrosExterior.Ubicacion[k][1]) &&
                                    Game.bulletPosition[j][0] == PeligrosExterior.Ubicacion[k][0] && !idEstadoPlanetas[k]) {
                                System.out.println("\u001B[31mColision!!\u001B[0m");
                                Nave.contEnemigosDestruidos++;
                                map[PeligrosExterior.Ubicacion[k][1]][PeligrosExterior.Ubicacion[k][0]] = caracterMap;
                                PeligrosExterior.Ubicacion[k][1] = -10; // Asigna posición fuera de rango para su eliminación
                            }
                        } else {
                            if ((Game.bulletPosition[j][1] == PeligrosExterior.Ubicacion[k][1] ||
                                    (Game.bulletPosition[j][1] + 1) == PeligrosExterior.Ubicacion[k][1]) &&
                                    Game.bulletPosition[j][0] == PeligrosExterior.Ubicacion[k][0]) {
                                System.out.println("\u001B[31mColision!!\u001B[0m");
                                Nave.contEnemigosDestruidos++;
                                map[PeligrosExterior.Ubicacion[k][1]][PeligrosExterior.Ubicacion[k][0]] = caracterMap;
                                PeligrosExterior.Ubicacion[k][1] = -10;
                            }
                        }
                    }
                    map[Game.bulletPosition[j][1]][Game.bulletPosition[j][0]] = bullet;
                } else {
                    compEliminaPosicionB = 1;
                }
            }
        }

        // Elimina posición Amenaza
        temp = new int[20][2];
        if ((instanciaGame.getCantEnemigosMap() > 0)) {
            for (int i = 0; i < instanciaGame.getCantEnemigosMap(); i++) {
                if (PeligrosExterior.Ubicacion[i][1] < (map.length - 2) && PeligrosExterior.Ubicacion[i][1] > 0) {
                    temp[contTemp][0] = PeligrosExterior.Ubicacion[i][0];
                    temp[contTemp][1] = PeligrosExterior.Ubicacion[i][1];
                    tempEstado[contTemp] = idEstadoPlanetas[i];
                    contTemp++;
                } else {
                    compEliminaPosicionA++;
                }
            }
            if (contTemp > 0) { // Elimina del arreglo de posiciones si el objeto salió del límite o ha sido eliminado
                for (int i = 0; i < temp.length; i++) {
                    PeligrosExterior.Ubicacion[i][0] = temp[i][0];
                    PeligrosExterior.Ubicacion[i][1] = temp[i][1];
                    idEstadoPlanetas[i] = tempEstado[i];
                    if (compEliminaPosicionA != 0) {
                        instanciaGame.setCantEnemigosMap(instanciaGame.getCantEnemigosMap() - 1);
                        compEliminaPosicionA--;
                    }
                }
            }
        }

        // Elimina posición Bullet
        if (compEliminaPosicionB == 1) {
            // Refresca el arreglo de posiciones eliminando la posición por llegar al límite del arreglo
            temp = new int[20][2];
            for (int k = 1; k < instanciaGame.getContBulletMap(); k++) { // Guardamos en un arreglo temporal parra no perder el orden
                temp[k - 1][0] = Game.bulletPosition[k][0];
                temp[k - 1][1] = Game.bulletPosition[k][1];
                Game.bulletPosition[k - 1][0] = temp[k - 1][0];
                Game.bulletPosition[k - 1][1] = temp[k - 1][1];
            }
            instanciaGame.setContBulletMap(instanciaGame.getContBulletMap() - 1);
        }

        // Genera "enemigos" por primera vez
        if ((Time % 6 == 0) && (Time > 0)) {
            // se genera el peligro en un lugar al azar en la parte superior de la matriz
            genUbicacion.PeligroExterior(map[0].length);
            PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][0] = genUbicacion.posicion.getAleatorioX();
            PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][1] = genUbicacion.posicion.getAleatorioY();
            map[genUbicacion.posicion.getAleatorioY()][genUbicacion.posicion.getAleatorioX()] = Amenaza.modeloPeligro;

            if (nivel == 0) { // Solo si es el nivel 1 genera el estado habitado o deshabitado en el planeta
                Planetas tempEstadoP = new Planetas();
                idEstadoPlanetas[instanciaGame.getCantEnemigosMap()] = tempEstadoP.GenEstado();
            } else {
                idEstadoPlanetas[instanciaGame.getCantEnemigosMap()] = true;
            }
            if (idEstadoPlanetas[instanciaGame.getCantEnemigosMap()]) { // por defecto solo se cumple si es true
                for (int i = PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][1] - Amenaza.longitudPeligros;
                     i <= PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][1] + Amenaza.longitudPeligros; i++) {
                    for (int j = PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][0] - Amenaza.longitudPeligros;
                         j <= PeligrosExterior.Ubicacion[instanciaGame.getCantEnemigosMap()][0] + Amenaza.longitudPeligros; j++) {
                        if (!map[i][j].equals(Amenaza.modeloPeligro)) {
                            if (map[i][j].equals(caracterInteresante)) { // Existe la posibilidad de que al generar el peligro este se coma el objetoInteresante
                                invokeReductorInteresante(instanciaGame, colisionNoInteresante);
                            }
                            if (instanciaGame.getContBulletMap() > 0) {
                                for (int k = 0; k < instanciaGame.getContBulletMap(); k++) {
                                    if (Game.bulletPosition[k][0] != i && Game.bulletPosition[k][1] != j) {
                                        map[i][j] = Amenaza.modeloPeligroExtra;
                                        break;
                                    }
                                }
                            } else {
                                map[i][j] = Amenaza.modeloPeligroExtra;
                            }
                        }
                    }
                }
            }
            instanciaGame.setCantEnemigosMap(instanciaGame.getCantEnemigosMap() + 1);
        }

        // invoca por primera vez la bala en la terminal
        if ((instanciaNave.getCantBullets() > instanciaGame.getCantBallsForShoot()) && Game.shoot) {
            // Reduce la cantidad de Bullets del inventario
            instanciaNave.setCantBullets(instanciaNave.getCantBullets() - instanciaGame.getCantBallsForShoot());
            // Guardamos la posición de la bala en un arreglo
            Game.bulletPosition[instanciaGame.getContBulletMap()][0] = Game.posicionNaveX;
            Game.bulletPosition[instanciaGame.getContBulletMap()][1] = Game.posicionNaveY - 1;
            // Si la bala golpea un objeto interesante lo elimina
            if (map[Game.bulletPosition[instanciaGame.getContBulletMap()][1]]
                    [Game.bulletPosition[instanciaGame.getContBulletMap()][0]].equals(caracterInteresante)) {
                invokeReductorInteresante(instanciaGame, colisionNoInteresante);
            }
            // Si la bala golpea una amenaza lo elimina
            for (int k = 0; k < instanciaGame.getCantEnemigosMap(); k++) {
                if (nivel == 0) { // Condición solo disponible para los planetas(Nivel 1)
                    if (Game.bulletPosition[instanciaGame.getContBulletMap()][1] == PeligrosExterior.Ubicacion[k][1] &&
                            Game.bulletPosition[instanciaGame.getContBulletMap()][0] == PeligrosExterior.Ubicacion[k][0] && !idEstadoPlanetas[k]) {
                        System.out.println("\u001B[31mColision!!\u001B[0m");
                        Nave.contEnemigosDestruidos++;
                        PeligrosExterior.Ubicacion[k][1] = -10; // Asigna posición fuera de rango para su eliminación
                    }
                } else {
                    if (Game.bulletPosition[instanciaGame.getContBulletMap()][1] == PeligrosExterior.Ubicacion[k][1] &&
                            Game.bulletPosition[instanciaGame.getContBulletMap()][0] == PeligrosExterior.Ubicacion[k][0]) {
                        System.out.println("\u001B[31mColision!!\u001B[0m");
                        Nave.contEnemigosDestruidos++;
                        PeligrosExterior.Ubicacion[k][1] = -10;
                    }
                }
            }
            // Posiciona la bullet delante de la nave
            map[Game.posicionNaveY - 1][Game.posicionNaveX] = bullet;
            instanciaGame.setContBulletMap(instanciaGame.getContBulletMap() + 1); // Sumamos las balas invocadas
            Game.shoot = false;
        }

        // Ubicación de la nave
        map[Game.posicionNaveY][Game.posicionNaveX] = nv;
        // Cuenta la cantidad de caracteres en cada casilla y lo guarda como total de la fila
        for (int i = 0; i < map.length; i++) { // Y
            contador[i] = 0;
            for (int j = 0; j < map[0].length; j++) { // X
                contador[i] += map[i][j].length();
            }
        }

        // Imprime el mapa "normalizado"
        for (int i = 0; i < map.length; i++) {
            cont = contador[i] - map[0].length; // Cuenta los caracteres que salgan del "Límite"
            for (int j = 0; j < map[0].length; j++) {
                // Si hay excesos no imprime las primeras líneas para compensar del lado derecho
                if (cont <= 0) {
                    sb.append(map[i][j]);
                } else {
                    cont--;
                }
            }
            sb.append("\n");
        }
    }
}

// Subclase para generar posiciones
class UbicacionAleatoria extends VentanaMatriz {
    private final Random ram = new Random();
    protected UbicacionAleatoria posicion;
    private int aleatorioX, aleatorioY;

    public UbicacionAleatoria(int x, int y) {
        this.aleatorioX = x;
        this.aleatorioY = y;
    }

    public UbicacionAleatoria() {
    }

    public int getAleatorioX() {
        return this.aleatorioX;
    }

    public int getAleatorioY() {
        return this.aleatorioY;
    }

    // Métodos
    // Método. Generador, posición para objetos de interés
    public void ObjetoInteres(int longX, int longY) {
        int aleatorioX = ram.nextInt(longX - 5) + 3; // 60 - 5, de (0 a 55) + 3 = de 3 a 58
        int aleatorioY = ram.nextInt(longY - 5) + 3;
        posicion = new UbicacionAleatoria(aleatorioX, aleatorioY);
    }

    // Método. Genera posición para los peligros
    public void PeligroExterior(int longX) {
        int aleatorioY = ram.nextInt(3) + 1;
        int aleatorioX = ram.nextInt(longX - 6) + 3; // 60 - 6 = (0 a 54) + 3 = (3 a 57)
        if (aleatorioX % 2 != 0) {
            if (aleatorioX > 30) {
                aleatorioX--;
            } else {
                aleatorioX++;
            }
        }
        posicion = new UbicacionAleatoria(aleatorioX, aleatorioY);
    }
}