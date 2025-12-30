import java.util.Arrays;
import java.util.Random;
public class Main {

    private static final int NUM_TERMINALES = 3;
    private static final int PUESTOS_POR_TERMINAL = 6;
    private static final int CAP_TREN = 5;
    private static final int CAP_FREESHOP = 8;
    private static final int TOTAL_PASAJEROS = 10; 
    private static final String[] NOMBRES_AEROLINEAS = {
        "LATAM", "Iberia", "Lufthansa", "United Airlines"
    };

    private static Terminal[] terminales;
    private static PuestoAtencion[] puestos;
    private static Vuelo[] vuelos;
    private static Random rng = new Random();

    public static void main(String[] args) {

        // Inicializar componentes del aeropuerto
        terminales = setupTerminales();
        puestos = setupPuestosAtencion();
        vuelos = setupVuelos();

        // Crear transporte y aeropuerto
        Tren tren = new Tren(CAP_TREN);
        Aeropuerto aeropuerto = new Aeropuerto(puestos, tren);

        // Tiempo y reloj
        Tiempo relojInterno = new Tiempo();
        ControlTiempo controlHorario = new ControlTiempo( relojInterno, aeropuerto);

        // Guardias
        for (PuestoAtencion pa : puestos) {
            Guardia g = new Guardia(pa);
            g.start();
        }

        // Chofer del tren
        ControlTren conductor = new ControlTren(tren, terminales);

        // Lanzar reloj y tren
        aeropuerto.iniciarJornada();
        controlHorario.start();
        conductor.start();

        // Crear y lanzar pasajeros
        generarPasajeros(aeropuerto, relojInterno);

    }

    private static Terminal[] setupTerminales() {
        Terminal[] array = new Terminal[NUM_TERMINALES];
        char letra = 'A';
        int contadorPuestos = 1, inicioPuesto=1, finPuesto=7;

        for (int i = 0; i < NUM_TERMINALES; i++) {
            int[] puestosEmb = new int[PUESTOS_POR_TERMINAL];
            for (int j = 0; j < PUESTOS_POR_TERMINAL; j++) {
                puestosEmb[j] = contadorPuestos++;
            }
            FreeShop free = new FreeShop(letra , CAP_FREESHOP);
            array[i] = new Terminal(letra, inicioPuesto, finPuesto);
            System.out.println("Terminal " + letra + " creada con puestos: " + Arrays.toString(puestosEmb));
            letra++;
            inicioPuesto += 7;
            finPuesto +=7;
        }
        System.out.println("-------------------------------------------------");
        return array;
    }

    private static PuestoAtencion[] setupPuestosAtencion() {
        PuestoAtencion[] array = new PuestoAtencion[NOMBRES_AEROLINEAS.length];
        int maxCapacidad = 3;
        for (int i = 0; i < NOMBRES_AEROLINEAS.length; i++) {
            array[i] = new PuestoAtencion(NOMBRES_AEROLINEAS[i], maxCapacidad);
            System.out.println("Puesto de atención creado para " + NOMBRES_AEROLINEAS[i]);
        }
        System.out.println("-------------------------------------------------");
        return array;
    }

    private static Vuelo[] setupVuelos() {
        Vuelo[] array = new Vuelo[NOMBRES_AEROLINEAS.length];
        for (int i = 0; i < NOMBRES_AEROLINEAS.length; i++) {
            Terminal t = terminales[rng.nextInt(terminales.length)];
            int[] puestosEmb = new int[7];
            int hora = rng.nextInt(17) + 6;
            array[i] = new Vuelo(NOMBRES_AEROLINEAS[i], t, puestosEmb, hora);
            System.out.println("Vuelo " + (i + 1) + ": " + NOMBRES_AEROLINEAS[i]);
            System.out.println(" Terminal: " + t.getNombre());
            System.out.println(" Puestos disponibles: " + Arrays.toString(puestosEmb));
            System.out.println(" Hora salida: " + hora + ":00");
            System.out.println("-------------------------------------------------");
        }
        return array;
    }

    private static void generarPasajeros(Aeropuerto aeropuerto, Tiempo tiempo) {
        // Lista temporal para guardar los hilos antes de arrancarlos
        Pasajero[] pasajerosThreads = new Pasajero[TOTAL_PASAJEROS];

        // 1. Crear pasajeros y registrar reservas
        for (int i = 0; i < TOTAL_PASAJEROS; i++) {
            Vuelo v = vuelos[rng.nextInt(vuelos.length)];
            v.registrarReserva();
            pasajerosThreads[i] = new Pasajero("Pasajero-" + (i+1), aeropuerto, v, tiempo);
        }

        // 2. Inicializar el Latch en los vuelos (AHORA es seguro porque ya están todos contados)
        for (Vuelo v : vuelos) {
            v.inicializarCountDownLatch();
        }
        System.out.println("Todos los CountDownLatch inicializados.");

        // 3. Iniciar los hilos de los pasajeros
        for (Pasajero p : pasajerosThreads) {
            p.start();
        }
        System.out.println("-------------------------------------------------");
    }
}