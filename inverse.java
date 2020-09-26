public class inverse {
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
        Matriks.CopyMatrix(M, tempM, brs, kol);
        Matriks.RREF(tempM, brs, kol);

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
        Matriks.RREF(extM, n, n*2);

        //Mengubah M menjadi inversenya (ruas kanan extM)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = extM[i][n+j];
            }
        }
        
    }


    // ** SPL METODE MATRIKS BALIKAN ** //

    static void SPLInverse(double[][] M, int brs, int kol) {
        double[][] MA = new double[brs][kol-1];
        double[] Mb = new double[brs];

        //mengisi matriks A dengan koefisien SPL dan
        //mengisi matriks b dengan konstanta
        Matriks.CopyMatrix(M, MA, brs, kol-1);
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

}
