public class rref {
    // ** FUNKY ** //

    //menukar 2 baris
    static void tukar(double[][] M, int kol, int baris1, int baris2) {
        double temp;
        for (int j = 0; j < kol; j++) {
            temp = M[baris1][j];
            M[baris1][j] = M[baris2][j];
            M[baris2][j] = temp;
        }
    }

    //mengalikan baris dengan konstanta
    static void operasiKaliBaris(double[][] M, int kol, int brsTarget, double operand) {
        for (int j = 0; j < kol; j++) {
            M[brsTarget][j] = M[brsTarget][j] * operand;
        }
    }

    //operasi baris dengan baris lain
    static void operasiThdBaris(double[][] M, int kol, int brsTarget, int brsOperator, double faktor) {
        for (int j = 0; j < kol; j++) {
            M[brsTarget][j] -= (M[brsOperator][j] * faktor);
        }
    }

    //mengubah diagonal menjadi 1
    static void eselonBeOne(double[][] M, int brs,int kol) {
        int eselonOrder = 0;
        double operand;
        int i = 0;
        //Matriks yg berada di kolom eselonOrder harus 1
        while(i < brs && eselonOrder < kol){
            if (M[i][eselonOrder] != 0){
                operand = (1 / M[i][eselonOrder]);
                operasiKaliBaris(M, kol, i, operand);
                
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
            //jika tidak ada baris dibawahnya yang tidak 0, dicari ke kolom selanjutnya
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
                tukar(M, kol, i, i+c);
            }

            //mengurangi baris sesuai faktor
            for (int j = 0; j < brs; j++) {
                if (i != j) {
                    double operand = M[j][k] / M[i][k];
                    System.out.println(operand);
                    operasiThdBaris(M, kol, j, i, operand);
                }
            }
            k++;
            
        }
        //leading one menjadi 1, kemudian print jawaban
        eselonBeOne(M, brs, kol);
        PrintAnswer(M, brs, kol);
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
    static void fillPivotSolution(double[][] M, double[] arr, int brs, int kol) {
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

    //mengoutput jawaban
    static void PrintAnswer(double[][] M, int brs, int kol) {
        double[] pivotValue = new double[kol-1];
        boolean pivot, leadingOneFound;
        int leadingOneIdx = 0;

        //jika tidak ada solusi, akan berhenti
        if (CheckForNoSolution(M, brs, kol)) {
            System.out.println("No Solution");
            return;
        }

        //mengisi array pivotValue
        fillPivotSolution(M, pivotValue, brs, kol);

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
}
