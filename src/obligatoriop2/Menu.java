package obligatoriop2;

import java.util.*;

public class Menu {
    
    public static void imprimirMenu() {
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   MENU:   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("a) Registrar jugador.");
        System.out.println("b) Jugar a 'Sumas'.");
        System.out.println("c) Ver ranking de jugadores.");
        System.out.println("d) Terminar.");
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    public static String leerOpcionMenu(Scanner input) {
        System.out.println("Ingrese opcion('a', 'b', 'c' o 'd'): ");
        String opcion = input.nextLine();

        while (!opcion.equals("a") && !opcion.equals("b") && !opcion.equals("c") && !opcion.equals("d")) {
            System.out.println("Valor ingresado incorrecto: debe ser 'a', 'b', 'c' o 'd'.");
            imprimirMenu();
            System.out.println("Ingrese opcion('a', 'b', 'c' o 'd'): ");
            opcion = input.nextLine();
        }

        return opcion;
    }
    

}
