
import java.util.concurrent.ConcurrentLinkedQueue;

public class Terminal {

    private char nombre;
    private int inicioPuestos;
    private int finPuestos;
    private ConcurrentLinkedQueue<String> salaEmbarque;
    private FreeShop freeShop;

    public Terminal(char nombre, int inicio, int fin) {
        this.nombre = nombre;
        this.inicioPuestos = inicio;
        this.finPuestos = fin;
        this.salaEmbarque = new ConcurrentLinkedQueue<>();
    }

    public void esperarEnSala(String pasajero) {
        System.out.println(pasajero + " espera en la sala de embarque de Terminal " + nombre);
        salaEmbarque.add(pasajero);
    }

    public boolean perteneceAPuesto(int puesto) {
        return puesto >= inicioPuestos && puesto <= finPuestos;
    }

    public char getNombre() {
        return nombre;
    }

    public FreeShop getFreeShop(){
        return freeShop;
    }
}
