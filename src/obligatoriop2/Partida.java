package obligatoriop2;

import java.util.*;

public class Partida {

    private boolean partidaEnCurso;
    private boolean turnoJugador1;
    private boolean modoTest;
    private Jugada jugada;
    private Jugador ganador;
    private Jugador[] jugadores;
    private String[][] tablero;
    private String[] inicialesJugadores;
    private int[] puntajesJugadores;

    public Partida(boolean modoTest) {
        this.partidaEnCurso = true;
        this.turnoJugador1 = true;
        this.modoTest = modoTest;
        this.jugadores = new Jugador[2];
        this.tablero = new String[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                this.tablero[i][j] = Integer.toString((j + 1) + 5 * i);
            }
        }
        this.inicialesJugadores = new String[2];
        this.inicialesJugadores[0] = "Sin inicial";
        this.inicialesJugadores[1] = "Sin inicial";
        this.puntajesJugadores = new int[2];
        this.puntajesJugadores[0] = 0;
        this.puntajesJugadores[1] = 0;

    }
    
    public static boolean esModoTest(Scanner input) {
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("Desea jugar en modo Test o en modo Real?: para modo Test ingrese 'T' , para modo Real ingrese 'R' " );
        String aux = "sin leer";
        boolean opcion = false;
        
        while ( (!aux.equalsIgnoreCase("T")) && (!aux.equalsIgnoreCase("R")) ){
            aux = input.nextLine();
            if (aux.equalsIgnoreCase("T")) {
                opcion = true;
            } 
            else{
                if (aux.equalsIgnoreCase("R")) {
                    opcion = false;
                }
                else{
                    System.out.println("Opcion ingresada incorrecta, solo puede ingresar 'T' o 'R' , reingrese:");
                }
            }
        }

        return opcion;
    }
    
    
    
    
    public void hacerJugada(Scanner input){
        
        this.jugada = new Jugada();     //se crea una jugada nueva
        this.jugada.lanzarDados( this.modoTest , input );
        
        boolean permanecerEnLaMismaJugada;  //este boolean controla si debo permanecer en la misma jugada o hacer una jugada nueva
        
        do{     //si entra en Ayuda este codigo se vuelve a repetir, pues no se tienen que lanzar los dados de vuelta, es la misma jugada
                //la condicion esta al final
            permanecerEnLaMismaJugada = false;    
                
            this.jugada.mostrarDados();
            this.jugada.imprimirOpcionesDeJugadas();
            this.imprimirDeQueJugadorEsElTurno();
            this.jugada.setOpcionDeJugada(this.jugada.leerOpcionDeJugada(input));

            System.out.println("---------------------------------------------------------------------------------------------------------");

            switch (this.jugada.getOpcionDeJugada()){
                case "X":
                    this.setPartidaEnCurso(false);
                    //lo que el programa debe hacer una vez que se termina la partida lo pongo luego del while,
                    //porque hace lo mismo sin importar como salio de la partida

                    break;
                case "P":
                    this.cambiarTurno();

                    break;
                case "0":
                    if (this.posicionEstaLibre(this.jugada.getDados()[0])){
                        this.ingresarInicialEnTablero(this.jugada.getDados()[0]);
                        this.cambiarTurno();
                    }
                    else{
                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        System.out.println("!!!!!!!!!!!!!!!!!!  LA POSICION DEL TABLERO INDICADA ESTA OCUPADA, reingrese jugada:   !!!!!!!!!!!!!!!!!!");
                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        this.imprimirTablero();
                        permanecerEnLaMismaJugada = true;
                    }    

                    break;
                case "A":
                    ArrayList< ArrayList<Integer> > listaDadosExtrasYPosicionAlFinal = this.dadosExtrasJuntoConPosicionLibreCorrespondienteAlFinal();
                    if ( (listaDadosExtrasYPosicionAlFinal.size() > 0) || this.posicionEstaLibre(jugada.getDados()[0]) ){
                        //si hay alguna combinacion de extras con posicion libre, o la posicion del base esta libre ( es decir, si hay jugada posible)
                        this.imprimirJugadasPosibles(listaDadosExtrasYPosicionAlFinal);
                        this.imprimirTablero();
                    }
                    else{
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("! NO HAY POSICION LIBRE PARA LOS DADOS OBTENIDOS, solo puede elegir la opcion 'P'(pasar de turno) o la opcion 'X'(abandonar la partida) !");
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
                    }
                    
                    permanecerEnLaMismaJugada = true;

                    break;
                default:

                    if (this.jugada.dadosExtrasIngresadosValidos()){    //corroboro que los valores ingresados en esta opcion sean efectivamente dados extra sin repetirse
                        permanecerEnLaMismaJugada = this.ingresarFichaEnPosicionCorrespondiente(this.jugada); 
                    }
                    else{
                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        System.out.println("!!!!!!!!!!!!!!!!!!!  OPCION NO VALIDA DE DADOS EXTRAS INGRESADOS, reingrese jugada:   !!!!!!!!!!!!!!!!!!!");
                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        this.imprimirTablero();
                        permanecerEnLaMismaJugada = true;
                    }

                    break;
            }            
        }while (permanecerEnLaMismaJugada);
        
        
        //una vez que termina la jugada hago siempre lo siguiente:
        this.codigoAEjecutarCuandoTerminaLaJugada(this.jugada.getOpcionDeJugada());
        
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    
    
    
    private void codigoAEjecutarCuandoTerminaLaJugada( String opcionDeJugada ){
        this.calcularYActualizarPuntajesJugadores();
        this.imprimirTablero();
        this.imprimirPuntajeDeAmbosJugadores();
        if (this.tableroEstaLLeno()){
            this.setPartidaEnCurso(false);
        }
    }
    
    public void codigoAEjecutarCuandoTerminaLaPartida( String opcionDeJugada ){
        this.incrementarEn1PartidasJugadasDeAmbos();
        int numeroDeJugadorQueGano = this.asignarGanadorYRetornarNumeroDeJugadorCorrespondiente(opcionDeJugada);   
            //le asigna uno de los dos jugadores a la variable ganador de tipo Jugador y retorno el numero de jugador correspondiente
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  TERMINÓ LA PARTIDA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        if ( numeroDeJugadorQueGano > 0){   //si es mayor a 0 es porque vale 1 o vale 2, es decir, hubo ganador
            this.incrementarEn1PartidasGanadasAlGanador();
            System.out.println("GANADOR:  JUGADOR " + numeroDeJugadorQueGano + "   ( Inicial: " + this.inicialesJugadores[ numeroDeJugadorQueGano - 1 ] + " . " + this.ganador + " )");
            System.out.println("PUNTAJE DEL GANADOR: " + this.puntajesJugadores[ numeroDeJugadorQueGano - 1 ] );
            if (  numeroDeJugadorQueGano == 1 ){
                System.out.println("PUNTAJE DEL PERDEDOR: " + this.puntajesJugadores[1] );
            }
            else{
                System.out.println("PUNTAJE DEL PERDEDOR: " + this.puntajesJugadores[0] );
            }   
        }
        else{   //si no es mayor a 0 quiere decir que es -1, lo cual corresponde con que hubo empate
            System.out.println("*************************************");
            System.out.println("!!!!! EMPATE  !!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("*************************************");
        }

    }
    
    
    
    

    public void seleccionDeJugadores(ArrayList<Jugador> listaJugadores) {
        if (listaJugadores.size() < 2) {
            System.out.println("Debe haber al menos 2 jugadores registrados para jugar a Sumas");
            this.setPartidaEnCurso(false);
        } else {
            Scanner input = new Scanner(System.in);
            leerJugadores(listaJugadores, input);
        }
    }

    private static void imprimirListaJugadores(ArrayList<Jugador> listaJugadores) {
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("LISTA DE JUGADORES: ");
        for (int i = 0; i < listaJugadores.size(); i++) {
            System.out.print((i + 1) + ") ");
            System.out.println(listaJugadores.get(i));
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    //observar que este metodo es llamado por el metodo seleccionDeJugadores, y en lugar de retornar jugadores e 
    //inicialesJugadores y asignarlos a los atributos en el metodo seleccionDeJugadores lo que hago es modificar
    //a los atributos mismo en este metodo, lo cual no estoy muy seguro que es mas correcto.
    private void leerJugadores(ArrayList<Jugador> listaJugadores, Scanner input) {
        for (int i = 0; i <= 1; i++) {
            imprimirListaJugadores(listaJugadores);
            System.out.println("Seleccione jugador " + (i + 1) + " (debe ingresar el numero correspondiente de la lista. El numero ingresado debe estar entre 1 y " + listaJugadores.size() + " ):");
            //AGREGAR VALIDACION PARA ESTA LECTURA, inclusive un try catch
            int num = input.nextInt();
            input.nextLine();
            jugadores[i] = listaJugadores.get(num - 1);
            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.println("Ingrese inicial para el jugador " + (i + 1) + " : (debe ser una letra mayuscula)");
            
            String inicial = input.nextLine();
            while( !(this.tieneSoloLetras(inicial) ) || ( inicial.length() > 1) ){
                System.out.println("Inicial no valida, solo puede ser una unica letra, reingrese inicial: ");
                inicial = input.nextLine();
            }
            inicialesJugadores[i] = inicial.toUpperCase();
            System.out.println("---------------------------------------------------------------------------------------------------------");

        }
    }

    public void imprimirTablero() {
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("TABLERO:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if ( this.tablero[i][j].equals(this.inicialesJugadores[0]) ){
                    System.out.print(ConsoleColors.RED + this.tablero[i][j] + "\t" + ConsoleColors.RESET);
                }
                else{
                    if ( this.tablero[i][j].equals(this.inicialesJugadores[1]) ){
                        System.out.print(ConsoleColors.BLUE + this.tablero[i][j] + "\t" + ConsoleColors.RESET);
                    }
                    else{
                        System.out.print(ConsoleColors.GREEN + this.tablero[i][j] + "\t" + ConsoleColors.RESET);
                    }
                }
            }
            System.out.println("");
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    private void imprimirDeQueJugadorEsElTurno(){
        if (this.turnoJugador1){
            System.out.println("-------- TURNO DEL JUGADOR 1 (inicial "+this.inicialesJugadores[0]+") :  ----------------------------------------");
        }
        else{
            System.out.println("-------- TURNO DEL JUGADOR 2 (inicial "+this.inicialesJugadores[1]+") :  ----------------------------------------");
        }
    }

    private void incrementarEn1PartidasJugadasDeAmbos() {
        jugadores[0].incrementarEn1PartidasJugadas();
        jugadores[1].incrementarEn1PartidasJugadas();
    }

    private void incrementarEn1PartidasGanadasAlGanador() {
        ganador.incrementarEn1PartidasGanadas();
    }

    private int asignarGanadorYRetornarNumeroDeJugadorCorrespondiente(String opcionDeJugada) {
        int numeroDeJugadorCorrespondiente;
        if (opcionDeJugada.equals("X")){   //caso en que un jugador abandona
            if (this.turnoJugador1) {
                ganador = jugadores[1];
                numeroDeJugadorCorrespondiente = 2;
            } else {
                ganador = jugadores[0];
                numeroDeJugadorCorrespondiente = 1;
            }
        }
        else{           //caso en que el tablero se completa
            if (this.puntajesJugadores[0] > this.puntajesJugadores[1]){
                ganador = jugadores[0];
                numeroDeJugadorCorrespondiente = 1;
            }
            else{
                if (this.puntajesJugadores[0] < this.puntajesJugadores[1]){
                    ganador = jugadores[1];
                    numeroDeJugadorCorrespondiente = 2;
                }
                else{   //caso en que los puntajes son iguales
                    numeroDeJugadorCorrespondiente = -1;    //en este caso retorno un numero de jugador negativo, para indicar no hubo ganador
                    ganador = null;
                }
            }
        }
        return numeroDeJugadorCorrespondiente;
    }

    private void cambiarTurno() {
        turnoJugador1 = !turnoJugador1;
    }

    private boolean posicionEstaLibre(int pos) {
        return !((tablero[(pos - 1) / 5][(pos - 1) % 5]).equals(inicialesJugadores[0]) || (tablero[(pos - 1) / 5][(pos - 1) % 5]).equals(inicialesJugadores[1]));
    }
    
    private boolean tableroEstaLLeno(){
        boolean estaLLeno = true;
        for (int pos = 1 ; pos <= 20 ; pos++){
            if (this.posicionEstaLibre(pos)){
                estaLLeno = false;
            }
        }
        return estaLLeno;
    }

    private void ingresarInicialEnTablero(int pos) {
        if (turnoJugador1) {
            tablero[(pos - 1) / 5][(pos - 1) % 5] = inicialesJugadores[0];
        } else {
            tablero[(pos - 1) / 5][(pos - 1) % 5] = inicialesJugadores[1];
        }
    }
    
    private void imprimirPuntajeDeAmbosJugadores(){
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("PUNTAJE JUGADOR 1:   " + this.puntajesJugadores[0] + "   ( Inicial: " + this.inicialesJugadores[0] + " . " + this.jugadores[0] + " )" );
        System.out.println("PUNTAJE JUGADOR 2:   " + this.puntajesJugadores[1] + "   ( Inicial: " + this.inicialesJugadores[1] + " . " + this.jugadores[1] + " )" );
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    private void calcularYActualizarPuntajesJugadores() {     //este metodo modifica el atributo array puntajesJugadores
        for (int n =0 ; n<=1 ; n++){    //este for es para que se haga para cada uno de los 2 jugadores
            this.puntajesJugadores[n] = 0;  //esto es para que cada vez que calcula los puntajes los calcule desde cero completamente, 
                                            //si no pongo esto aunque el tablero no cambie se iria sumando el valor que tenia antes
                                            //aumentando siempre el puntaje, cuando no debe
            
            //SUMAR PUNTOS DE FICHAS ALINEADAS HORIZONTALMENTE
            for (int i = 0; i <= 3; i++) {     //recorro cada una de las filas buscando fichas alineadas horizontalmente
                int puntajeActual = 1;
                int puntajeAnterior = 0;
                for (int j = 0; j <= 3; j++){
                    if (this.inicialesJugadores[n].equals(tablero[i][j]) &&
                        this.inicialesJugadores[n].equals(tablero[i][j+1]) ){
                        puntajeActual = puntajeActual +1;
                    }
                    else{
                        if (puntajeActual > puntajeAnterior){
                            puntajeAnterior = puntajeActual;
                        }    
                        puntajeActual = 1;
                    }
                }
                if (Math.max(puntajeActual,puntajeAnterior) >= 3){
                    this.puntajesJugadores[n] = this.puntajesJugadores[n] + Math.max(puntajeActual,puntajeAnterior);
                }
            }    
            
            //SUMAR PUNTOS DE FICHAS ALINEADAS VERTICALMENTE
            for (int j = 0; j <= 4; j++) {     //recorro cada una de las columnas buscando fichas alineadas verticalmente
                int puntajeActual = 1;
                int puntajeAnterior = 0;
                for (int i = 0; i <= 2; i++){
                    if (this.inicialesJugadores[n].equals(tablero[i][j]) &&
                        this.inicialesJugadores[n].equals(tablero[i+1][j]) ){
                        puntajeActual = puntajeActual +1;
                    }
                    else{
                        if (puntajeActual > puntajeAnterior){
                            puntajeAnterior = puntajeActual;
                        }    
                        puntajeActual = 1;
                    }
                }
                if (Math.max(puntajeActual,puntajeAnterior) >= 3){
                    this.puntajesJugadores[n] = this.puntajesJugadores[n] + Math.max(puntajeActual,puntajeAnterior);
                }
            }
            
            //SUMAR PUNTOS EN FICHAS ALINEADAS EN DIAGONAL
            //PRIMERO sumo puntos de las fichas alineadas en las diagonales en el sentido desde izquierda-arriba 
            //hacia derecha-abajo (mismo sentido que diagonal principal)
            calcularPuntajesDiagonalesMismoSentidoQueDiagonalPrincipal(this.tablero , n);
            //AHORA sumo puntos de las fichas alineadas en las diagonales en el sentido desde derecha-arriba 
            //hacia izquierda-abajo (sentido opuesto que diagonal principal)
            String[][] matriz = EspejarTableroRespectoAColumna2();
            calcularPuntajesDiagonalesMismoSentidoQueDiagonalPrincipal(matriz , n);
        }
    }    
    
    private void calcularPuntajesDiagonalesMismoSentidoQueDiagonalPrincipal(String[][] matriz , int numJugador){ 
        //Este metodo se fija si hay fichas alineadas en las diagonales en el sentido desde izquierda-arriba 
        //hacia derecha-abajo (mismo sentido que diagonal principal). hay 4 diagonales asi que puedan sumar puntos.
        //numJugador es la posicion en el array inicialesJugadores (puede valer 0 o 1)
        for (int k = -1; k <= 2; k++) {  //k es la columna en la que empieza arriba a la izquierda
            //k=-1 es para que "empiece en la posicion [0][-1]", pero en ese caso no entra y luego 
            //se fija la posicion [1][0] , k=-1 es para recorrer la diagonal de mas abajo
            int puntajeActual = 1;
            int puntajeAnterior = 0;
            for (int i = 0; i <= 2; i++){       //i es la fila 
                if ( (i+k >= 0) && (i+k+1 <= 4) ){      //me aseguro de que sean columnas validas de la matriz
                    if (this.inicialesJugadores[numJugador].equals(matriz[i][i+k]) &&
                        this.inicialesJugadores[numJugador].equals(matriz[i+1][i+k+1]) ){
                        puntajeActual = puntajeActual +1;
                    }
                    else{
                        if (puntajeActual > puntajeAnterior){
                            puntajeAnterior = puntajeActual;
                        }    
                        puntajeActual = 1;
                    }
                }
            }
            if (Math.max(puntajeActual,puntajeAnterior) >= 3){
                this.puntajesJugadores[numJugador] = this.puntajesJugadores[numJugador] + Math.max(puntajeActual,puntajeAnterior);
            }
        }
    }

    private String[][] EspejarTableroRespectoAColumna2(){
        //metodo que devuelve una matriz nueva, que se obtiene a partir del tablero, espejandolo 
        //con respecto a la columna 2, es decir, ver esta matriz de izquierda a derecha 
        //es lo mismo que mirar el tablero de derecha a izquierda
        String[][] matriz = new String[4][5];
        for (int j=0 ; j<=1 ; j++){     //asigno las primeras 2 columnas de la nueva matriz
            for (int i=0 ; i<=3 ; i++){
                matriz[i][j] = this.tablero[i][4-j];
            }
        }
        for (int i=0 ; i <= 3 ; i++){       //asigno la columna 2 a la nueva matriz (es la columna central, que es igual que la de tablero)
            matriz[i][2] = this.tablero[i][2];
        }
        for (int j=3 ; j<=4 ; j++){     //asigno las ultimas 2 columnas de la nueva matriz
            for (int i=0 ; i<=3 ; i++){
                matriz[i][j] = this.tablero[i][4-j];
            }
        }
        return matriz;
    }
    
    //METODOS CORRESPONDIENTES A LA OPCION SOLICITAR AYUDA (case "A")
    
    private ArrayList< ArrayList<Integer> > dadosExtrasJuntoConPosicionLibreCorrespondienteAlFinal(){
        
        ArrayList< ArrayList<Integer> > listaDadosExtrasYPosicionAlFinal = new ArrayList<>();
        ArrayList<Integer> listaDadosExtras;
        ArrayList<Integer> sumasValidas = new ArrayList<>();
        
        for (int i=1 ; i<=15 ; i++){        //no considero i=0 porque no quiero tener en cuenta el base solo aqui, lo tengo en cuenta al imprimir posibles jugadas
            listaDadosExtras = new ArrayList<>();
            int suma = 0;
            
            if (i%2 > 0){       //si i%2 > 0, quiere decir que en binario el numero i es de la forma bbb1, y por lo tanto sumo la pos 4 de dados
                suma = suma + jugada.getDados()[4];
                listaDadosExtras.add(jugada.getDados()[4]);
            }
            if (i%4 > 1){       //si i%4 > 1, quiere decir que en binario el numero i es de la forma bb1b, y por lo tanto sumo la pos 3 de dados
                suma = suma + jugada.getDados()[3];
                listaDadosExtras.add(jugada.getDados()[3]);
            }
            if (i%8 > 3){       //si i%8 > 3, quiere decir que en binario el numero i es de la forma b1bb, y por lo tanto sumo la pos 2 de dados
                suma = suma + jugada.getDados()[2];
                listaDadosExtras.add(jugada.getDados()[2]);
            }            
            if (i > 7){      //si i > 7, quiere decir que en binario el numero i es de la forma 1bbb, y por lo tanto sumo la pos 1 de dados
                suma = suma + jugada.getDados()[1];
                listaDadosExtras.add(jugada.getDados()[1]);
            }
            suma = suma + jugada.getDados()[0];     //el dado base siempre se suma
            
            if ( (suma <= 20) && !sumasValidas.contains(suma) ){    //si la suma corresponde a posicion valida del tablero y no esta comprendida aun esa posicion
                sumasValidas.add(suma);
                agregarAListaDadosExtrasYPosicionAlFinal( listaDadosExtrasYPosicionAlFinal , listaDadosExtras , suma);
            }
            
            
        }
        
        return listaDadosExtrasYPosicionAlFinal;
    }


    private void agregarAListaDadosExtrasYPosicionAlFinal( ArrayList< ArrayList<Integer> > listaDadosExtrasYPosicionAlFinal, ArrayList<Integer> listaDadosExtras , int pos){
        listaDadosExtras.add(pos);
        if ( this.posicionEstaLibre(pos) && !listaDadosExtrasYPosicionAlFinal.contains(listaDadosExtras) ){
            listaDadosExtrasYPosicionAlFinal.add(listaDadosExtras);
        }   
    }
    
    private void imprimirJugadasPosibles(ArrayList< ArrayList<Integer> > listaDadosExtrasYPosicionAlFinal){
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("JUGADAS POSIBLES:  (mostrando una unica combinacion de dados para cada posicion posible)");
        if (this.posicionEstaLibre(jugada.getDados()[0])){
            System.out.println("Jugar solo el dado base ( opcion '0' ) , " + "correspondiente a la posicion " + jugada.getDados()[0]);
        }
        for (int i=0 ; i < listaDadosExtrasYPosicionAlFinal.size() ; i++){
            System.out.print("Jugar los extras: ");
            for (int j =0 ; j < listaDadosExtrasYPosicionAlFinal.get(i).size()-1 ; j++ ){   //recorro hasta la penultima posicion, porque en la ultima esta la suma obtenida
                System.out.print(listaDadosExtrasYPosicionAlFinal.get(i).get(j) + " ");
            }
            //imprimo el ultimo elemento de la lista de Integer que esta en la posicion i de la lista de listas.
            System.out.println(", correspondiente a la posicion " + listaDadosExtrasYPosicionAlFinal.get(i).get( listaDadosExtrasYPosicionAlFinal.get(i).size()-1 ) );
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    
    //METODOS CORRESPONDIENTES A LA OPCION DE JUGADA DADOS EXTRAS ("n1 n2 n3 n4")
    
    private boolean ingresarFichaEnPosicionCorrespondiente( Jugada jugada ){ 
        boolean permanecerEnLaMismaJugada = false;
        int suma = jugada.getDados()[0];
        int[] extrasIngresados = jugada.pasarOpcionIngresadaAExtrasCorrespondientes();
        for (int i =0 ; i < extrasIngresados.length ; i++){
            suma = suma + extrasIngresados[i];
        }
        if (this.posicionEstaLibre(suma)){
            this.ingresarInicialEnTablero(suma);
            this.cambiarTurno();
        }
        else{
            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.println("!!!!!!!!!!!!!!!!!!  LA POSICION DEL TABLERO INDICADA ESTA OCUPADA, reingrese jugada:   !!!!!!!!!!!!!!!!!!");
            System.out.println("---------------------------------------------------------------------------------------------------------");
            this.imprimirTablero();
            permanecerEnLaMismaJugada = true;
        }
        return permanecerEnLaMismaJugada;
    }
    
    
    private boolean tieneSoloLetras(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            char caracter = cadena.toUpperCase().charAt(i);
            int valorASCII = (int) caracter;
            if ( (valorASCII != 165) && (valorASCII < 65 || valorASCII > 90)) {
                return false; //Se ha encontrado un caracter que no es letra 
            }
        }

        //Terminado el bucle sin que se haya retornado false, es que todos los caracteres son letras
        return true;
    }
    
    


    

    

    public boolean isPartidaEnCurso() {
        return partidaEnCurso;
    }

    public void setPartidaEnCurso(boolean partidaEnCurso) {
        this.partidaEnCurso = partidaEnCurso;
    }

    public Jugada getJugada() {
        return jugada;
    }

    public void setJugada(Jugada jugada) {
        this.jugada = jugada;
    }

    public boolean isModoTest() {
        return modoTest;
    }

    public void setModoTest(boolean modoTest) {
        this.modoTest = modoTest;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public String[][] getTablero() {
        return tablero;
    }

    public void setTablero(String[][] tablero) {
        this.tablero = tablero;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    public String[] getInicialesJugadores() {
        return inicialesJugadores;
    }

    public void setInicialesJugadores(String[] inicialesJugadores) {
        this.inicialesJugadores = inicialesJugadores;
    }

    public int[] getPuntajesJugadores() {
        return puntajesJugadores;
    }

    public void setPuntajesJugadores(int[] puntajesJugadores) {
        this.puntajesJugadores = puntajesJugadores;
    }

    public boolean isTurnoJugador1() {
        return turnoJugador1;
    }

    public void setTurnoJugador1(boolean turnoJugador1) {
        this.turnoJugador1 = turnoJugador1;
    }

}
