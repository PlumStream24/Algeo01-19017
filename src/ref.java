public class ref{
     // ** FUNKY ** //

    //menukar 2 baris
    static void Tukar(double[][] M, int kol, int baris1, int baris2) {
        double temp;
        for (int j = 0; j < kol; j++) {
            temp = M[baris1][j];
            M[baris1][j] = M[baris2][j];
            M[baris2][j] = temp;
        }
    }

    //mengalikan baris dengan konstanta
    static void OperasiKaliBaris(double[][] M, int kol, int brsTarget, double operand) {
        for (int j = 0; j < kol; j++) {
            M[brsTarget][j] = M[brsTarget][j] * operand;
        }
    }

    //operasi baris dengan baris lain
    static void OperasiThdBaris(double[][] M, int kol, int brsTarget, int brsOperator, double faktor) {
        for (int j = 0; j < kol; j++) {
            M[brsTarget][j] -= (M[brsOperator][j] * faktor);
        }
    }

    //menyalin matriks
    static void CopyMatrix(double[][] Morigin, double[][] Mresult, int brs, int kol) {
        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < kol; j++) {
                Mresult[i][j] = Morigin[i][j];
            }
        }
    }


    // ** REDUCED ROW ECHELON FORM ** //

    //mengubah diagonal menjadi 1
    static void EchelonBeOne(double[][] M, int brs,int kol) {
        int eselonOrder = 0;
        double operand;
        int i = 0;
        //Matriks yg berada di kolom eselonOrder harus 1
        while(i < brs && eselonOrder < kol){
            if (M[i][eselonOrder] != 0){
                operand = (1 / M[i][eselonOrder]);
                OperasiKaliBaris(M, kol, i, operand);
                
                i++;
                eselonOrder++;
            } else {
                eselonOrder++;
            }
        }
    }

    static int findEselonAt(double[][] M,int barisKe,int kol){
        for (int j=0;j<kol;j++){
            if (Math.round(M[barisKe][j])==1){
                return j;
            }
        }
        return 0;
    }

    static void checkSolutionExistence(double[][] M, int brs, int kol,boolean[] solutionIsDefineable){
        for (int i=brs-1;i>=0;i--){
            boolean pivot = true;
            boolean leadingOneFound = false;
            int leadingOneIdx=0;

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

            if (!leadingOneFound){
                continue;
            }
            else if (pivot){
                solutionIsDefineable[leadingOneIdx]=true;
            }
            else{ //berarti ada banyak variable dalam satu baris.
                int idx=findEselonAt(M,i,kol-1);
                if (solutionIsDefineable[idx]==false){//jika belum terdefinisi, cek dulu apakah dia sebenarnya udah bisa didefinisikan atau belum
                    solutionIsDefineable[idx]=true; //ganti nilai default
                    for (int j=idx;j<kol-1;j++){
                        if (M[i][j]!=0){
                            solutionIsDefineable[idx]=solutionIsDefineable[idx] && solutionIsDefineable[j];
                        }
                    }
                }
            }
        }
    }

    static void PrintEchelonREFAnswer(double[][] M, int brs, int kol){
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
        for (int hitler=0;hitler<kol-1;hitler++){
            //printing the valid solution/unique solution
            if (solutionIsDefineable[hitler]){
                System.out.println("x"+(hitler+1)+" = "+solution[hitler]);
            }
        }
        //printing the not so unique solutions
        for (int hitler=0;hitler<kol-1;hitler++){
            if (unknownSolution[hitler]!=""){
                System.out.println(unknownSolution[hitler]);
            }
        }
    }
}
