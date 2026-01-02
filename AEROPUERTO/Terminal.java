
import java.util.concurrent.ConcurrentLinkedQueue;

public class Terminal {

    private char nombre;
    private int inicioPuestos;
    private int finPuestos;
    private ConcurrentLinkedQueue<String> salaEmbarque;
    private FreeShop freeShop;
    private int[] puestosEmbarque;

    public Terminal(char nombre, int[] puestosEmbarque2, FreeShop freeShop2) {
        this.nombre = nombre;
        this.puestosEmbarque = puestosEmbarque2;
        this.freeShop = freeShop2;
        this.salaEmbarque = new ConcurrentLinkedQueue<>();
    }

    public boolean perteneceAPuesto(int puesto) {
        for (int p : puestosEmbarque) {
            if (puesto == p) {
                return true;
            }
        }
        return false;
    }

    public char getNombre() {
        return nombre;
    }

    public FreeShop getFreeShop(){
        return freeShop;
    }

    public int[] getPuestosEmbarque() {
        return puestosEmbarque;
    }
}
