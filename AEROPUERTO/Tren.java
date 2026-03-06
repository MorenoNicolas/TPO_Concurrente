public class Tren {
    public static final String MAGENTA = "\u001B[35m";
    public static final String RESET = "\u001B[0m";

    private final int capacidad;
    private int pasajeros;
    private char terminalActual;
    private boolean puertasAbiertas; // true mientras el tren acepta pasajeros

    public Tren(int capacidad) {
        this.capacidad = capacidad;
        this.pasajeros = 0;
        this.terminalActual = ' ';
        this.puertasAbiertas = false;
    }

    // ================= PASAJERO =================
    public synchronized void subirTren(String pasajero, char terminalDestino)
            throws InterruptedException {
        // Espera condición: puertas abiertas Y hay capacidad
        while (!puertasAbiertas || pasajeros >= capacidad) {
            this.wait();
        }
        pasajeros++;
        System.out.println(pasajero + " subió al Tren con destino a Terminal "
                + terminalDestino + ". Pasajeros: " + pasajeros + "/" + capacidad);
    }

    public synchronized void bajarTren(String pasajero, char terminalDestino)
            throws InterruptedException {
        // Espera condición: el tren está en la terminal del pasajero
        while (terminalDestino != terminalActual) {
            this.wait();
        }
        pasajeros--;
        System.out.println(pasajero + " bajó en la Terminal " + terminalDestino
                + ". Pasajeros restantes: " + pasajeros);
        this.notifyAll(); // por si ControlTren espera que el tren quede vacío
    }

    // ================= CONTROL DEL TREN =================

    public synchronized void habilitarAcceso() {
        terminalActual = ' ';
        puertasAbiertas = true;
        System.out.println(MAGENTA + "=== EL TREN ABRE PUERTAS (Inicio de recorrido) ===" + RESET);
        this.notifyAll(); // despierta pasajeros que esperaban para subir
    }

    public synchronized void cerrarPuertas() {
        puertasAbiertas = false;
        System.out.println(MAGENTA + "=== EL TREN CIERRA PUERTAS ===" + RESET);
        this.notifyAll(); // despierta eventuales hilos esperando en subirTren para que reevalúen
    }

    public synchronized void iniciarRecorrido() {
        System.out.println(MAGENTA + "El Tren inició el recorrido hacia las terminales..." + RESET);
    }

    public synchronized void viajarATerminal(char terminal) {
        terminalActual = terminal;
        System.out.println(MAGENTA + ">> El Tren llegó a la Terminal " + terminal + RESET);
        this.notifyAll(); // despierta a todos los que esperan en bajarTren
    }

    public synchronized void finalizarRecorrido() {
        terminalActual = ' ';
        puertasAbiertas = false;
        System.out.println(MAGENTA + "El Tren finalizó el recorrido y vuelve al inicio" + RESET);
    }
}