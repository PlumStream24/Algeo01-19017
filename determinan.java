public class determinan {
    // ** FUNKY ** //
    //tukar 2 baris
    static void tukar(double[][] M, int kolom, int baris1, int baris2) {
        double temp;
        for (int k = 0; k < kolom; k++) {
            temp = M[baris1][k];
            M[baris1][k] = M[baris2][k];
            M[baris2][k] = temp;
        }
    }

    //operasi antara 2 baris
    static void operasiThdBaris(double[][] M, int kolom, int barisTarget, int barisOperator, double faktor) {
        for (int k = 0; k < kolom; k++) {
            M[barisTarget][k] -= (M[barisOperator][k] * faktor);
        }
    }

    // ** Mencari Determinan ** //

    // Metode OBE //
    static double determinant(double[][] M, int baris, int kolom) {
        double det = 1;

        for (int i = 0; i < baris; i++) {
            //cek jika diagonal adalah 0,
            //jika 0, ditukar dengan yang tidak 0
            if (M[i][i] == 0) {
                int c = 1;
                //mencari indeks baris di kolom i yang tidak 0
                while ((i + c) < baris && M[i+c][i] == 0) {
                    c++;
                }

                //terminasi jika semua baris di kolom i 0
                if ((i + c) == baris) {
                    return 0;
                }

                //tukar baris
                det *= -1;
                tukar(M, kolom, i, i+c);
            }
            
            //mengurangi baris sesuai faktor
            for (int j = i+1; j < baris; j++) {
                double operand = M[j][i] / M[i][i];
                operasiThdBaris(M, kolom, j, i, operand);
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
}
