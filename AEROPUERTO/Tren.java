import java.util.concurrent.Semaphore;

public class Tren {
    public static final String MAGENTA = "\u001B[35m"; // colores para la salida por pantalla 
    public static final String RESET = "\u001B[0m"; // colores para la salida por pantalla 

    private int capacidad;
    private int pasajeros;
    private char terminalActual;

    private boolean enRecorrido;

    // Semáforos
    private Semaphore permisoSubir;
    private Semaphore trenLleno;
    private Semaphore mutex;

    public Tren(int capacidad) {
        this.capacidad = capacidad;
        this.pasajeros = 0;
        this.enRecorrido = false;

        this.permisoSubir = new Semaphore(0);
        this.trenLleno = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    // ================= PASAJERO =================

    public void subirTren(String pasajero, char terminal) {
        try {
            permisoSubir.acquire(); // Espera autorización

            mutex.acquire();
            pasajeros++;
            System.out.println(pasajero + " subió al Tren con destino a la Terminal " 
                    + terminal + ". " + pasajeros + "/" + capacidad);

            if (pasajeros == capacidad) {
                trenLleno.release(); // Avisar al chofer
            }
            mutex.release();

        } catch (InterruptedException e) {
            System.out.println("ERROR al subir pasajero " + pasajero);
        }
    }

    public void bajarTren(String pasajero, char terminal) {
        try {
            synchronized (this) {
                while (terminal != terminalActual) {
                    this.wait();
                }
            }

            mutex.acquire();
            pasajeros--;
            System.out.println(pasajero + " bajó en la Terminal " + terminal 
                    + ". " + pasajeros + "/" + capacidad);
            mutex.release();

        } catch (Exception e) {
            System.out.println("ERROR al bajar pasajero " + pasajero);
        }
    }

    // ================= CONTROL DEL TREN =================

    public void habilitarAcceso() {
        System.out.println(MAGENTA + "El Tren está habilitado para subir pasajeros" + RESET);
        permisoSubir.release(capacidad); // Permite subir a todos
    }

    public void iniciarRecorrido() {
            enRecorrido = true;
            System.out.println(MAGENTA + "El Tren inició el recorrido"+ RESET);
    }

    public synchronized void viajarATerminal(char terminal) {
        terminalActual = terminal;
        System.out.println(MAGENTA + "El Tren llegó a la Terminal " + terminal + RESET);
        this.notifyAll();
    }

    public void finalizarRecorrido() {
        enRecorrido = false;
        System.out.println(MAGENTA + "El Tren finalizó el recorrido y vuelve al inicio" + RESET);
    }
}
