import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

import static choco.Choco.eq;

public class Sudoku {
    public static void main(String[] args) {
        //Taille du problème
        int nbvar = 9;

        //set the Grid
        int[][] Grid= new int[][]{
                {5, 0, 3, 0, 0, 4, 2, 0, 0},
                {2, 7, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 2, 0, 4, 0, 0, 0, 0, 7},
                {4, 3, 0, 0, 8, 0, 0, 0, 2},
                {0, 5, 0, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 5, 9, 0, 0, 0, 4},
                {3, 0, 5, 0, 1, 0, 9, 0, 6}
        };

        //Declaration du modèle
        Model m = new CPModel();

        //Déclaration des variables et des domaines
        IntegerVariable[][] var = new IntegerVariable[nbvar][nbvar];

        for(int i=0; i<nbvar; i++){
            for (int j=0; j<nbvar; j++){
                var[i][j]= Choco.makeIntVar("N", 1, nbvar);
                if(Grid[i][j]!=0){
                    //donnes initiales
                    m.addConstraint(eq(var[i][j], Grid[i][j]));
                }
                m.addVariable(var[i][j]);
            }
        }

        //Déclaration des contraintes
        //C1 : contrainte ligne
        for (int i = 0; i < nbvar; i++) {
            for (int j = 0; j < nbvar; j++)
                for (int k = j; k < nbvar; k++)
                    if (k != j) m.addConstraint(Choco.neq(var[i][j], var[i][k]));
        }

        //C2 : contrainte colonne
        for (int j = 0; j < nbvar; j++) {
            for (int i = 0; i < nbvar; i++)
                for (int k = 0; k < nbvar; k++)
                    if (k != i) m.addConstraint(Choco.neq(var[i][j], var[k][j]));
        }

        //C3 : contrainte carrée
        int [] A = new int[]{0,3,6};
        int [] B = new int[]{0,3,6};

        for (int a:A) {
            for (int b:B
                 ) {
                for (int i = a; i < a + 3; i++)
                    for (int j = b; j < b + 3; j++)
                        for (int k = a; k < a + 3; k++)
                            for (int l = b; l < b + 3; l++)
                                if (k != i || l != j) m.addConstraint(Choco.neq(var[i][j], var[k][l]));
            }
        }

        //Declaration du solveur
        Solver s = new CPSolver();

        //lecture du modele par le solveur
        s.read(m);

        //recherche de la premier solution
        s.solve();

        //Affichage des resultats

        //to find out all the possible solutions
        /*int t = 1;
        do {
            System.out.println("Solution : " + t);
            printResult(s,var,nbvar);
            t++;
        } while (s.nextSolution());*/


        //print the first solution
        printResult(s,var,nbvar);
    }

    private static void printResult(Solver s, IntegerVariable[][] vars, int n) {
        int sqrtn=(int) Math.sqrt(n);
        for(int i = 0; i < n; i++) {
            if (i%sqrtn == 0) {
                String str="";
                for(int k=0;k<(2*n)+4;k++) str=str.concat("-");
                System.out.println(str);
            }
            for(int j = 0; j < n; j++) {
                if (j%sqrtn == 0) System.out.print("|");
                int val=s.getVar(vars[i][j]).getVal();
                System.out.print(val+" ");
            }
            System.out.println("|");
        }
        String str="";
        for(int k=0;k<(2*n)+4;k++) str=str.concat("-");
        System.out.println(str);
    }
}

