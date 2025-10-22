import java.util.concurrent.Semaphore;

public class FreeShop {
    private char terminal;
    private Semaphore capacidad;   
    private Semaphore cajas;       

    public FreeShop(char terminal, int capacidadMaxima) {
        this.terminal = terminal;
        this.capacidad = new Semaphore(capacidadMaxima); 
        this.cajas = new Semaphore(2); // 2 cajas disponibles
    }

    public void ingresar(String pasajero) throws InterruptedException {
        capacidad.acquire(); // espera si está lleno
        System.out.println(pasajero + " ingresó al FreeShop de la Terminal " + terminal);
    }

    public void mirarProductos(String pasajero) throws InterruptedException {
        System.out.println(pasajero + " está mirando productos en FreeShop de la Terminal " + terminal);
        Thread.sleep(500); // simula tiempo de paseo
    }

    public void comprar(String pasajero) throws InterruptedException {
        cajas.acquire(); 
        System.out.println(pasajero + " está comprando en caja del FreeShop de la Terminal " + terminal);
        Thread.sleep(500); // simula tiempo de pago
        System.out.println(pasajero + " terminó de pagar en caja del FreeShop de la Terminal " + terminal);
        cajas.release();
    }

    public void salir(String pasajero) {
        System.out.println(pasajero + " salió del FreeShop de la Terminal " + terminal);
        capacidad.release(); 
    }
}
