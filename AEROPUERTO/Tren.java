import java.util.concurrent.Semaphore;

public class Tren {
    public static final String MAGENTA = "\u001B[35m"; 
    public static final String RESET = "\u001B[0m"; 

    private int capacidad;
    private int pasajeros;
    private char terminalActual;

    // Semáforos
    private Semaphore permisoSubir; // Controla la capacidad y el estado de puertas abiertas
    private Semaphore mutex;        // Exclusión mutua para variables compartidas

    public Tren(int capacidad) {
        this.capacidad = capacidad;
        this.pasajeros = 0;
        this.terminalActual = ' '; // Ninguna terminal al inicio

        // Iniciamos en 0, el ControlTren dará los permisos cuando abra puertas
        this.permisoSubir = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    // ================= PASAJERO =================

    public void subirTren(String pasajero, char terminalDestino) {
        try {
            // El pasajero se bloquea aquí si el tren no está en estación inicial o está lleno
            permisoSubir.acquire(); 

            mutex.acquire();
            pasajeros++;
            System.out.println(pasajero + " subió al Tren con destino a Terminal " 
                    + terminalDestino + ". Pasajeros: " + pasajeros + "/" + capacidad);
            mutex.release();

        } catch (InterruptedException e) {
            System.out.println("ERROR al subir pasajero " + pasajero);
        }
    }

    public void bajarTren(String pasajero, char terminalDestino) {
        try {
            synchronized (this) {
                // Mientras no estemos en la terminal del pasajero, espera
                while (terminalDestino != terminalActual) {
                    this.wait();
                }
            }

            mutex.acquire();
            pasajeros--;
            System.out.println(pasajero + " bajó en la Terminal " + terminalDestino 
                    + ". Pasajeros restantes: " + pasajeros);
            mutex.release();

        } catch (Exception e) {
            System.out.println("ERROR al bajar pasajero " + pasajero);
        }
    }

    // ================= CONTROL DEL TREN =================

    public void habilitarAcceso() {
        terminalActual = ' '; // Reseteamos terminal
        System.out.println(MAGENTA + "=== EL TREN ABRE PUERTAS (Inicio de recorrido) ===" + RESET);
        
        // Drenamos permisos viejos por seguridad y damos permisos nuevos según capacidad
        permisoSubir.drainPermits(); 
        permisoSubir.release(capacidad); 
    }

    public void cerrarPuertas() {
        System.out.println(MAGENTA + "=== EL TREN CIERRA PUERTAS ===" + RESET);
        // Quitamos todos los permisos restantes para que nadie más suba
        permisoSubir.drainPermits();
    }

    public void iniciarRecorrido() {
        System.out.println(MAGENTA + "El Tren inició el recorrido hacia las terminales..."+ RESET);
    }

    public synchronized void viajarATerminal(char terminal) {
        terminalActual = terminal;
        System.out.println(MAGENTA + ">> El Tren llegó a la Terminal " + terminal + RESET);
        // Notificamos a todos los pasajeros que están esperando (wait) en bajarTren
        this.notifyAll();
    }

    public void finalizarRecorrido() {
        terminalActual = ' ';
        System.out.println(MAGENTA + "El Tren finalizó el recorrido y vuelve al inicio" + RESET);
    }
}