
public class ControlTren extends Thread {

    public static final String MAGENTA = "\u001B[35m"; // colores para la salida por pantalla (mas legible)
    public static final String RESET = "\u001B[0m"; // colores para la salida por pantalla (mas legible)

    private Tren tren;
    private Terminal[] terminales;

    public ControlTren(Tren tren, Terminal[] terminales) {
        this.tren = tren;
        this.terminales = terminales;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // espera que el tren esté lleno
                tren.esperarLleno();

                System.out.println("El tren inicia su recorrido...");

                // recorre todas las terminales
                for (int i = 0; i < terminales.length; i++) {
                    System.out.println("El tren llegó a Terminal " + terminales[i].getNombre());
                    tren.bajarTren(terminales[i]);
                    Thread.sleep(500); // simula parada
                }

                // asegura que quedó vacío en la última terminal
                if (!tren.estaVacio()) {
                    System.out.println("El tren llegó a la última terminal pero aún quedan pasajeros.");
                }

                System.out.println("El tren regresa vacío al inicio.");
                tren.finalizarRecorrido();
            }
        } catch (InterruptedException e) {
            System.out.println("ControlTren interrumpido.");
        }
    }
}
