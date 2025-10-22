
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Tren {
    private int capacidad;
    private BlockingQueue<Pasajero> pasajeros; // pasajeros a bordo
    private boolean enViaje = false;

    public Tren(int capacidad) {
        this.capacidad = capacidad;
        this.pasajeros = new LinkedBlockingQueue<>(capacidad);
    }

    // Pasajero sube al tren
    public synchronized void subirTren(Pasajero p) throws InterruptedException {
        while (enViaje || pasajeros.size() == capacidad) {
            wait(); // espera si el tren está viajando o lleno
        }
        pasajeros.put(p);
        System.out.println(p.getNombre() + " subió al tren.");
        if (pasajeros.size() == capacidad) {
            notifyAll(); // avisa al control que está lleno y inicia recorrido
        }
    }

    // ControlTren espera hasta que el tren esté lleno para arrancar
    public synchronized void esperarLleno() throws InterruptedException {
        while (pasajeros.size() < capacidad) {
            wait();
        }
        enViaje = true;
    }

    // Pasajeros que deben bajar en esta terminal
    public synchronized void bajarTren(Terminal terminal) {
        pasajeros.removeIf(p -> {
            if (p.getTerminalAsignada() == terminal) {
                System.out.println(p.getNombre() + " bajó en Terminal " + terminal.getNombre());
                terminal.esperarEnSala(p.getNombre()); // lo manda a la sala de embarque
                return true;
            }
            return false;
        });
    }

    // Al terminar recorrido
    public synchronized void finalizarRecorrido() {
        enViaje = false;
        notifyAll(); // permite que nuevos pasajeros suban
    }

    public boolean estaVacio() {
        return pasajeros.isEmpty();
    }
}
