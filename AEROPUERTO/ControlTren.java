public class ControlTren extends Thread {

    private Tren tren;
    private char[] terminales = {'A', 'B', 'C'};

    public ControlTren(Tren tren) {
        this.tren = tren;
    }

    @Override
    public void run() {

        while (true) {

            tren.habilitarAcceso();      // Permite subir pasajeros
            tren.iniciarRecorrido();     // Espera el tren 

            for (char t : terminales) {
                try {
                    Thread.sleep(1000);  // Simula el viaje
                } catch (InterruptedException e) {
                }
                tren.viajarATerminal(t);
            }

            tren.finalizarRecorrido();
        }
    }
}
