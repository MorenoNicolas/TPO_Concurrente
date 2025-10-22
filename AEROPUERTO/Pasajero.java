
import java.util.Random;

public class Pasajero extends Thread {

    private String nombre;
    private Aeropuerto aeropuerto;
    private Vuelo vuelo;
    private Tiempo tiempo;
    private Random random;
    private Terminal terminalAsignada;

    public Pasajero(String nombre, Aeropuerto aeropuerto, Vuelo vuelo, Tiempo tiempo) {
        this.nombre = nombre;
        this.aeropuerto = aeropuerto;
        this.vuelo = vuelo;
        this.tiempo = tiempo;
        this.random = new Random();
        this.terminalAsignada = vuelo.getTerminal();
    }

    public void run() {
        try {
            //tiempo aleatorio para que no entren todos los pasajeros cuando abre el aeropuerto
            Thread.sleep(random.nextInt(1000, 30000));

            
            // Después de llegar a la terminal
            if (tieneTiempoAntesDeEmbarque()) {
                try {
                    terminalAsignada.getFreeShop().ingresar(nombre);

                    // Simula que el pasajero mira siempre y a veces compra
                    terminalAsignada.getFreeShop().mirarProductos(nombre);
                    if (Math.random() > 0.5) { // 50% probabilidad de comprar
                        terminalAsignada.getFreeShop().comprar(nombre);
                    }
                    terminalAsignada.getFreeShop().salir(nombre);
                } catch (InterruptedException e) {
                    System.out.println(nombre + " no pudo entrar al FreeShop");
                }
            } else {
                // Se sienta directamente en la sala de embarque
                terminalAsignada.esperarEnSala(nombre);
            }
        } catch (Exception e) {
        }
    }

    
    private boolean tieneTiempoAntesDeEmbarque() {
        // 70% de chance de tener tiempo
        return Math.random() < 0.7;
    }

    public String getNombre() {
        return nombre;
    }

    public Terminal getTerminalAsignada(){
        return terminalAsignada;
    }

}
