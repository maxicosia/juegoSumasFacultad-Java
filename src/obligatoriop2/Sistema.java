package obligatoriop2;

import java.util.ArrayList;

public class Sistema {

    private ArrayList<Jugador> listaJugadores;

    public Sistema() {
        this.listaJugadores = new ArrayList<>();
    }
    
    public void agregarJugador(Jugador unJ){
        this.getListaJugadores().add(unJ);
    }
    
    

    public ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void setListaJugadores(ArrayList<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    
}
