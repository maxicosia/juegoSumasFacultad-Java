package obligatoriop2;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Sistema sistema = new Sistema();

        boolean terminarPrograma = false;

        while (!terminarPrograma) {

            Scanner input = new Scanner(System.in);
            Menu.imprimirMenu();
            String opcion = Menu.leerOpcionMenu(input);

            switch (opcion) {
                case "a":
                    //Creo nuevo jugador
                    Jugador jugador = new Jugador();
                    
                    //LEO NOMBRE
                    jugador.leerYAsignarNombre(input);
                    
                    //LEO EDAD
                    jugador.leerYAsignarEdad(input);

                    //LEO ALIAS
                    jugador.leerYAsignarAlias(input, sistema);
                    
                    //agrego jugador a la lista, una vez que el jugador esta creado
                    sistema.agregarJugador(jugador);    
                            
                    break;
                case "b":

                    Partida partida = new Partida(Partida.esModoTest(input));
                    //al llamar al constructor Partida() se inicializa el tablero como debe, 
                    //se inicializa turnoJugador1 en true

                    if (sistema.getListaJugadores().size() < 2) {
                        System.out.println("Debe haber al menos 2 jugadores registrados para jugar a Sumas");
                    } else {
                        partida.seleccionDeJugadores(sistema.getListaJugadores());

                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    COMIENZA LA PARTIDA    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        partida.imprimirTablero();

                        while (partida.isPartidaEnCurso()) {
                            partida.hacerJugada(input);
                        }

                        partida.codigoAEjecutarCuandoTerminaLaPartida(partida.getJugada().getOpcionDeJugada());
                    }

                    break;
                case "c":
                    System.out.println("---------------------------------------------------------------------------------------------------------");
                    System.out.println("RANKING DE JUGADORES:");
                    Collections.sort(sistema.getListaJugadores());
                    
                    int i = 1;
                    for (Jugador elemento : sistema.getListaJugadores()) {
                        System.out.print("Puesto Nº " + i + " )");
                        System.out.println(elemento);
                        i = i+1;
                    }
                    System.out.println("---------------------------------------------------------------------------------------------------------");
                    
                    break;
                case "d":
                    terminarPrograma = true;
                    break;
            }

        }

    }

}
