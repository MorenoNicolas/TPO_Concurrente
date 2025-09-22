
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Vuelo {

    private String aerolinea;
    private Terminal terminal;
    private int[] puestosEmbarque;
    private int horaSalida;
    private int cantidadPasajeros;
    private boolean embarqueIniciado;
    private boolean yaDespego;
    private CountDownLatch latchEmbarque;
    private Random random = new Random();

    public int asignarPuestoEmbarque() {
        int puesto = (int) (Math.random() * 20) + 1; // 1 a 20
        if (puesto <= 7) {
            return puesto;              // Terminal A
        } else if (puesto <= 15) {
            return puesto;              // Terminal B
        } else {
            return puesto;              // Terminal C
        }
    }

    
    public Terminal getTerminal() {
        return terminal;

    }
}
