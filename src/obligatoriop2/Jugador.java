package obligatoriop2;

import java.util.*;

class Jugador implements Comparable<Jugador> {

    private String nombre;
    private int edad;
    private String alias;
    private int partidasJugadas;
    private Integer partidasGanadas;

    public Jugador() {
        this.nombre = "Sin nombre";
        this.edad = 20;
        this.alias = "Sin alias";
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
    }

    @Override
    public String toString() {
        return "Jugador de nombre: " + this.nombre + " y alias: " + this.alias + ", Ganó " + this.partidasGanadas + " partidas.";
    }

    @Override
    public int compareTo(Jugador jugador) {
        //Ordenado por ganadas decreciente
        return new Integer(jugador.getPartidasGanadas()).compareTo(this.partidasGanadas);
    }

    public boolean tieneSoloLetrasOEspacios(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            char caracter = cadena.toUpperCase().charAt(i);
            int valorASCII = (int) caracter;
            if ( (valorASCII != 165) && (valorASCII != 32) && (valorASCII < 65 || valorASCII > 90)) {
                return false; //Se ha encontrado un caracter que no es letra ni es espacio
            }
        }

        //Terminado el bucle sin que se haya retornado false, es que todos los caracteres son letras
        return true;
    }

    public void incrementarEn1PartidasJugadas() {
        this.partidasJugadas++;
    }

    public void incrementarEn1PartidasGanadas() {
        this.partidasGanadas++;
    }
    
    public void leerYAsignarNombre(Scanner input){
        System.out.println("Ingrese nombre: ");
        String nombre = input.nextLine();
        while( !(this.tieneSoloLetrasOEspacios(nombre)) ){
            System.out.println("Nombre no valido, solo puede estar compuesto por letras o espacios, reingrese nombre: ");
            nombre = input.nextLine();
        }
        this.setNombre(nombre);
    }
    
    public void leerYAsignarEdad(Scanner input){
        System.out.println("Ingrese edad: ");
        int edad = 0;
        while ( !(edad > 0) ){
            boolean correcto = false;
            while (!correcto){
                try{
                    edad = input.nextInt();
                    correcto= true;
                } 
                catch(InputMismatchException e){
                    System.out.println("Error, la edad que ingresó no es un entero, reingresar edad:"); 
                    input.nextLine(); 
                } 
            }
            input.nextLine();   //pongo esto porque se leyo un entero, para que no de error la siguiente lectura de string

            if ( !(edad > 0) ){
                System.out.println("La edad ingresada debe ser un numero entero positivo, reingrese edad:");
            }
        }
        this.setEdad(edad);
    }
    
    public void leerYAsignarAlias( Scanner input , Sistema sistema ){
        boolean aliasRepetido = false;
        String alias;
        do{
            if ( !aliasRepetido ){
                System.out.println("Ingrese alias: ");
                alias = input.nextLine();
            }
            else{
                System.out.println("El alias ingresado ya existe, ingrese otro alias:");
                alias = input.nextLine();
            }
            //me fijo si el alias ingresado ya existe en la lista de jugadores
            aliasRepetido = false;
            for ( int i=0 ; i < sistema.getListaJugadores().size() ; i++){
                if ( (sistema.getListaJugadores().get(i).getAlias()).equals(alias) ){
                    aliasRepetido = true;
                }
            }
        }while ( aliasRepetido );
        this.setAlias( alias );
    }
    
    
    
    
    

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getAlias() {
        return alias;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }
}
