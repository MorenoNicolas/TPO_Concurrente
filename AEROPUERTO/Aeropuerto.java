public class Aeropuerto {
    private PuestoAtencion[] listaPuestos;
    private Tren transporte;
    private boolean abierto;

    public Aeropuerto(PuestoAtencion[] puestos, Tren tren) {
        this.listaPuestos = puestos;
        this.transporte = tren;
        this.abierto = false;
    }

    // Pasajero intenta entrar al aeropuerto
    public synchronized void entrarAeropuerto(String pasajero) {
        try {
            while (!abierto) {
                wait(); // espera a que abra
            }
            System.out.println(pasajero + " ingresó al aeropuerto.");
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + pasajero + " interrumpido al intentar entrar.");
        }
    }

    // Pasajero consulta informes y obtiene su puesto de atención
    public synchronized PuestoAtencion derivarAPuestoAtencion(String pasajero, String aerolinea) {
        PuestoAtencion destino = null;
        for (PuestoAtencion p : listaPuestos) {
            if (p.getAerolinea().equals(aerolinea)) {
                destino = p;
                break;
            }
        }
        System.out.println(pasajero + " fue derivado al puesto de atención de " + aerolinea);
        return destino;
    }

    // Abrir aeropuerto (llamado desde ControlTiempo)
    public synchronized void iniciarJornada() {
        abierto = true;
        System.out.println("Son las 6:00 → El aeropuerto abrió sus puertas.\n");
        notifyAll();
    }

    // Cerrar aeropuerto (llamado desde reloj)
    public synchronized void finalizarJornada() {
        abierto = false;
        System.out.println("Son las 22:00 → El aeropuerto cerró su atención.\n");
        notifyAll();
    }

    // Getter del transporte
    public Tren obtenerTren() {
        return transporte;
    }
}
