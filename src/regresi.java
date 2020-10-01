public class regresi {
    //regresion-borne
    //shit's fucked my guy, (me) too stupid for this
    static void REFRegresi(double[][] M, int brs, int kol, double randomx) {
        int k = 0;
        int c = 0;
        //double det = 1;

        for (int i = 0; i < brs; i++) {
            //cek jika diagonal adalah 0
            //jika 0, ditukar dengan yang tidak 0
            //jika tidak ada baris dibawahnya yang tidak 0, dicari di kolom selanjutnya
            c = 0;
            while (M[i+c][k] == 0) {
                c++;

                if ((i + c) == brs) {
                    c = 0;
                    k++;
                }

                if (k == kol) {
                    break;
                }
            }

            if (c != 0) {
                Tukar(M, kol, i, i+c);
            }

            //mengurangi baris sesuai faktor
            for (int j = 0; j < brs; j++) {
                if (i != j && j>i ) {
                    double operand = M[j][k] / M[i][k];
                    OperasiThdBaris(M, kol, j, i, operand);
                }
            }
            k++;
            
        }
        //leading one menjadi 1, kemudian print jawaban
        EchelonBeOne(M, brs, kol);
        PrintEchelonREFAnswerRegresi(M, brs, kol,randomx);
    }
    
    static void PrintEchelonREFAnswerRegresi(double[][] M, int brs, int kol, double randomx){
        double[] pivotValue = new double[kol-1];
        boolean pivot, leadingOneFound;
        int leadingOneIdx = 0;
        double[] solution= new double[kol-1];
        String[] unknownSolution = new String[kol-1];
        boolean[] solutionIsDefineable=new boolean[kol-1];
        
        //filling solution with null because fuck you, that's why
        for (int howard=0;howard<kol-1;howard++){
            solution[howard]=-999;
            unknownSolution[howard]="";
            solutionIsDefineable[howard]=false;
        }

        //jika tidak ada solusi, akan berhenti
        if (CheckForNoSolution(M, brs, kol)) {
            System.out.println("No Solution");
            return;
        }

        checkSolutionExistence(M,brs,kol,solutionIsDefineable);

        //mengisi array pivotValue
        FillPivotSolution(M, pivotValue, brs, kol);
        
        for (int i = brs-1; i >= 0; i--) {
            //mengecek apakah baris merupakan pivot atau bukan
            pivot = true;
            leadingOneFound = false;

            for (int j = 0; j < kol - 1; j++) {
                if (M[i][j] != 0) {
                    if (!leadingOneFound) {
                        leadingOneFound = true;
                        leadingOneIdx = j;
                    } else {
                        pivot = false;
                    }
                }
            }

            //jika baris 0 semua, akan dilanjutkan ke baris selanjutnya
            if (!leadingOneFound) {
                continue;
            }

            //jika baris pivot akan output solusi uniknya
            //jika bukan, akan menggunakan parametrik
            if (pivot) {
                solution[leadingOneIdx]=pivotValue[i];
            } else {
                //filling solution
                int idx=findEselonAt(M,i,kol);
                if (solutionIsDefineable[idx]){
                    solution[idx]=M[i][kol-1];
                    for (int todd=0;todd<kol-1;todd++){
                        if (todd!=idx && M[i][todd]!=0){
                            solution[idx]+=(-1*solution[todd]*M[i][todd]);
                        }
                    }
                }
                else{
                    solution[idx]=M[i][kol-1];
                    for (int todd=0;todd<kol-1;todd++){
                        if (todd!=idx && M[i][todd]!=0 && solutionIsDefineable[todd]){
                            solution[idx]+=(-1*solution[todd]*M[i][todd]);
                        }
                        else if(!solutionIsDefineable[todd] && M[i][todd]!=0 ){
                            if (todd!=idx){
                                unknownSolution[i]+=(" + "+(M[i][todd])+"x"+(todd+1))+" ";
                            }
                            else{
                                unknownSolution[i]+=((M[i][todd])+"x"+(todd+1))+" ";
                            }
                        }
                    }
                    unknownSolution[i]+=(" = "+(solution[idx]));
                }
            }
        }

        //Printing the solutions
        System.out.print("yi = ");
        double KEKKA = 0;
        for (int hitler=0;hitler<kol-1;hitler++){
            //printing the valid solution/unique solution
            if (solutionIsDefineable[hitler]){
                if(hitler==0){
                    System.out.print(solution[hitler]);
                    KEKKA += solution[hitler];
                }
                else {
                    KEKKA += randomx*solution[hitler];
                    if (solution[hitler]<0){
                        System.out.print(" - "+-1*solution[hitler]+"x"+hitler+"i");
                    }
                    else{
                        System.out.print(" + "+solution[hitler]+"x"+hitler+"i");
                    }
                }
            }
        }
        System.out.println(" + ε");
        System.out.println("Taksiran "+randomx+" adalah "+KEKKA+" + ε");
        //printing the not so unique solutions
        for (int hitler=0;hitler<kol-1;hitler++){
            if (unknownSolution[hitler]!=""){
                System.out.println("nope.avi");
            }
        }
    }

    static void regresi(double[][] M, int brs, int kol, double randomx){
        int kolA = kol+1;
        double[][] funky = new double[kol][kolA];
        for (int i = 0;i<kol;i++){
            for (int j = 0;j<kolA;j++){
                if (((j==0) || (i==0)) && (j!=kol)) {
                    if ((j==0) && (i==0)){
                        funky[i][j] = kol-1;
                    }
                    else if (i==0){
                        int sajam = 0;
                        for (int k = 0;k<brs;k++){
                            sajam += M[k][j-1];
                        }
                        funky [i][j] = sajam;
                    }
                    else{
                        funky [i][j] = funky[j][i];
                    }
                }
                else if(j!=kol){
                    int sajam = 0;
                    for(int k = 0;k<brs;k++){
                        sajam += M[k][j-1]*M[k][i-1];
                    }
                    funky[i][j] = sajam;
                }
                else if(j==kol){
                    if (i==0){
                        int sajam = 0;
                        for(int k = 0;k<brs;k++){
                            sajam += M[k][j-1];
                        }
                        funky[i][j] = sajam;
                    }
                    else{
                        int sajam = 0;
                        for(int k = 0;k<brs;k++){
                            sajam += M[k][i-1]*M[k][j-1];
                        }
                        funky[i][j] = sajam;
                    }
                }
            }
        }
        REFRegresi(funky, kol, kolA, randomx);
        //printing this shit
        /*for (int i = 0;i<kol;i++){
            for (int j = 0;j<kolA;j++){
                System.out.print(funky[i][j]+" ");
            }
            System.out.println("");
        }*/
    }
}
