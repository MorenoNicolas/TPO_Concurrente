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
            // Tiempo aleatorio para llegada escalonada
            Thread.sleep(random.nextInt(1500));

            aeropuerto.entrarAeropuerto(nombre);
            Thread.sleep(random.nextInt(1000));

            String aerolinea = vuelo.getAerolinea();
            PuestoAtencion puestoAtencion = aeropuerto.derivarAPuestoAtencion(nombre, aerolinea);

            Thread.sleep(random.nextInt(1000));
            puestoAtencion.ingresarPuestoAtencion(nombre);
            puestoAtencion.realizarCheckIn(nombre);
            
            Thread.sleep(random.nextInt(1000));
            int puestoEmbarque = puestoAtencion.salirPuestoAtencion(nombre, vuelo);

            // === LÓGICA DEL TREN ===
            Tren tren = aeropuerto.obtenerTren();
            
            // Asumimos que el nombre de la terminal es "A", "B" o "C". Obtenemos el char.
            char charTerminal = terminalAsignada.getNombre();
            
            // Intenta subir. Si el tren no está, se bloqueará aquí hasta el próximo ciclo.
            tren.subirTren(nombre, charTerminal);
            
            // Se bloquea hasta que el tren llegue a su terminal específica
            tren.bajarTren(nombre, charTerminal);
            // =======================

            
            // Después de llegar a la terminal (Lógica de FreeShop)
            if (tieneTiempoAntesDeEmbarque()) {
                try {
                    terminalAsignada.getFreeShop().ingresar(nombre);
                    terminalAsignada.getFreeShop().mirarProductos(nombre);
                    if (Math.random() > 0.5) { 
                        terminalAsignada.getFreeShop().comprar(nombre);
                    }
                    terminalAsignada.getFreeShop().salir(nombre);
                } catch (InterruptedException e) {
                    System.out.println(nombre + " no pudo entrar al FreeShop");
                }
            } 
            
            // Se sienta en la sala de embarque
            tiempo.esperarEnSala(nombre, vuelo.getHoraSalida(), vuelo);
            vuelo.embarcarEsperarDespegue(nombre, puestoEmbarque);
            
        } catch (Exception e) {
            System.out.println("Error en pasajero " + nombre + ": " + e.getMessage());
        }
    }

    private boolean tieneTiempoAntesDeEmbarque() {
        return Math.random() < 0.5;
    }

    public String getNombre() {
        return nombre;
    }

    public Terminal getTerminalAsignada(){
        return terminalAsignada;
    }
}