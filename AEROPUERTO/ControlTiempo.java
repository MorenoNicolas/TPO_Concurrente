
public class ControlTiempo extends Thread {

    private Tiempo tiempo;
    private Aeropuerto aeropuerto;

    public ControlTiempo(Tiempo t, Aeropuerto aeropuerto) {
        this.tiempo = t;
        this.aeropuerto = aeropuerto;
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(150); // medio minuto
                tiempo.avanzarMinuto();

                if (!tiempo.estaAbierto()) {
                    aeropuerto.finalizarJornada();
                    // Simulo cierre de 22 a 6
                    Thread.sleep(10000);
                    tiempo.adelantarHorario();
                    // Luego, el Aeropuerto abre de nuevo
                    aeropuerto.iniciarJornada();
                }
            }
        } catch (Exception e) {
        }

    }
}
