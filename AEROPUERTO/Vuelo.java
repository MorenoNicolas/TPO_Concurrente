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

public Vuelo(String aerolinea, Terminal terminal, int[] puestosEmbarque, int horaSalida) {
        this.aerolinea = aerolinea;
        this.terminal = terminal;
        this.puestosEmbarque = puestosEmbarque;
        this.horaSalida = horaSalida;
        this.cantidadPasajeros = 0;
        this.embarqueIniciado = false;
        this.yaDespego = false;
    }
    
    // Metodo para Main
    public synchronized void registrarReserva() {
        // Para llevar la cuenta de la cantidad de pasajeros que adquirieron reserva del vuelo
        cantidadPasajeros++;
    }

    // Metodo para Main
    public synchronized void inicializarCountDownLatch() {
        // Para que el avion despegue cuando suban todos los pasajeros
        System.out.println("Vuelo de " + aerolinea + ": " + cantidadPasajeros + " reservas");
        latchEmbarque = new CountDownLatch(cantidadPasajeros);
    }

    // Metodo para Pasajero
    public void embarcarEsperarDespegue(String pasajero, int puestoEmbarque) {
        try {
            System.out.println(pasajero + " abordó el avión de " + aerolinea + " en el Puesto de Embarque " + puestoEmbarque);
            latchEmbarque.countDown();
            latchEmbarque.await();
            synchronized (this) {
                if (!yaDespego && latchEmbarque.getCount() == 0) {
                    yaDespego = true;
                    System.out.println("\u001B[31m" + "Todos los pasajeros abordaron el avión. El Vuelo de " + aerolinea + " ha despegado " + "\u001B[0m");
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: Ocurrió un problema con el " + pasajero + " al intentar abordar el avión: " + e.getMessage());
        }
    }

    public int asignarPuestoEmbarque() {
        char t = terminal.getNombre();
        int min, max;
        if (t == 'A') {
            min = 1; max = 7;        // Terminal A
        } else if (t == 'B') {
            min = 8; max = 14;       // Terminal B
        } else{
            min = 15; max = 21;      // Terminal C
        }
        return min + random.nextInt(max - min + 1);
    }

    public Terminal getTerminal() {
        return terminal;

    }
    
    // Metodo para Tiempo
    public synchronized void notificarComienzoEmbarque() {
        if(!embarqueIniciado) {
            embarqueIniciado = true;
            System.out.println("El Vuelo de " + aerolinea + " está listo para comenzar a embarcar");
        }
    }

    public String getAerolinea() {
        return aerolinea;
    }
    // Random para el pasajero
    public int getPuestoEmbarque() {
        return puestosEmbarque[random.nextInt(puestosEmbarque.length)];
    }

    public int getHoraSalida() {
        return horaSalida;
    }

}
