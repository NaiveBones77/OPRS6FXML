import java.util.ArrayList;

public class Satellite {
    double V2;
    double a, e, i, w0, lambda0, M0;
    Vector osc = new Vector(new double[6]);
    Vector x = new Vector(new double[3]);
    double mu=3.986004418e14;
    double t0;
    double t;
    ArrayList<Vector> coords;

    public Satellite(double a, double e, double i, double w0, double lambda0, double m0, double t0, double t) {
        this.a = a;
        this.e = e;
        this.i = Transition.fromGradToRad(i);;
        this.w0 = Transition.fromGradToRad(w0);
        this.lambda0 = Transition.fromGradToRad(w0);
        M0 = Transition.fromGradToRad(m0);
        this.osc = new Vector(new double[]{a, e, i, w0, lambda0, m0});
        this.t0 = t0;
        this.t = t;
        coords = new ArrayList<Vector>();
    }



    public void calcXYZ(double t)
    {
        double n=Math.sqrt(mu/Math.pow(a,3));
        double M=n*(t-t0) + M0;
        double xi = M +(2.*e-Math.pow(e,3)/4)*Math.sin(M) + 5./4*e*e*Math.sin(2.*M) +
                13./12*e*e*e*Math.sin(3.*M);
        double theta = xi + w0;
        double p = a*(1-e*e);
        double r = p/(1+e*Math.cos(xi));
        double Vr = Math.sqrt(mu/p)*e*Math.sin(xi);
        double Vn = Math.sqrt(mu/p)*(1 + e*Math.cos(xi));
        double x = r*(Math.cos(theta)*Math.cos(lambda0)-Math.cos(i)*Math.sin(theta)*Math.sin(lambda0));
        double y = r*(Math.cos(theta)*Math.sin(lambda0)+Math.cos(i)*Math.sin(theta)*Math.cos(lambda0));
        double z = r*Math.sin(i)*Math.sin(theta);
        V2 = Math.sqrt(mu*(2./r - 1./a));
        coords.add(new Vector(new double[]{
                x,
                y,
                z,
                x/r*Vr + (-Math.sin(theta)*Math.cos(lambda0) -  Math.cos(theta)*Math.sin(lambda0)*Math.cos(i))*Vn,
                y/r*Vr + (-Math.sin(theta)*Math.sin(lambda0) +  Math.cos(theta)*Math.cos(lambda0)*Math.cos(i))*Vn,
                z/r*Vr + Math.cos(theta)*Math.sin(i)*Vn,
        }));
    }
}
