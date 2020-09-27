public class determinan {
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
                Matriks.Tukar(M, n, i, i+c);
            }
            
            //mengurangi baris sesuai faktor
            for (int j = i+1; j < n; j++) {
                double operand = M[j][i] / M[i][i];
                Matriks.OperasiThdBaris(M, n, j, i, operand);
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
        Matriks.CopyMatrix(M, MA, brs, kol-1);
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
            Matriks.CopyMatrix(MA, tempM, brs, kol-1);
            for (int i = 0; i < brs; i++) {
                tempM[i][j] = Mb[i];
            }
            tempDet = determinant1(tempM, brs);
            System.out.println("x" + (j+1) + " = " + (tempDet/det));
        }

    }

}
