import java.util.*;
import java.io.File; 

class Matriks {
    //atribut
    double[][] Mat = new double[100][100];
    int baris;
    int kolom;
    int mark = -999;

    static Scanner in = new Scanner(System.in);
    static String filename;

    // ** CONSTRUCTOR ** //
    Matriks() {
        //mark adalah -999
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                Mat[i][j] = mark;
            }
        }
        baris=0;
        kolom=0;
    }

    // ** METHOD ** //
    
    //Isi elemen matriks sesuai M dan N
    void IsiMatriks(int M, int N) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.Mat[i][j] = in.nextInt();
            }
        }
        this.baris = M;
        this.kolom = N;
    }

    //Same shit, tpi khusus untuk interpolasi
    void IsiMatriksInterpolasi(int n) {
        for (int i = 0; i <= n; i++) {
            double x = in.nextDouble();
            double y = in.nextDouble();
            for (int j = 0; j <= n; j++) {
                this.Mat[i][j] = Math.pow(x,j);
            }
            this.Mat[i][n+1] = y;
        }
        
        this.baris = n+1;
        this.kolom = n+2;
    }

    void IsiMatriksFile(int i,int N, String[] data){
        double input;
        for (int j = 0; j < N; j++) {
            input=Double.parseDouble(data[j]);
            this.Mat[i][j] = input;
        }
        this.baris++;
        this.kolom = N;
    }

    //Print matriks ke layar
    void TulisMatriks() {
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < this.kolom; j++) {
                if (this.Mat[i][j] != this.mark) {
                    System.out.print(this.Mat[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

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

    //mengubah matriks menjadi matriks eselon reduksi
    static void RREF(double[][] M, int brs, int kol) {
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
                if (i != j ) {
                    double operand = M[j][k] / M[i][k];
                    OperasiThdBaris(M, kol, j, i, operand);
                }
            }
            k++;
            
        }
        //leading one menjadi 1, kemudian print jawaban
        EchelonBeOne(M, brs, kol);
        //PrintEchelonAnswer(M, brs, kol);
    }

    static void REF(double[][] M, int brs, int kol) {
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
        //PrintEchelonREFAnswer(M, brs, kol);
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

    //Cek jika matrix tidak memiliki solusi
    //Jika ujung kanan baris non-0 dan sisanya 0, return true
    static boolean CheckForNoSolution(double[][] M, int brs, int kol) {
        boolean zero;
        for (int i = brs-1; i >= 0; i--) {
            zero = true;
            for (int j = 0; j < kol-1; j++) {
                if (M[i][j] != 0) {
                    zero = false;
                }
            }
            if (zero) {
                return (M[i][kol-1] != 0);
            }
        }
        return false;
    }

    //Mengisi array yang berisi solusi untuk pivot.
    //Jika baris bukan pivot, array akan diisi -999
    static void FillPivotSolution(double[][] M, double[] arr, int brs, int kol) {
        boolean pivot, leadingOneFound;
        int leadingOneIdx = 0;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = -999;
        }

        for (int i = 0; i < brs; i++) {
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

            //mengisi array dengan solusi jika baris merupakan pivot.
            //jika baris berisi 0 atau memiliki angka selain leading one, tidak akan diisi
            if (pivot && leadingOneFound) {
                arr[leadingOneIdx] = M[i][kol-1];
            }
        }
        
    }

    //mengoutput jawaban dari eselon reduksi
    static void PrintEchelonAnswer(double[][] M, int brs, int kol) {
        double[] pivotValue = new double[kol-1];
        boolean pivot, leadingOneFound;
        int leadingOneIdx = 0;

        //jika tidak ada solusi, akan berhenti
        if (CheckForNoSolution(M, brs, kol)) {
            System.out.println("No Solution");
            return;
        }

        //mengisi array pivotValue
        FillPivotSolution(M, pivotValue, brs, kol);

        for (int i = 0; i < brs; i++) {
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
                System.out.println("x" + (i+1) + " = "  + pivotValue[i]);
            } else {
                System.out.print("x" + (i+1) + " = ");
                for (int k = leadingOneIdx + 1; k < kol - 1; k++) {
                    if (M[i][k] != 0) {
                        if (pivotValue[k] == 0) {
                            continue;
                        } else if (pivotValue[k] != -999) {
                            System.out.print(-1*M[i][k]*pivotValue[k] + " + ");
                        } else {
                            System.out.print(-1*M[i][k] + "x" + (k+1) + " + ");
                        }
                    }
                }
                System.out.println(M[i][kol-1]);
            }
        }
    }


    // ** DETERMINANT MATRIX ** //

    // Metode OBE //
    static double determinant1(double[][] M, int n) {
        double det = 1;

        for (int i = 0; i < n; i++) {
            //cek jika diagonal adalah 0,
            //jika 0, ditukar dengan yang tidak 0
            if (M[i][i] == 0) {
                int c = 1;
                //mencari indeks baris di kolom i yang tidak 0
                while ((i + c) < n && M[i+c][i] == 0) {
                    c++;
                }

                //terminasi jika semua baris di kolom i 0
                if ((i + c) == n) {
                    return 0;
                }

                //Tukar baris
                det *= -1;
                Tukar(M, n, i, i+c);
            }
            
            //mengurangi baris sesuai faktor
            for (int j = i+1; j < n; j++) {
                double operand = M[j][i] / M[i][i];
                OperasiThdBaris(M, n, j, i, operand);
            }
            //mengalikan diagonal
            det *= M[i][i];
        }
        return det;
    }

    // Metode Kofaktor //
    //Mendapatkan matriks kofaktor dari baris p dan kolom q pada mat[][]
    //n: dimensi matrix mat[][]
    static void getMatrixCofactor(double mat[][], double temp[][], int p, int q, int n) {
        int i = 0, j = 0;
        for (int brs = 0; brs < n; brs++) {
            for (int kol = 0; kol < n; kol++) {
                if (brs != p && kol != q) {
                    temp[i][j] = mat[brs][kol];
                    j++;
                    //mereset j (kolom) ketika mencapai ujung
                    if (j == n-1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    //Menghitung determinan dari kofaktor yang didapat
    //n : dimensi matrix max[][]
    static double determinant2(double mat[][], int n) {
        double det = 0;
        int sign = 1;

        //Basis
        //jika dimensi 1, akan mereturn indeks [0][0] pada matriks
        if (n == 1) {
            return mat[0][0];
        }
        
        //declare matriks untuk kofaktor
        double[][] temp = new double[n-1][n-1];

        //Rekurens
        //mengalikan baris 0 ke kofaktornya
        for (int k = 0; k < n; k++) {
            getMatrixCofactor(mat, temp, 0, k, n);
            det += sign * mat[0][k] * determinant2(temp, n-1);

            sign = -sign;
        }

        return det;
    }


    // ** SPL METODE CRAMER ** //

    //Mencari solusi spl menggunakan kaidah cramer
    //jika determinan 0, maka tidak ada solusi unik
    static void SPLCramer(double[][] M, int brs, int kol) {
        double[][] MA = new double[brs][kol-1];
        double[] Mb = new double[brs];
        double[][] tempM = new double[brs][kol-1];
        double det, tempDet;

        //jika MA bukan matriks persegi, akan keluar
        if (brs != (kol-1)) {
            System.out.println("SPL tidak memiliki solusi unik");
            return;
        }

        //mengisi matriks A dengan koefisien SPL dan b dengan konstanta
        CopyMatrix(M, MA, brs, kol-1);
        for (int i = 0; i < brs; i++) {
            Mb[i] = M[i][kol-1];
        }

        //mencari determinan dari matriks A, jika 0 akan keluar
        det = determinant2(MA, brs);
        if (det == 0) {
            System.out.println("SPL tidak memiliki solusi unik");
            return;
        }

        //mencari determinan tiap matriks yang telah disubstitusi,
        //kemudian mengoutput jawaban dengan membaginya dengan determinan matriks A
        for (int j = 0; j < kol-1; j++) {
            CopyMatrix(MA, tempM, brs, kol-1);
            for (int i = 0; i < brs; i++) {
                tempM[i][j] = Mb[i];
            }
            tempDet = determinant1(tempM, brs);
            System.out.println("x" + (j+1) + " = " + (tempDet/det));
        }

    }


    // ** INVERSE MATRIX ** //
    
    //mencari apakah matriks memiliki inverse atau tidak
    //true untuk ada, dan false untuk tidak
    static boolean InverseExist(double[][] M, int brs, int kol) {
        boolean zero; 
        double[][] tempM = new double[brs][kol];

        //terminasi jika matriks bukan persegi
        if (brs != kol) {
            return false;
        }

        //mengisi temporary matrix dan dijadikan eselon
        CopyMatrix(M, tempM, brs, kol);
        RREF(tempM, brs, kol);

        //jika ada baris yang berisi nol semua return false
        //jika tidak return true
        for (int i = 0; i < brs; i++) {
            zero = true;
            for (int j = 0; j < kol; j++) {
                if (tempM[i][j] != 0) {
                    zero = false;
                }
            }
            if (zero) {
                return false;
            }
        }
        return true;
    }

    //mengubah M menjadi inversenya
    //jika inverse tidak ada, M tidak berubah dan print warning
    static void Inverse(double[][] M, int n) {
        //Terminasi jika inverse tidak ada
        if (!InverseExist(M, n, n)) {
            System.out.println("Inverse doesn't exist");
            return;
        }

        //menggabungkan matriks dengan matriks identitas
        double[][] extM = new double[n][n*2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                extM[i][j] = M[i][j];
            }
            extM[i][n+i] = 1;
        }
        
        //dijadikan RREF
        RREF(extM, n, n*2);

        //Mengubah M menjadi inversenya (ruas kanan extM)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = extM[i][n+j];
            }
        }
        
    }

    //mengubah matriks menjadi transposenya
    static void Transpose(double[][] M, int n) {
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                double temp = M[i][j];
                M[i][j] = M[j][i];
                M[j][i] = temp;
            }
        }
    }

    static void Inverse2(double[][] M, int n) {
        double[][] adjM = new double[n][n];
        double det = determinant2(M, n);
        double[][] tempM = new double[n-1][n-1];
        
        //mengisi matriks kofaktor dengan cara mencari determinan kofaktor i j
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                getMatrixCofactor(M, tempM, i, j, n);
                adjM[i][j] = Math.pow(-1, i+j) * determinant2(tempM, n-1);
            }
        }
        //menyalinnya ke matriks M kemudian transpose sehingga menjadi adjoin
        CopyMatrix(adjM, M, n, n);
        Transpose(M, n);
        
        //mengalikan dengan 1/det sehingga menghasilkan matriks inverse
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] *= (1/det);
            }
        }
        
}


    // ** SPL METODE MATRIKS BALIKAN ** //

    static void SPLInverse(double[][] M, int brs, int kol) {
        double[][] MA = new double[brs][kol-1];
        double[] Mb = new double[brs];

        //mengisi matriks A dengan koefisien SPL dan
        //mengisi matriks b dengan konstanta
        CopyMatrix(M, MA, brs, kol-1);
        for (int i = 0; i < brs; i++) {
            Mb[i] = M[i][kol-1];
        }

        //terminasi jika matriks A tidak memiliki inverse
        if (!InverseExist(MA, brs, kol-1)) {
            System.out.println("Matriks tidak memiliki inverse");
            return;
        }

        //menginverse matriks A
        Inverse(MA, brs);

        //menghitung hasil perkalian matriks inverse A dengan matriks b
        double[] result = new double[brs];
        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < kol-1; j++) {
                result[i] += (MA[i][j] * Mb[j]);
            }
        }

        //output hasil
        for (int i = 0; i < brs; i++) {
            System.out.println("x" + (i+1) + " = " + result[i]);
        }

    }

    // ** INTERPOLASI MY ASS ** //
    
    static void Interpolasi(double[][] M, int brs, int kol, double taksir) {
        double hasilTaksir = 0;
        
        //mendapatkan solusi SPL
        RREF(M, brs, kol);

        //mengoutput fungsi
        System.out.print("p" + (brs-1) + "(x) = " + M[0][kol-1] + " + ");
        for (int i = 1; i < brs; i++) {
            if (i == brs-1) {
                System.out.print(M[i][kol-1] + "x^" + (brs-1));
            } else if (i == 1) {
                System.out.print(M[i][kol-1] + "x + ");
            } else {
                System.out.print(M[i][kol-1] + "x^" + i + " + ");
            }
        }
        System.out.println();

        //menghitung taksiran kemudian menghoutputnya
        for (int i = 0; i < brs; i++) {
            hasilTaksir += M[i][kol-1] * Math.pow(taksir, i);
        }
        System.out.println(hasilTaksir);
    }

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

    static void FilenameInput(boolean first) throws Exception{
        System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
        if (first){
            String input=in.nextLine();
        }
        filename=in.nextLine();
    }

    // ** DORAIFAA / DRIVER ** //
    public static void main(String[] args) throws Exception {
        //MENU
        int choice,choice2;
        Matriks Matrix = new Matriks();
        System.out.println("!!WARNING!!\nKarena error yang belum bisa diselesaikan, maka input nama file (jika input melalui file) akan dilakukan diawal.");
        FilenameInput(false);
        // filename="test.txt";
        while (true){
            System.out.print("MENU\n1.Sistem Persamaaan Linier\n2.Determinan\n3.Matriks balikan\n4.Interpolasi Polinom\n5.Regresi linier berganda\n6.Ganti nama file\n7.Keluar\nMasukkan pilihan anda (berupa nomor) : ");
            choice=in.nextInt();
            if (choice==1){
                System.out.print("\nPilihan Metode SPL\n1.Metode eliminasi Gauss\n2.Metode eliminasi Gauss-Jordan\n3.Metode matriks balikan\n4.Kaidah Cramer\nMasukkan pilihan anda (berupa nomor) : ");
                choice=in.nextInt();
                System.out.print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice2=in.nextInt();
                if (choice2==1){
                    System.out.print("\nMasukkan jumlah baris : ");
                    int M = in.nextInt();
                    System.out.print("Masukkan jumlah kolom : ");
                    int N = in.nextInt();
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
                    String currentDir = System.getProperty("user.dir");
                    // System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
                    // String filename=in.nextLine();
                    // String filename="test.txt";
                    File file= new File(currentDir+"/"+filename);
                    in=new Scanner(file);
                    while (in.hasNextLine()){
                        data=in.nextLine();
                        dataParts=data.split(" ");
                        N= dataParts.length;
                        Matrix.IsiMatriksFile(i,N,dataParts);
                        i++;
                    }
                    in= new Scanner(System.in);
                }
                switch(choice){
                    case 1 :
                        REF(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        PrintEchelonREFAnswer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        System.out.println();
                        break;
                    case 2 :
                        RREF(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        PrintEchelonAnswer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        System.out.println();
                        break;
                    case 3 :
                        SPLInverse(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        System.out.println();
                        break;
                    case 4 :
                        SPLCramer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        System.out.println();
                        break;
                }
            }
            else if(choice==2){
                System.out.print("\nPilihan Metode Determinan\n1.Metode reduksi baris\n2.Metode ekspansi kofaktor\nMasukkan pilihan anda (berupa nomor) : ");
                choice=in.nextInt();
                System.out.print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice2=in.nextInt();
                if (choice2==1){
                    System.out.print("\nMasukkan jumlah baris : ");
                    int M = in.nextInt();
                    System.out.print("Masukkan jumlah kolom : ");
                    int N = in.nextInt();
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
                    String currentDir = System.getProperty("user.dir");
                    // System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
                    // String filename=in.nextLine();
                    File file= new File(currentDir+"/"+filename);
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()){
                        data=sc.nextLine();
                        dataParts=data.split(" ");
                        N= dataParts.length;
                        Matrix.IsiMatriksFile(i,N,dataParts);
                        i++;
                    }
                    in= new Scanner(System.in);
                }
                double hasil;
                switch(choice){
                    case 1 :
                        hasil = determinant1(Matrix.Mat,Matrix.kolom);
                        System.out.println("Determinan = "+ hasil);
                        System.out.println();
                        break;
                    case 2 :
                        hasil = determinant2(Matrix.Mat,Matrix.kolom);
                        System.out.println("Determinan = "+ hasil);
                        System.out.println();
                        break;
                }
            }
            else if (choice==3){
                System.out.print("\nPilihan Metode Invers\n1.Metode reduksi baris\n2.Metode adjoin\nMasukkan pilihan anda (berupa nomor) : ");
                choice=in.nextInt();
                System.out.print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice2=in.nextInt();
                if (choice2==1){
                    System.out.print("\nMasukkan jumlah baris : ");
                    int M = in.nextInt();
                    System.out.print("Masukkan jumlah kolom : ");
                    int N = in.nextInt();
                    System.out.println("Masukkan isi matriks : ");
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
                    String currentDir = System.getProperty("user.dir");
                    // System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
                    // String filename=in.nextLine();
                    File file= new File(currentDir+"/"+filename);
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()){
                        data=sc.nextLine();
                        dataParts=data.split(" ");
                        N= dataParts.length;
                        Matrix.IsiMatriksFile(i,N,dataParts);
                        i++;
                    }
                    in= new Scanner(System.in);
                }
                switch(choice){
                    case 1:
                        Inverse(Matrix.Mat,Matrix.baris);
                        Matrix.TulisMatriks();
                        System.out.println();
                        break;
                    case 2:
                        Inverse2(Matrix.Mat,Matrix.baris);
                        Matrix.TulisMatriks();
                        System.out.println();
                        break;
                }
            }
            else if(choice==4){
                System.out.print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice=in.nextInt();
                double N=0;
                if (choice==1){
                    System.out.print("\nMasukkan ukuran matriks (satu ukuran, mxm) : ");
                    int M = in.nextInt();
                    System.out.print("Nilai x yang akan ditaksir? : ");
                    N = in.nextDouble();
                    Matrix.IsiMatriksInterpolasi(M);
                }
                else{
                    int i=0;
                    int Kol;
                    String data;
                    String[] dataParts;
                    String currentDir = System.getProperty("user.dir");
                    boolean firstTime=true;
                    // System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
                    // String filename=in.nextLine();
                    File file= new File(currentDir+"/"+filename);
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()){
                        if (firstTime){
                            data=sc.nextLine();
                            N=Double.parseDouble(data);
                            firstTime=false;
                        }
                        else{
                            data=sc.nextLine();
                            dataParts=data.split(" ");
                            Kol= dataParts.length;
                            Matrix.IsiMatriksFile(i,Kol,dataParts);
                            i++;
                        }
                    }
                    in= new Scanner(System.in);
                }
                Interpolasi(Matrix.Mat, Matrix.baris, Matrix.kolom, N);
                System.out.println();
            }
            else if (choice==5){
                System.out.print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice=in.nextInt();
                double N=0;
                if (choice==1){
                    System.out.println("\nMasukkan nilai xk yang akan ditaksir");
                    N = in.nextDouble();
                    System.out.println("Masukkan jumlah n");
                    int M = in.nextInt();
                    System.out.println("Masukkan jumlah i");
                    int O = in.nextInt();
                    System.out.println("Masukkan datanya dalam format");
                    System.out.println("(x11..xn1,y1)");
                    System.out.println("(...........)");
                    System.out.println("(x1i..xni,yi)");
                    Matrix.IsiMatriks(O,M+1);
                }
                else{
                    int i=0;
                    int Kol;
                    String data;
                    String[] dataParts;
                    String currentDir = System.getProperty("user.dir");
                    boolean firstTime=true;
                    // System.out.print("Nama file .txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : ");
                    // String filename=in.nextLine();
                    File file= new File(currentDir+"/"+filename);
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()){
                        if (firstTime){
                            data=sc.nextLine();
                            N=Double.parseDouble(data);
                            firstTime=false;
                        }
                        else{
                            data=sc.nextLine();
                            dataParts=data.split(" ");
                            Kol= dataParts.length;
                            Matrix.IsiMatriksFile(i,Kol,dataParts);
                            i++;
                        }
                    }
                    in= new Scanner(System.in);
                }
                regresi(Matrix.Mat, Matrix.baris, Matrix.kolom, N);
                System.out.println();

            }
            else if (choice==7){
                break;
            }
            else if(choice==6){
                FilenameInput(true);
            }
            else {
                System.out.println("Tolong masukan input yang benar.\n");
            }

        }

        //input for regresi
        //made for my own testing sanity (Hagli)
        // Scanner in = new Scanner(System.in);
        // Matriks Matrix = new Matriks();
        // System.out.println("Masukkan nilai xk yang akan ditaksir");
        // double N = in.nextDouble();
        // System.out.println("Masukkan jumlah n");
        // int M = in.nextInt();
        // System.out.println("Masukkan jumlah i");
        // int O = in.nextInt();
        // System.out.println("Masukkan datanya dalam format");
        // System.out.println("(x11..xn1,y1)");
        // System.out.println("(...........)");
        // System.out.println("(x1i..xni,yi)");
        // Matrix.IsiMatriks(O,M+1);
        

        //input for interpolasi
        /*Scanner in = new Scanner(System.in);
        Matriks Matrix = new Matriks();
        int M = in.nextInt();
        double N = in.nextDouble();
        Matrix.IsiMatriksInterpolasi(M);*/

        //input for the rest
        /*Scanner in = new Scanner(System.in);
        Matriks Matrix = new Matriks();
        int M = in.nextInt();
        int N = in.nextInt();
        Matrix.IsiMatriks(M, N);*/

        // * Insert Function Here * //
        //SPLCramer(Matrix.Mat, Matrix.baris, Matrix.kolom);
        //SPLInverse(Matrix.Mat, Matrix.baris, Matrix.kolom);
        //RREF(Matrix.Mat, Matrix.baris, Matrix.kolom);
        //REF(Matrix.Mat, Matrix.baris, Matrix.kolom);
        //interpolasi(Matrix.Mat, Matrix.baris, Matrix.kolom, N);
        // regresi(Matrix.Mat, Matrix.baris, Matrix.kolom, N);
        //Matrix.TulisMatriks();
        
    }


/*
    void eselon(int M, int N, boolean isForDeterminant){
        //sebelum mulai, dilihat dulu apakah fungsi ini digunakan untuk menghitung determinan atau tidak
        if (isForDeterminant){
            M = this.baris;
            N = this.kolom;
        }
        //pengecekan apakah perlu Tukar menukar
        int[] note = new int[M];
        //isi array note dengan 999
        for (int i = 0; i < M; i++){
            note[i] = 999;
        }
        //mencatat kemunculan elemen non 0 di Matriks utama
        int a=0;
        int b=0;
        boolean firstTime=true;
        while (a<M){
            b=0;
            while (b<N){
                if (this.Mat[a][b]!=0 && firstTime){
                    note[a]=b;
                    firstTime=false;
                }
                b++;
            }
            a++;
            firstTime=true;
        }
        //mengecek apakah elemen non 0 yg pertama muncul sudah terurut. jika tidak, maka diperlukan Tukar menukar baris
        boolean isSorted=true;
        int temp=note[0];
        //cek apakah terurut menaik
        for (int i=1; i<M && isSorted;i++){
            if (note[i]<temp){
                isSorted=false;
            }
            temp=note[i];
        }
        
        if (!isSorted){ //jika tidak terurut, maka harus Tukar menukar
            for (int i=0;i<M-1;i++){
                for (int j=i+1;j<M;j++){
                    if (note[i]>note[j]){
                        temp=note[i];
                        note[i]=note[j];
                        note[j]=temp;
                        this.Tukar(i,j);
                        this.determinant*=(-1);
                    }
                }
            }
        }
        //lakukan operasi-operasi
        int eselonOrder=0;
        double operand;
        int i=0;
        //Matriks yg berada di kolom eselonOrder harus 1
        this.EchelonBeOne(M,N);
        //semua sudah terurut dan urutan eselon sudah seharusnya.
        //sekarang, memastikan yg berada di bawah dan atas itu 0
        //karena saya mager, jadi ini adalah barisan kode untuk memastikan yg bawah saja yg 0 dulu
        eselonOrder= this.findEselonAt(0); //mencari elemen 1 pada kolom keberapa di suatu baris
        int acuanBaris=0;
        i=1;
        boolean done=false;
        while(i<M && !done){
            if (this.Mat[i][eselonOrder]!=0){
                if (this.Mat[i][eselonOrder]>0 && this.Mat[acuanBaris][eselonOrder]>0){
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else if (this.Mat[i][eselonOrder]<0 && this.Mat[acuanBaris][eselonOrder]>0){
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else if (this.Mat[i][eselonOrder]>0 & this.Mat[acuanBaris][eselonOrder]<0){
                    this.OperasiThdBaris(i,eselonOrder,((this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else{
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
            }
            if (eselonOrder==N-1 && i==M-1){
                done=true;
            }
            if (i==M-1){
                this.EchelonBeOne(M,N);
                eselonOrder=this.findEselonAt(acuanBaris+1);
                acuanBaris++;
                i=acuanBaris+1;
            }
            else{
                i++;
            }
        }
        //sekarang memastikan atasnya itu 0
        eselonOrder= this.findEselonAt(1); //mencari elemen 1 pada kolom keberapa di suatu baris
        acuanBaris=1;
        i=acuanBaris-1;
        done=false;
        while(!done){
            if (this.Mat[i][eselonOrder]!=0){
                if (this.Mat[i][eselonOrder]>0 && this.Mat[acuanBaris][eselonOrder]>0){
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else if (this.Mat[i][eselonOrder]<0 && this.Mat[acuanBaris][eselonOrder]>0){
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else if (this.Mat[i][eselonOrder]>0 & this.Mat[acuanBaris][eselonOrder]<0){
                    this.OperasiThdBaris(i,eselonOrder,((this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
                else{
                    this.OperasiThdBaris(i,eselonOrder,(-1*(this.Mat[i][eselonOrder])/this.Mat[acuanBaris][eselonOrder]));
                }
            }
            if (eselonOrder==N-1 && i<=0){
                done=true;
            }
            else if (i==0){
                this.EchelonBeOne(M,N);
                eselonOrder=this.findEselonAt(acuanBaris+1);
                acuanBaris++;
                i=acuanBaris-1;
            }
            else{
                i--;
            }
        }
        System.out.println();
        this.TulisMatriks();
        System.out.println();
        System.out.println("determinan : "+this.determinant);
    }

*/

}
