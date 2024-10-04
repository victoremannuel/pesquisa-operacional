package ifgoiano;

import java.util.Scanner;

public class SimplexAlgorithm {
    
    private static double[][] tableau;
    private static int numVariables;
    private static int numConstraints;

    // Entrada de dados
    public static void input() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe o número de variáveis: ");
        numVariables = scanner.nextInt();

        System.out.print("Informe o número de restrições: ");
        numConstraints = scanner.nextInt();

        // Inicializar a tabela com espaço para as variáveis de folga e a linha da função objetivo
        tableau = new double[numConstraints + 1][numVariables + numConstraints + 1];

        System.out.println("Informe os coeficientes da função objetivo (maximização):");
        for (int i = 0; i < numVariables; i++) {
            tableau[numConstraints][i] = -scanner.nextDouble();  // Coeficientes com sinal negativo
        }

        System.out.println("Informe os coeficientes das restrições e os valores de b:");
        for (int i = 0; i < numConstraints; i++) {
            System.out.println("Restrição " + (i + 1) + ":");
            for (int j = 0; j < numVariables; j++) {
                tableau[i][j] = scanner.nextDouble();
            }
            tableau[i][numVariables + i] = 1.0;  // Variáveis de folga
            tableau[i][numVariables + numConstraints] = scanner.nextDouble();  // Valores de b
        }
    }

    // Encontra a coluna pivô (variável que entra na base)
    private static int findPivotColumn() {
        int pivotCol = 0;
        for (int i = 1; i < tableau[0].length - 1; i++) {
            if (tableau[numConstraints][i] < tableau[numConstraints][pivotCol]) {
                pivotCol = i;
            }
        }
        return tableau[numConstraints][pivotCol] < 0 ? pivotCol : -1;
    }

    // Encontra a linha pivô (variável que sai da base)
    private static int findPivotRow(int pivotCol) {
        int pivotRow = -1;
        double minRatio = Double.MAX_VALUE;
        for (int i = 0; i < numConstraints; i++) {
            if (tableau[i][pivotCol] > 0) {
                double ratio = tableau[i][numVariables + numConstraints] / tableau[i][pivotCol];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    // Realiza a operação de pivotamento
    private static void pivot(int pivotRow, int pivotCol) {
        double pivotValue = tableau[pivotRow][pivotCol];

        // Divide a linha pivô pelo valor do pivô
        for (int j = 0; j < tableau[0].length; j++) {
            tableau[pivotRow][j] /= pivotValue;
        }

        // Atualiza as outras linhas
        for (int i = 0; i <= numConstraints; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < tableau[0].length; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }

    // Método Simplex
    public static void simplex() {
        while (true) {
            int pivotCol = findPivotColumn();
            if (pivotCol == -1) {
                break;  // Não há mais colunas negativas, solução ótima encontrada
            }

            int pivotRow = findPivotRow(pivotCol);
            if (pivotRow == -1) {
                System.out.println("Solução ilimitada.");
                return;
            }

            pivot(pivotRow, pivotCol);
        }

        System.out.println("Solução ótima encontrada:");
        for (int i = 0; i < numVariables; i++) {
            boolean isBasic = false;
            double value = 0;
            for (int j = 0; j < numConstraints; j++) {
                if (tableau[j][i] == 1) {
                    isBasic = true;
                    value = tableau[j][numVariables + numConstraints];
                    break;
                }
            }
            if (isBasic) {
                System.out.println("x" + (i + 1) + " = " + value);
            } else {
                System.out.println("x" + (i + 1) + " = 0");
            }
        }
        System.out.println("Valor ótimo: " + tableau[numConstraints][numVariables + numConstraints]);
    }

    public static void main(String[] args) {
        input();
        simplex();
    }
}
