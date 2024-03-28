import gridsim.Gridlet;

public class MatriceTask extends Gridlet {
    private int[][] matrice1;
    private int[][] matrice2;
    private int[] resultat;
    private boolean horizontal;

    public MatriceTask(String name, int length, int width, int[][] matrice1, int[][] matrice2, boolean horizontal) {
        super(name, length, width);
        this.matrice1 = matrice1;
        this.matrice2 = matrice2;
        this.horizontal = horizontal;
    }

    public void calculate() {
        int m = matrice1.length;
        int n = matrice2[0].length;
        int p = matrice2.length;
        resultat = new int[m * n];

        if (horizontal) {
            // Calcul horizontal
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < p; k++) {
                        resultat[i * n + j] += matrice1[i][k] * matrice2[k][j];
                    }
                }
            }
        } else {
            // Calcul vertical
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < m; i++) {
                    for (int k = 0; k < p; k++) {
                        resultat[j * m + i] += matrice1[i][k] * matrice2[k][j];
                    }
                }
            }
        }
    }

    public int[] getResultat() {
        return resultat;
    }
}
