
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PuestoAtencion {
    private String aerolinea;
    private int capacidad;                         // capacidad máxima
    private int ocupacion;                         // ocupación actual
    private ConcurrentLinkedQueue<String> filaCheckIn; // fila de pasajeros en check-in
    private ConcurrentLinkedQueue<String> hall;        // cola de espera (hall central)
    private ReentrantLock accesoPuesto;
    private Condition esperaFila;
    private Condition esperaGuardia;

    public PuestoAtencion(String aerolinea, int cantidadMaxima) {
        this.aerolinea = aerolinea;
        this.capacidad = cantidadMaxima;
        this.ocupacion = 0;
        this.filaCheckIn = new ConcurrentLinkedQueue<>();
        this.hall = new ConcurrentLinkedQueue<>();
        this.accesoPuesto = new ReentrantLock(true);
        this.esperaFila = accesoPuesto.newCondition();
        this.esperaGuardia = accesoPuesto.newCondition();
    }

    // Método para Pasajero
    public void ingresarPuestoAtencion(String pasajero) throws InterruptedException {
        accesoPuesto.lock();
        try {
            if (ocupacion == capacidad) {
                // puesto lleno → se queda en el hall
                hall.add(pasajero);
                System.out.println(pasajero + " espera en el hall para " + aerolinea);
                esperaGuardia.signal(); // avisa al guardia
            } else {
                // entra directo a la fila de check-in
                System.out.println(pasajero + " ingresó a la fila del Puesto de Atención de " + aerolinea);
                filaCheckIn.add(pasajero);
                ocupacion++;
            }
        } finally {
            accesoPuesto.unlock();
        }
    }

    // Método para Pasajero
    public void realizarCheckIn(String pasajero) throws InterruptedException {
        accesoPuesto.lock();
        try {
            while (!pasajero.equals(filaCheckIn.peek())) {
                esperaFila.await();
            }
            System.out.println(pasajero + " está realizando check-in en " + aerolinea);
            Thread.sleep(500); // simula tiempo de atención
        } finally {
            accesoPuesto.unlock();
        }
    }

    // Método para Pasajero
    public int salirPuestoAtencion(String pasajero, Vuelo vuelo) {
        accesoPuesto.lock();
        try {
            int puestoAsignado = vuelo.asignarPuestoEmbarque();
            System.out.println(pasajero + " realizó el check-in del Vuelo de " + aerolinea);
            System.out.println(pasajero + " obtuvo el puesto de embarque: " + puestoAsignado);
            filaCheckIn.poll(); // lo saco de la fila
            ocupacion--;
            esperaFila.signalAll();   // despierta al siguiente de la fila
            esperaGuardia.signalAll();// avisa al guardia
            return puestoAsignado;
        } finally {
            accesoPuesto.unlock();
        }
    }

    // Método para Guardia
    public void darPasoAPasajero() throws InterruptedException {
        accesoPuesto.lock();
        try {
            // espera a que haya pasajeros en el hall
            while (hall.isEmpty()) {
                esperaGuardia.await();
            }
            // espera lugar en el puesto
            while (ocupacion >= capacidad) {
                esperaGuardia.await();
            }
            // mueve un pasajero del hall a la fila
            String pasajero = hall.poll();
            System.out.println("Guardia deja pasar a " + pasajero + " al Puesto de Atención de " + aerolinea);
            filaCheckIn.add(pasajero);
            ocupacion++;
            esperaFila.signalAll();
        } finally {
            accesoPuesto.unlock();
        }
    }

    public String getAerolinea() {
        return aerolinea;
    }
}
