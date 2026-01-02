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
            Thread.sleep(random.nextInt(900) + 1000);

            aeropuerto.entrarAeropuerto(nombre);
            // Simula el tiempo que tarda el pasajero en llegar al puesto de informes luego de entrar al aeropuerto
            Thread.sleep(random.nextInt(2501) + 500);

            String aerolinea = vuelo.getAerolinea();
            PuestoAtencion puestoAtencion = aeropuerto.derivarAPuestoAtencion(nombre, aerolinea);

            // Simula el tiempo que tarda el pasajero en llegar al puesto de atencion luego de las indicaciones
            Thread.sleep(random.nextInt(2501) + 500);
            puestoAtencion.ingresarPuestoAtencion(nombre);
            puestoAtencion.realizarCheckIn(nombre);
            // Simula el tiempo que tarda el pasajero en realizar el check-in de su vuelo
            Thread.sleep(random.nextInt(3001) + 2000);
            int puestoEmbarque = puestoAtencion.salirPuestoAtencion(nombre, vuelo);


            Tren tren = aeropuerto.obtenerTren();
            // Simula el tiempo que tarda el pasajero en llegar al tren luego de salir del puesto de atencion
            Thread.sleep(random.nextInt(2501) + 500);
            tren.subirTren(nombre, terminalAsignada.getNombre());
            tren.bajarTren(nombre, terminalAsignada.getNombre());

            
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
            } 
                // Se sienta directamente en la sala de embarque
                tiempo.esperarEnSala(nombre, vuelo.getHoraSalida(), vuelo);

                vuelo.embarcarEsperarDespegue(nombre, puestoEmbarque);
            
        } catch (Exception e) {
        }
    }

    
    private boolean tieneTiempoAntesDeEmbarque() {
        // 50% de chance de tener tiempo
        return Math.random() < 0.5;
    }

    public String getNombre() {
        return nombre;
    }

    public Terminal getTerminalAsignada(){
        return terminalAsignada;
    }

}
