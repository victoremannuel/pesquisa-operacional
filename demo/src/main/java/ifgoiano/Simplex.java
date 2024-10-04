package ifgoiano;

import java.util.Scanner;

public class Simplex {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Perguntar o número de variáveis e restrições
        System.out.println("Informe o número de variáveis:");
        int numVariaveis = scanner.nextInt();
        System.out.println("Informe o número de restrições:");
        int numRestricoes = scanner.nextInt();

        // Receber coeficientes da função objetivo
        System.out.println("Informe os coeficientes da função objetivo (ex: 3 2):");
        double[] funcObj = new double[numVariaveis];
        for (int i = 0; i < numVariaveis; i++) {
            funcObj[i] = scanner.nextDouble();
        }

        // Receber coeficientes das restrições
        System.out.println("Informe os coeficientes das restrições:");
        double[][] restricoes = new double[numRestricoes][numVariaveis];
        double[] limites = new double[numRestricoes]; // Limites de cada restrição
        for (int i = 0; i < numRestricoes; i++) {
            System.out.println("Informe os coeficientes da restrição " + (i + 1) + ":");
            for (int j = 0; j < numVariaveis; j++) {
                restricoes[i][j] = scanner.nextDouble();
            }
            System.out.println("Informe o limite da restrição " + (i + 1) + ":");
            limites[i] = scanner.nextDouble();
        }

        // Criar matriz do simplex com variáveis de folga
        int totalColunas = numVariaveis + numRestricoes + 1; // Variáveis + folgas + resultado
        double[][] simplex = new double[numRestricoes + 1][totalColunas]; // Inclui linha da função objetivo
        
        // Preencher a matriz com as restrições
        for (int i = 0; i < numRestricoes; i++) {
            for (int j = 0; j < numVariaveis; j++) {
                simplex[i][j] = restricoes[i][j];  // Coeficientes das variáveis
            }
            simplex[i][numVariaveis + i] = 1;      // Variável de folga
            simplex[i][totalColunas - 1] = limites[i]; // Limite da restrição
        }

        // Preencher a função objetivo (negativos para maximização)
        for (int j = 0; j < numVariaveis; j++) {
            simplex[numRestricoes][j] = -funcObj[j];
        }

        // Aplicar o método simplex
        executarSimplex(simplex, numRestricoes, totalColunas);
        
        scanner.close();
    }

    // Método que executa o algoritmo simplex
    public static void executarSimplex(double[][] simplex, int numRestricoes, int totalColunas) {
        int linhaPivo, colunaPivo;

        while (true) {
            // Encontrar coluna pivô (menor valor na última linha, exceto o último elemento)
            colunaPivo = encontrarColunaPivo(simplex, totalColunas);
            if (colunaPivo == -1) {
                break;  // Se não há mais valores negativos na última linha, temos a solução ótima
            }

            // Encontrar linha pivô (razão mais baixa da divisão entre coluna de resultados e coluna pivô)
            linhaPivo = encontrarLinhaPivo(simplex, colunaPivo, numRestricoes);
            if (linhaPivo == -1) {
                System.out.println("Problema sem solução.");
                return;
            }

            // Atualizar a tabela para a nova solução
            atualizarTabela(simplex, linhaPivo, colunaPivo, numRestricoes, totalColunas);
        }

        // Exibir a solução
        System.out.println("Solução ótima encontrada:");
        for (int i = 0; i < numRestricoes; i++) {
            System.out.println("x" + (i + 1) + " = " + simplex[i][totalColunas - 1]);
        }
        System.out.println("Valor máximo de z = " + (-simplex[numRestricoes][totalColunas - 1]));
    }

    // Método para encontrar a coluna pivô (menor valor na última linha)
    public static int encontrarColunaPivo(double[][] simplex, int totalColunas) {
        int colunaPivo = -1;
        double menorValor = 0;

        for (int i = 0; i < totalColunas - 1; i++) {
            if (simplex[simplex.length - 1][i] < menorValor) {
                menorValor = simplex[simplex.length - 1][i];
                colunaPivo = i;
            }
        }

        return colunaPivo;
    }

    // Método para encontrar a linha pivô (razão mais baixa)
    public static int encontrarLinhaPivo(double[][] simplex, int colunaPivo, int numRestricoes) {
        int linhaPivo = -1;
        double menorRazao = Double.MAX_VALUE;

        for (int i = 0; i < numRestricoes; i++) {
            if (simplex[i][colunaPivo] > 0) {
                double razao = simplex[i][simplex[i].length - 1] / simplex[i][colunaPivo];
                if (razao < menorRazao) {
                    menorRazao = razao;
                    linhaPivo = i;
                }
            }
        }

        return linhaPivo;
    }

    // Atualizar a tabela após encontrar o pivô
    public static void atualizarTabela(double[][] simplex, int linhaPivo, int colunaPivo, int numRestricoes, int totalColunas) {
        double valorPivo = simplex[linhaPivo][colunaPivo];

        // Dividir a linha pivô pelo valor do pivô
        for (int j = 0; j < totalColunas; j++) {
            simplex[linhaPivo][j] /= valorPivo;
        }

        // Atualizar as outras linhas
        for (int i = 0; i <= numRestricoes; i++) {
            if (i != linhaPivo) {
                double fator = simplex[i][colunaPivo];
                for (int j = 0; j < totalColunas; j++) {
                    simplex[i][j] -= fator * simplex[linhaPivo][j];
                }
            }
        }
    }
}
