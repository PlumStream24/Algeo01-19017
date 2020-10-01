import java.util.*;
import java.io.*; 

class Matriks {
    //atribut
    double[][] Mat = new double[100][100];
    int baris;
    int kolom;
    int mark = -999;

    static Scanner in = new Scanner(System.in);
    static String filename;
    static String currentDir = System.getProperty("user.dir");

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
    void IsiMatriks(int M, int N) throws Exception{
        print("Masukkan elemen-elemen matriks : \n");
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
    void TulisMatriks() throws Exception{
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < this.kolom; j++) {
                if (this.Mat[i][j] != this.mark) {
                    print(this.Mat[i][j] + " ");
                }
            }
            print("\n");
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
            if (k == kol) break;
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

    static void PrintEchelonREFAnswer(double[][] M, int brs, int kol) throws Exception{
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
            print("No Solution\n");
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
                print("x"+(hitler+1)+" = "+solution[hitler]+"\n");
            }
        }
        //printing the not so unique solutions
        for (int hitler=0;hitler<kol-1;hitler++){
            if (unknownSolution[hitler]!=""){
                print(unknownSolution[hitler]+"\n");
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
    static void FillPivotSolution(double[][] M, double[] arr, int brs, int kol) throws Exception{
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
    static void PrintEchelonAnswer(double[][] M, int brs, int kol) throws Exception{
        double[] pivotValue = new double[kol-1];
        boolean pivot, leadingOneFound;
        int leadingOneIdx = 0;

        //jika tidak ada solusi, akan berhenti
        if (CheckForNoSolution(M, brs, kol)) {
            print("No Solution\n");
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
                print("x" + (i+1) + " = "  + pivotValue[i]+"\n");
            } else {
                print("x" + (i+1) + " = ");
                for (int k = leadingOneIdx + 1; k < kol - 1; k++) {
                    if (M[i][k] != 0) {
                        if (pivotValue[k] == 0) {
                            continue;
                        } else if (pivotValue[k] != -999) {
                            print(-1*M[i][k]*pivotValue[k] + " + ");
                        } else {
                            print(-1*M[i][k] + "x" + (k+1) + " + ");
                        }
                    }
                }
                print(M[i][kol-1]+"\n");
            }
        }
    }


    // ** DETERMINANT MATRIX ** //

    // Metode OBE //
    static double determinant1(double[][] M, int n) throws Exception{
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
    static void getMatrixCofactor(double mat[][], double temp[][], int p, int q, int n) throws Exception{
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
    static double determinant2(double[][] M, int n) throws Exception{
        double det = 0;
        int sign = 1;

        //Basis
        //jika dimensi 1, akan mereturn indeks [0][0] pada matriks
        if (n == 1) {
            return M[0][0];
        }
        
        //declare matriks untuk kofaktor
        double[][] temp = new double[n-1][n-1];

        //Rekurens
        //mengalikan baris 0 ke kofaktornya
        for (int k = 0; k < n; k++) {
            getMatrixCofactor(M, temp, 0, k, n);
            det += sign * M[0][k] * determinant2(temp, n-1);

            sign = -sign;
        }

        return det;
    }


    // ** SPL METODE CRAMER ** //

    //Mencari solusi spl menggunakan kaidah cramer
    //jika determinan 0, maka tidak ada solusi unik
    static void SPLCramer(double[][] M, int brs, int kol) throws Exception{
        double[][] MA = new double[brs][kol-1];
        double[] Mb = new double[brs];
        double[][] tempM = new double[brs][kol-1];
        double det, tempDet;

        //jika MA bukan matriks persegi, akan keluar
        if (brs != (kol-1)) {
            print("SPL tidak memiliki solusi unik\n");
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
            print("SPL tidak memiliki solusi unik\n");
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
            print("x" + (j+1) + " = " + (tempDet/det)+"\n");
        }

    }


    // ** INVERSE MATRIX ** //
    
    //mencari apakah matriks memiliki inverse atau tidak
    //true untuk ada, dan false untuk tidak
    static boolean InverseExist(double[][] M, int brs, int kol) throws Exception{
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
    static void Inverse(double[][] M, int n) throws Exception{
        //Terminasi jika inverse tidak ada
        if (!InverseExist(M, n, n)) {
            print("Inverse doesn't exist\n");
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
    static void Transpose(double[][] M, int n) throws Exception{
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                double temp = M[i][j];
                M[i][j] = M[j][i];
                M[j][i] = temp;
            }
        }
    }

    static void Inverse2(double[][] M, int n) throws Exception{
        double[][] adjM = new double[n][n];
        double det = determinant2(M, n);
        double[][] tempM = new double[n-1][n-1];
        
        if (det == 0) {
            System.out.println("Inverse doesn't exist");
            return;
        }

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

    static void SPLInverse(double[][] M, int brs, int kol) throws Exception{
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
            print("Matriks tidak memiliki inverse\n");
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
            print("x" + (i+1) + " = " + result[i]+"\n");
        }

    }

    // ** INTERPOLASI MY ASS ** //
    
    static void Interpolasi(double[][] M, int brs, int kol, double taksir) throws Exception{
        double hasilTaksir = 0;
        
        //mendapatkan solusi SPL
        RREF(M, brs, kol);

        //mengoutput fungsi
        print("p" + (brs-1) + "(x) = " + M[0][kol-1] + " + ");
        for (int i = 1; i < brs; i++) {
            if (i == brs-1) {
                print(M[i][kol-1] + "x^" + (brs-1));
            } else if (i == 1) {
                print(M[i][kol-1] + "x + ");
            } else {
                print(M[i][kol-1] + "x^" + i + " + ");
            }
        }
        print("\n");

        //menghitung taksiran kemudian menghoutputnya
        for (int i = 0; i < brs; i++) {
            hasilTaksir += M[i][kol-1] * Math.pow(taksir, i);
        }
        print(hasilTaksir+"\n");
    }

    //regresion-borne
    //shit's fucked my guy, (me) too stupid for this
    static void REFRegresi(double[][] M, int brs, int kol, double randomx) throws Exception{
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
    
    static void PrintEchelonREFAnswerRegresi(double[][] M, int brs, int kol, double randomx) throws Exception{
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
            print("No Solution\n");
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
        print("yi = ");
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
                        print(" - "+-1*solution[hitler]+"x"+hitler+"i");
                    }
                    else{
                        print(" + "+solution[hitler]+"x"+hitler+"i");
                    }
                }
            }
        }
        print(" + ε\n");
        print("Taksiran "+randomx+" adalah "+KEKKA+" + ε\n");
        //printing the not so unique solutions
        for (int hitler=0;hitler<kol-1;hitler++){
            if (unknownSolution[hitler]!=""){
                print("nope.avi\n");
            }
        }
    }

    static void regresi(double[][] M, int brs, int kol, double randomx) throws Exception{
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
        print("Nama file txt? (Pastikan berada di directory yang sama. contoh input: \"test.txt\") : \n");
        if (first){
            String input=in.nextLine();
        }
        filename=in.nextLine();
    }

    static void print(String obj) throws Exception{
        PrintStream fileOutput = new PrintStream(new FileOutputStream(currentDir+"/log.txt",true)); 
        PrintStream console = System.out; 

        System.setOut(fileOutput); 
        System.out.print(obj); 
  

        System.setOut(console); 
        System.out.print(obj); 
    }

    // ** DORAIFAA / DRIVER ** //
    public static void main(String[] args) throws Exception {
        //MENU
        int choice,choice2;
        Matriks Matrix = new Matriks();
        print("!!WARNING!!\nKarena error yang belum bisa diselesaikan, maka input nama file (jika input melalui file) akan dilakukan diawal.\n");
        FilenameInput(false);
        // filename="test.txt";
        while (true){
            print("\n#####MENU#####\n1.Sistem Persamaaan Linier\n2.Determinan\n3.Matriks balikan\n4.Interpolasi Polinom\n5.Regresi linier berganda\n6.Ganti nama file\n7.Keluar\nMasukkan pilihan anda (berupa nomor) : \n");
            choice=in.nextInt();
            if (choice==1){
                print("\n=====SISTEM PERSAMAAN LINIER=====\n");
                print("Pilihan Metode SPL\n1.Metode eliminasi Gauss\n2.Metode eliminasi Gauss-Jordan\n3.Metode matriks balikan\n4.Kaidah Cramer\nMasukkan pilihan anda (berupa nomor) : \n");
                choice=in.nextInt();
                print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : \n");
                choice2=in.nextInt();
                if (choice2==1){
                    print("\nMasukkan jumlah baris : \n");
                    int M = in.nextInt();
                    print("Masukkan jumlah kolom : \n");
                    int N = in.nextInt();
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
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
                        print("[Metode eliminasi Gauss]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        REF(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        PrintEchelonREFAnswer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        print("\n");
                        break;
                    case 2 :
                        print("[Metode eliminasi Gauss-jordan]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        RREF(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        PrintEchelonAnswer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        print("\n");
                        break;
                    case 3 :
                        print("[Metode matriks balikan]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        SPLInverse(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        print("\n");
                        break;
                    case 4 :
                        print("[Metode crammer]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        SPLCramer(Matrix.Mat, Matrix.baris, Matrix.kolom);
                        print("\n");
                        break;
                }
            }
            else if(choice==2){
                print("\n=====DETERMINAN=====\n");
                print("Pilihan Metode Determinan\n1.Metode reduksi baris\n2.Metode ekspansi kofaktor\nMasukkan pilihan anda (berupa nomor) : \n");
                choice=in.nextInt();
                print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : \n");
                choice2=in.nextInt();
                if (choice2==1){
                    print("\nMasukkan jumlah baris : \n");
                    int M = in.nextInt();
                    print("Masukkan jumlah kolom : \n");
                    int N = in.nextInt();
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
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
                        print("[Metode reduksi baris]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        hasil = determinant1(Matrix.Mat,Matrix.kolom);
                        print("Determinan = "+ hasil+"\n");
                        break;
                    case 2 :
                        print("[Metode ekspansi kofaktor]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        hasil = determinant2(Matrix.Mat,Matrix.kolom);
                        print("Determinan = "+ hasil+"\n");
                        break;
                }
            }
            else if (choice==3){
                print("\n=====MATRIKS BALIKAN/INVERS=====\n");
                print("Pilihan Metode Invers\n1.Metode reduksi baris\n2.Metode adjoin\nMasukkan pilihan anda (berupa nomor) : ");
                choice=in.nextInt();
                print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : ");
                choice2=in.nextInt();
                if (choice2==1){
                    print("\nMasukkan jumlah baris : \n");
                    int M = in.nextInt();
                    print("Masukkan jumlah kolom : \n");
                    int N = in.nextInt();
                    print("Masukkan isi matriks : \n");
                    Matrix.IsiMatriks(M, N);
                }
                else{
                    int i=0;
                    int N;
                    String data;
                    String[] dataParts;
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
                        print("[Metode reduksi baris]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        Inverse(Matrix.Mat,Matrix.baris);
                        print("Matriks invers : \n");
                        Matrix.TulisMatriks();
                        System.out.println();
                        break;
                    case 2:
                        print("[Metode adjoin]\n");
                        print("Matriks input : \n");
                        Matrix.TulisMatriks();
                        Inverse2(Matrix.Mat,Matrix.baris);
                        print("Matriks invers : \n");
                        Matrix.TulisMatriks();
                        System.out.println();
                        break;
                }
            }
            else if(choice==4){
                print("\n=====INTERPOLASI POLINOM=====\n");
                print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : \n");
                choice=in.nextInt();
                double N=0;
                if (choice==1){
                    print("\nMasukkan ukuran matriks (satu ukuran, mxm) : \n");
                    int M = in.nextInt();
                    print("Nilai x yang akan ditaksir? : \n");
                    N = in.nextDouble();
                    Matrix.IsiMatriksInterpolasi(M);
                }
                else{
                    int i=0;
                    int Kol;
                    String data;
                    String[] dataParts;
                    boolean firstTime=true;
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
                print("\n=====REGRESI LINIER BERGANDA=====\n");
                print("Apakah akan memasukkan input dari 1)keyboard or 2)file? (masukkan angka pilihan) : \n");
                choice=in.nextInt();
                double N=0;
                if (choice==1){
                    print("\nMasukkan nilai xk yang akan ditaksir\n");
                    N = in.nextDouble();
                    print("Masukkan jumlah n\n");
                    int M = in.nextInt();
                    print("Masukkan jumlah i\n");
                    int O = in.nextInt();
                    print("Masukkan datanya dalam format\n");
                    print("(x11..xn1,y1)\n");
                    print("(...........)\n");
                    print("(x1i..xni,yi)\n");
                    Matrix.IsiMatriks(O,M+1);
                }
                else{
                    int i=0;
                    int Kol;
                    String data;
                    String[] dataParts;
                    boolean firstTime=true;
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
                print("Tolong masukan input yang benar.\n\n");
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

}
