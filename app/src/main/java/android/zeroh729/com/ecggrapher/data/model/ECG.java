package android.zeroh729.com.ecggrapher.data.model;

public class ECG {
    double pv;
    double qv;
    double rv;
    double sv;
    double pdelta;
    double qdelta;
    double rdelta;

    public ECG(double pv, double qv, double rv, double sv, double pdelta, double qdelta, double rdelta, double sdelta) {
        this.pv = pv;
        this.qv = qv;
        this.rv = rv;
        this.sv = sv;
        this.pdelta = pdelta;
        this.qdelta = qdelta;
        this.rdelta = rdelta;
        this.sdelta = sdelta;
    }

    public double getPv() {

        return pv;
    }

    public double getQv() {
        return qv;
    }

    public double getRv() {
        return rv;
    }

    public double getSv() {
        return sv;
    }

    public double getPdelta() {
        return pdelta;
    }

    public double getQdelta() {
        return qdelta;
    }

    public double getRdelta() {
        return rdelta;
    }

    public double getSdelta() {
        return sdelta;
    }

    double sdelta;
}
