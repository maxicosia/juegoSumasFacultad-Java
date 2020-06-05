package obligatoriop2;

import static java.lang.String.valueOf;
import java.util.*;

class Jugada {

    private String opcionDeJugada;
    private int[] dados;

    public Jugada() {
        this.opcionDeJugada = "opcion no ingresada aun";
        this.dados = new int[5];
    }
    
    public void imprimirOpcionesDeJugadas(){
        System.out.println("Opciones de Jugadas:");
        System.out.println("X : Abandonar y perder la partida");
        System.out.println("P : Pasar de turno");
        System.out.println("0 (numero cero) : Usar solo el dado base");
        System.out.println("n1 n2 n3 n4 : Siendo n1 n2 n3 n4 los valores obtenidos en los dados extras, se puede indicar de 1 hasta 4 valores, sin importar el orden");
        System.out.println("A : Solicitar ayuda");
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    public String leerOpcionDeJugada(Scanner input){
        String opcion = "opcion no ingresada aun";
        boolean opcionValida = false;
        while ( !opcionValida ){
            System.out.println("Ingrese opcion de jugada: ('X' , 'P' , '0' (numero cero), 'n1 n2 n3 n4' , 'A' )");
            opcion = input.nextLine();
            if ( (!opcion.equals("X")) && (!opcion.equals("P")) && (!opcion.equals("0")) && (!opcion.equals("A")) ){
                String opcionSinEspacios = opcion.replaceAll(" ", "");
                if ( this.tieneSoloDigitosPositivos(opcionSinEspacios) ){
                    if ( opcionSinEspacios.length() > 4){
                        System.out.println("Error, demasiados valores de dados extras ingresados, debe ingresar como maximo 4 valores, reingrese opcion de jugada: ");
                    }
                    else{   //si llega aca es porque es una opcion de jugada valida
                        opcionValida = true;
                    }
                }
                else{
                    System.out.println("Error, opcion de jugada ingresada no valida, reingrese:");
                }
            }
            else{
                opcionValida = true;
            }    
        }
        return opcion;
    }
    
    private boolean tieneSoloDigitosPositivos(String cadena) {
        boolean tieneSoloDigitosPositivos = true;
        for (int i = 0; i < cadena.length(); i++) {
            char caracter = cadena.charAt(i);
            int valorASCII = (int) caracter;
            if (valorASCII < 49 || valorASCII > 57) {
                tieneSoloDigitosPositivos = false; //Se ha encontrado un caracter que no es un digito positivo
            }
        }
        return tieneSoloDigitosPositivos;
    }

    public void lanzarDados( boolean modoTest , Scanner input ) {
        if (modoTest){
            System.out.println("Ingrese los 5 dados, el primero es el base: ");

            for (int i =0; i < this.dados.length ; i++){
                System.out.print("Ingrese valor del dado (valor entre 1 y 6): ");
                int dado = 0;
                while ( !( (dado >=1 ) && (dado <= 6) ) ){
                    boolean correcto = false;
                    while (!correcto){
                        try{
                            dado = input.nextInt();
                            correcto= true;
                        } 
                        catch(InputMismatchException e){
                            System.out.println("Error, el valor del dado que ingresó no es un entero, reingresar valor del dado:"); 
                            input.nextLine(); 
                        } 
                    }
                    input.nextLine();   //pongo esto porque se leyo un entero, para que no de error la siguiente lectura de string

                    if ( !( (dado >=1 ) && (dado <= 6) ) ){
                        System.out.println("El dado ingresado debe ser un entero entre 1 y 6, reingrese valor del dado:");
                    }
                }
                this.dados[i] = dado;
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        }
        else{
            for (int i = 0; i < this.dados.length; i++) {
                this.dados[i] = new Random().nextInt(5 + 1) + 1;
            }
        }
    }

    public void mostrarDados() {
        System.out.println("Dados obtenidos:");
        System.out.println("Dado Base:\t" + dados[0]);
        System.out.println("Dados Extras:\t" + dados[1] + "\t" + dados[2] + "\t" + dados[3] + "\t" + dados[4]);
    }

    public boolean dadosExtrasIngresadosValidos() {     //caso en que el usuario ingresa dados extras (n1 n2 n3 n4)
        //metodo que corrobora que los valores ingresados en esta opcion (n1n2n3n4) sean efectivamente dados extra sin repetirse
        
        boolean estan = true;
        this.opcionDeJugada = this.opcionDeJugada.replaceAll(" ", "");
        if (this.opcionDeJugada.length() > 4){
            estan = false;
        }
        else{
            int[] extrasIngresados = pasarOpcionIngresadaAExtrasCorrespondientes();

            ArrayList<Integer> dadosExtras = new ArrayList<>();
            for (int i = 1 ; i<=4 ; i++){
                dadosExtras.add(this.dados[i]);
            }

            int i = 0;

            while (estan && (i < extrasIngresados.length)){
                if (dadosExtras.contains(extrasIngresados[i])){
                    dadosExtras.remove(new Integer(extrasIngresados[i]));
                }
                else{
                    estan = false;
                }
                i++;
            }            
        }
        return estan;
    }
    
    
    public int[] pasarOpcionIngresadaAExtrasCorrespondientes(){
        int[] extrasIngresados = new int[this.opcionDeJugada.length()];
        int opcionEntero = Integer.parseInt(this.opcionDeJugada);
        for (int i=0 ; i < extrasIngresados.length ; i++){
            extrasIngresados[i]= opcionEntero % 10;
            opcionEntero = opcionEntero / 10;
        }
        return extrasIngresados;
    }
    
    
    
    
    
    public String getOpcionDeJugada() {
        return opcionDeJugada;
    }

    public void setOpcionDeJugada(String opcionDeJugada) {
        this.opcionDeJugada = opcionDeJugada;
    }

    public int[] getDados() {
        return dados;
    }

    public void setDados(int[] dados) {
        this.dados = dados;
    }

}
