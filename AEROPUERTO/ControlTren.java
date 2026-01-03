public class ControlTren extends Thread {

    private Tren tren;
    private char[] terminales = {'A', 'B', 'C'};

    public ControlTren(Tren tren) {
        this.tren = tren;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1. Llegada al inicio y carga de pasajeros
                tren.habilitarAcceso();      
                
                // Tiempo de espera para que los pasajeros suban
                Thread.sleep(3000); 

                // 2. Cierre y salida
                tren.cerrarPuertas();
                tren.iniciarRecorrido();    

                // 3. Recorrido por las terminales
                for (char t : terminales) {
                    // Simula el viaje entre estaciones
                    Thread.sleep(1500);  
                    
                    // Llega a la terminal y avisa a pasajeros
                    tren.viajarATerminal(t);
                    
                    // Tiempo de espera en la estación para que bajen
                    Thread.sleep(1000); 
                }

                // 4. Fin del ciclo
                tren.finalizarRecorrido();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}