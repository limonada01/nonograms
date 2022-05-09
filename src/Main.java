import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    // [[1,1,1,1,0,0],[1,1,1,1,1,0],[0,1,0,0,1,1],[0,1,0,0,1,1],[0,1,0,0,1,1],[0,1,0,0,1,1],[1,1,1,1,1,0],[1,1,1,1,0,0]]
    static int[][] solucion = new int[][] {
            new int[] { 1, 1, 1, 1, 0, 0 },
            new int[] { 1, 1, 1, 1, 1, 0 },
            new int[] { 0, 1, 0, 0, 1, 1 },
            new int[] { 0, 1, 0, 0, 1, 1 },
            new int[] { 0, 1, 0, 0, 1, 1 },
            new int[] { 0, 1, 0, 0, 1, 1 },
            new int[] { 1, 1, 1, 1, 1, 0 },
            new int[] { 1, 1, 1, 1, 0, 0 },
    };
    // [[4],[5],[1,2],[1,2],[1,2],[1,2],[5],[4]]
    static int[][] rowConstraint = new int[][] {
            new int[] { 4 },
            new int[] { 5 },
            new int[] { 1, 2 },
            new int[] { 1, 2 },
            new int[] { 1, 2 },
            new int[] { 1, 2 },
            new int[] { 5 },
            new int[] { 4 },
    };
    // [[2,2],[8],[2,2],[2,2],[6],[4]]
    static int[][] colConstraint = new int[][] {
            new int[] { 2, 2 },
            new int[] { 8 },
            new int[] { 2, 2 },
            new int[] { 2, 2 },
            new int[] { 6 },
            new int[] { 4 },
    };

    public static boolean check(int[][] rowConstraint, int[][] colConstraint, int[][] solucion) {
        boolean resultado = false;
        //verifico que existan restricciones para cada fila y para cada columna, sin que sobren o falten
        //verifico las restricciones de las filas, luego las restricciones de las columnas usando la matriz transpuesta
        if ( rowConstraint.length==solucion.length && colConstraint.length==solucion[0].length && checkRow(rowConstraint, solucion)  && checkRow(colConstraint, matrizTranspuesta(solucion)) ) { 
            resultado = true;
        }
        return resultado;
    }

    public static boolean checkRow(int[][] rowConstraint, int[][] solucion) {
        boolean resultado = true;
        int cantRows = solucion.length;
        int i=0;
        while(resultado && i<cantRows){//chequeo todas las filas
            resultado=checkSecuenciasDeUnos(rowConstraint[i], solucion[i]);
            i++;
        }
        return resultado;
    }

    public static boolean checkSecuenciasDeUnos(int[] rowConstraint, int[] rowSolucion) {
        int cantSecuenciasEsperadas = rowConstraint.length; // restricciones [2,3] --> se esperan 2 secuencias de unos
                                                            // ej: [0,1,1,0,1,1,1]
        int i = 0;
        int longitudFilaSolucion = rowSolucion.length;
        int cantSecuencias = 0;// variable para contar las secuencias encontradas para luego comparar con las
                               // esperadas
        boolean leyendoSecuencia = false;
        LinkedList<Integer> longitudDeLasSecuencias = new LinkedList<Integer>();
        int aux = 0;
        while (i < longitudFilaSolucion) {
            if (rowSolucion[i] == 1) {
                aux++;// cuento el tamaño de la secuencia
                if (!leyendoSecuencia) {// si viene 1, y no estoy leyendo, significa que comienza una nueva secuencia de
                                        // unos. debo comenzar a leer la secuencia
                    leyendoSecuencia = true;
                } /*else {//si venia leyendo una secuencia
                    if (i == longitudFilaSolucion - 1) {
                        // CASO ESPECIAL
                        // si venia leyendo una secuencia y acabo de leer el ultimo elemento de la fila
                        // solucion
                        // cuando la fila solucion termina con una secuencia de unos
                        cantSecuencias++; // contabilizo esta ultima secuencia
                        leyendoSecuencia = false; // dejo de leer
                        longitudDeLasSecuencias.push(aux);// añado el tamaño de la secuencia leida a la lista para luego
                                                         // verificar con las restricciones
                        aux = 0;// reinicio el contador de tamaño de una secuencia
                    } 
                }*/
                if (i == longitudFilaSolucion - 1) {
                    // CASO ESPECIAL
                    // venia leyendo una secuencia y acabo de leer el ultimo elemento de la fila solucion
                    // cuando la fila solucion termina con una secuencia de unos
                    // o comienzo a leer una nueva secuencia pero resulta que es el ultimo 1 de la fila solucion (secuencia de longitud 1)
                    cantSecuencias++; // contabilizo esta ultima secuencia 
                    longitudDeLasSecuencias.push(aux);// añado el tamaño de la secuencia leida a la lista para luego
                                                     // verificar con las restricciones    
                }
            } else {// rowSolucion[i]==0
                if (leyendoSecuencia) {// si venia leyendo una secuencia y viene 0, debo dejar de leer porq ya se acabo
                                       // la secuencia de unos.
                    cantSecuencias++;// contabilizo la secuencia recien leida
                    leyendoSecuencia = false;// dejo de leer hasta que aparezca una nueva secuencia de unos
                    longitudDeLasSecuencias.push(aux);// añado el tamaño de la secuencia leida a la lista para luego
                                                     // verificar con las restricciones
                    aux = 0;// reinicio el contador de tamaño de una secuencia
                } // si no tengo que leer unos y viene 0, no hago nada
            }
            i++;
        }
        //System.out.println("Cant secuencias: "+cantSecuencias);
        return cantSecuencias == cantSecuenciasEsperadas
                && checkLongitudSecuencias(longitudDeLasSecuencias, rowConstraint);
    }

    public static boolean checkLongitudSecuencias(LinkedList secuenciasLeidas, int[] rowConstraint) {
        boolean resultado=true;
        if (rowConstraint.length == secuenciasLeidas.size()) {
            int i = rowConstraint.length - 1;
            while (resultado && i >= 0 ) {
                if(!secuenciasLeidas.getFirst().equals(rowConstraint[i])){
                    resultado=false;
                }
                secuenciasLeidas.pop();//quito el elemento analizado
                i--;
            }
        }else{
            resultado=false;
        }
        return resultado;
    }

    public static int[][] matrizTranspuesta(int[][] matriz){
        int row=matriz.length;
        int column=matriz[0].length;
        int[][] transpose = new int[column][row];
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                transpose[j][i] = matriz[i][j];
            }
        }
        return transpose;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(check(rowConstraint, colConstraint, solucion));
    }
}
