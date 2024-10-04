package ifgoiano;

import java.util.Scanner;

public class Simplex {
    
    // Método para pegar os dados do problema do usuário
    public static double[][] getTableau(int rows, int cols) {
        Scanner scanner = new Scanner(System.in);
        double[][] tableau = new double[rows][cols];

        System.out.println("Informe os coeficientes da tabela simplex, linha por linha.");
        for (int i = 0; i < rows; i++) {
            System.out.println("Linha " + (i + 1) + ": ");
            for (int j = 0; j < cols; j++) {
                tableau[i][j] = scanner.nextDouble();
            }
        }

        return tableau;
    }

    // Método para mostrar o tableau
    public static void printTableau(double[][] tableau) {
        System.out.println("Tabela Simplex atual:");
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.printf("%10.2f ", tableau[i][j]);
            }
            System.out.println();
        }
    }

    // Método para encontrar a coluna pivô (regra de maior coeficiente negativo)
    public static int findPivotColumn(double[] objectiveRow) {
        int pivotColumn = 0;
        for (int i = 1; i < objectiveRow.length - 1; i++) {  // Ignora a coluna do lado direito
            if (objectiveRow[i] < objectiveRow[pivotColumn]) {
                pivotColumn = i;
            }
        }
        return pivotColumn;
    }

    // Método para encontrar a linha pivô (razão mínima positiva)
    public static int findPivotRow(double[][] tableau, int pivotColumn) {
        int pivotRow = -1;
        double minRatio = Double.MAX_VALUE;
        
        for (int i = 1; i < tableau.length; i++) {
            double rhs = tableau[i][tableau[0].length - 1];
            double pivotElement = tableau[i][pivotColumn];
            if (pivotElement > 0) {
                double ratio = rhs / pivotElement;
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    // Método para realizar a eliminação gaussiana (redefinir linha e coluna pivô)
    public static void performPivot(double[][] tableau, int pivotRow, int pivotColumn) {
        double pivotElement = tableau[pivotRow][pivotColumn];

        // Dividir a linha pivô pelo elemento pivô
        for (int j = 0; j < tableau[pivotRow].length; j++) {
            tableau[pivotRow][j] /= pivotElement;
        }

        // Para cada linha, exceto a linha pivô, realizar a eliminação gaussiana
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotColumn];
                for (int j = 0; j < tableau[i].length; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }

    // Método principal para resolver o problema usando Simplex
    public static void simplex(double[][] tableau) {
        while (true) {
            printTableau(tableau);
            int pivotColumn = findPivotColumn(tableau[0]);
            
            if (tableau[0][pivotColumn] >= 0) {
                // Se não houver mais coeficientes negativos na linha do objetivo, terminamos
                break;
            }
            
            int pivotRow = findPivotRow(tableau, pivotColumn);
            
            if (pivotRow == -1) {
                System.out.println("Problema ilimitado.");
                return;
            }
            
            performPivot(tableau, pivotRow, pivotColumn);
        }
        
        System.out.println("Solução ótima encontrada:");
        printSolution(tableau);
    }

    // Método para imprimir a solução
    public static void printSolution(double[][] tableau) {
        for (int i = 1; i < tableau.length; i++) {
            boolean isBasic = false;
            for (int j = 1; j < tableau[0].length - 1; j++) {
                if (tableau[i][j] == 1) {
                    System.out.println("x" + j + " = " + tableau[i][tableau[0].length - 1]);
                    isBasic = true;
                    break;
                }
            }
            if (!isBasic) {
                System.out.println("Variável de folga na linha " + i + ": " + tableau[i][tableau[0].length - 1]);
            }
        }
        System.out.println("Valor ótimo de z = " + tableau[0][tableau[0].length - 1]);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pergunta ao usuário se é um problema de maximização ou minimização
        System.out.println("É um problema de maximização ou minimização? (+ para max, - para min)");
        char problemType = scanner.next().charAt(0);

        // Pegando as variáveis básicas e todas as variáveis do problema
        System.out.println("Informe as variáveis básicas separadas por espaço (ex: z f1 f2 f3):");
        scanner.nextLine();  // Limpa o buffer
        String basicVariables = scanner.nextLine();

        System.out.println("Informe todas as variáveis do problema separadas por espaço (ex: z x1 x2 f1 f2 f3):");
        String allVariables = scanner.nextLine();

        // Número de linhas e colunas da tabela simplex
        System.out.println("Quantas linhas tem a tabela?");
        int rows = scanner.nextInt();

        System.out.println("Quantas colunas tem a tabela?");
        int cols = scanner.nextInt();

        // Coletando o tableau simplex
        double[][] tableau = getTableau(rows, cols);

        // Resolva o problema usando o algoritmo Simplex
        simplex(tableau);
    }
}
