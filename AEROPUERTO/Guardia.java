
public class Guardia extends Thread {

    private PuestoAtencion puesto;
    private boolean activo = true;

    public Guardia(PuestoAtencion puesto) {
        this.puesto = puesto;
    }

    @Override
    public void run() {
        try {
            while (activo) {
                puesto.darPasoAPasajero();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Guardia del puesto " + puesto.getAerolinea() + " interrumpido.");
        }
    }
}
