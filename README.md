# knn
Libreria JavaFX para implementar el algoritmo de K-Vecinos proximos (Intelincia Artificial)

### Libreria de comunicación del Billetero BV20 en Java
![BV20](http://innovative-technology.com/images/stories/demo/portfolio/bv20-profile.jpg)
# Requerimientos
1. Java 8 (JDK 8)

# Dependencias
1. [JSerial](https://mvnrepository.com/artifact/com.fazecast/jSerialComm/1.3.11): Conector de comunicación serial en Java.
2. [Apache Common](https://mvnrepository.com/artifact/commons-lang/commons-lang/2.6): Utilitarios de uso comun

# Arquitectura
La libreria se basa en la misma arquitectura de oyentes (listeners) de la libreria de puerto serie. Para utilizarla correctamente se debe implementar una interfaz de control y disponer de al menos un puerto serie disponible

# Ejemplo
A continuación se muestra una ejecución simple de la libreria
```java
    BV20 billetero = new BV20(new Manejador());
    billetero.abrirPuerto(0);
    if (billetero.puertoAbierto()) {
         billetero.estado(); //Consultar estado del billetero
         Thread.sleep(10000);
    }
    else{
        System.out.println("Puerto ocupado");
    }
```
Aqui vemos que el objeto principal de comunicación es BV20, el mismo solo se puede instanciar con un objeto Controlador, el mismo es una interfaz que controla todos los eventos recibidos del billetero, por ejemplo
```java
    public class Manejador implements Controlador{
        //Metodos de comunicacion
        ....
        @Override
        public void manejadorEstado(Estado e) {
            //Mostrar por pantalla todos los estados de los 16 canales 
            for (int i = 0; i < 16; i++) {
                System.out.print(e.getCanalInhibido(i) + " ");
            }
        }
    }
```
# Diagrama de secuencia (ejemplo)
La secuencia de operaciones se refleja perfectamente en el siguiente diagrama
![Secuencia de flujo de estado](https://i.imgur.com/QaysMpZ.png)

# Métodos de BV20
| Método | Descripción | Código |
|:-------------|:--------------------|:------------|
|configurarPuerto(int baudios, int dataBits, int stopBits, int paridad) | Configura el puerto serie | No tiene |
|abrirPuerto(int n) | Abre el puerto de indice "n" en la lista de puertos disponibles, lo configura previamente | No tiene |
|puertoAbierto() | Verifica si el puerto esta abierto, responde true si es asi | No tiene |
|cerrarPuerto() | Cierra el puerto, si se encuentra abierto | No tiene |
|estado() | Solicita el estado del billetero | 182 |
|inhibirCanal(int n) | Inhabilita el canal "n" con n de 1 a 16 | 130 + n |
|deshinhibirCanal(int n) | Deshinhabilita el canal "n" con n de 1 a 16 | 150 + n |
|habilitarDeposito() | Habilita el deposito de dinero | 170 |
|deshabilitarDeposito() | Habilita el deposito de dinero | 171 |
|aceptarDeposito() | Habilita al billetero que acepte el billete siguiente | 172 |
|rechazarDeposito() | Rechaza el deposito de billetes | 173 |
|habilitarTodo() | Habilita todas las entradas, deposito y canales | 184 |
|deshabilitarTodo() | Deshabilita todas las entradas, deposito y canales | 185 |
|deshabilitarTiempoDeEsperaDelDeposito() | Deshabilita el tiempo de espera para el ingreso de billetes al deposito | 190 |
|habilitarTiempoDeEsperaDelDeposito() | Habilita el tiempo de espera para el ingreso de billetes al deposito | 191 |
|enviarComando(byte comando) | Envia un comando al billetero | No tiene |
| firmware() | Solicita el numero de version de firmware | 192 |
| dataSet() | Solicita el juego de caracteres del firmware | 193 |

# Controlador (Interfaz)
Es obligatorio implementar esta interfaz para el correcto uso del BV20. A continuación vemos todos los métodos que se deben sobreescribir
```java
public interface Controlador {
    
    public void canalInhibido(int numeroDeCanal);
    
    public void canalDesinhibido(int numeroDeCanal);
    
    public void billeteNoReconocido();
    public void billeteroFuncionandoLento();
    
    public void validadorOcupado();
    public void validadorDisponible();

    public void billeteRechazadoFalso();

    public void billeteroLlenoOatascado();

    public void operacionAbortadaDuranteIngreso();

    public void billeteroReiniciado();

    public void depositoHabilitado();

    public void ingresoDeshabilitado();

    public void depositoDeshabilitado();

    public void todosLosCanalesHabilitados();

    public void errorDeComando();

    public void aceptadoEnCanal(int numeroDeCanal);

    public void manejadorPorDefectoPaquetesLongitud2(byte[] datos);

    public void manejadorPorDefectoPaquetesLongitud1(byte[] datos);

    public void manejadorEstado(Estado e);
}
```

# Estructura interna
El Billetero BV20 posee una base de datos en la cual almacena la siguiente información
![Arquitectura](https://i.imgur.com/6SyKjwQ.png)
Cada canal representa un tipo de billete pre-programado en el firmware del Billetero, los registros de estado posibilitan el control del billetero en cualquier momento

# Mensajes (Host a Billetero)
|Código | Descripción | Billetero responde |
|:-----------|:--------------------|:------------------------------|
| 131 | Inhibir CANAL 1 | 131 (CANAL 1 inhibido) |
| 132 | Inhibir CANAL 2 | 132 (CANAL 2 inhibido) |
| 133 | Inhibir CANAL 3 | 133 (CANAL 3 inhibido) |
| 134 | Inhibir CANAL 4 | 134 (CANAL 4 inhibido) |
| 135 | Inhibir CANAL 5 | 135 (CANAL 5 inhibido) |
| 136 | Inhibir CANAL 6 | 136 (CANAL 6 inhibido) |
| 137 | Inhibir CANAL 7 | 137 (CANAL 7 inhibido) |
| 138 | Inhibir CANAL 8 | 138 (CANAL 8 inhibido) |
| 139 | Inhibir CANAL 9 | 139 (CANAL 9 inhibido) |
| 140 | Inhibir CANAL 10 | 140 (CANAL 10 inhibido) |
| 141 | Inhibir CANAL 11 | 141 (CANAL 11 inhibido) |
| 142 | Inhibir CANAL 12 | 142 (CANAL 12 inhibido) |
| 143 | Inhibir CANAL 13 | 143 (CANAL 13 inhibido) |
| 144 | Inhibir CANAL 14 | 144 (CANAL 14 inhibido) |
| 145 | Inhibir CANAL 15 | 145 (CANAL 15 inhibido) |
| 146 | Inhibir CANAL 16 | 146 (CANAL 16 inhibido)  |
| 170 | Habilitar deposito en modo serial | 170 (Deposito en modo serial habilitado) |
| 171 | Deshabilitar deposito en modo serial | 171 (Deposito en modo serial deshabilitado) |
| 172 | Acceptar deposito | 172 (Deposito aceptado) |
| 173 | Rechazar deposito | 173 (Deposito rechazado) |
| 182 | Estado | 182 (estado) - 1 byte = Canales inhibidos del 1 al 8 - 1 byte = Canales inhibidos del 9 al 16 - 1 byte = Deposito Habilitado = 1/Deshabilitado = 0 |
| 184 | Habilitar todo | 184 (Todo fue habilitado) |
| 185 | Deshabilitar todo | 185 (Todo fue deshabilitado) |
| 190 | Habilitar tiempo de espera para deposito | 190 (Tiempo de espera habilitado) |
| 191 | Deshabilitar tiempo de espera para deposito | 191 (Tiempo de espera deshabilitado) |
