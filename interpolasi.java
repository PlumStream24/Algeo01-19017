public class interpolasi {
    
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
    
}
