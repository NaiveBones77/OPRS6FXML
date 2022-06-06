import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AnomalGP extends Model {

    ArrayList<Vector> listSferical = new ArrayList<>();
    ArrayList<Double> arrayList = new ArrayList<>();
    ArrayList<Vector> tm60 = new ArrayList<>();

    double omega = 7.292115e-5;
    double nu=3.986004418e14;
    double R = 6371.3e3;

    double J2 = 1082.62575e-6;
    double J4 = -2.37089e-6;
    double J6 = 6.08e-9;
    double J8 = -1.40e-11;
    double a = 6378136;

    Vector mass = new Vector(new double[]{});

    public AnomalGP(double t0, double t1, double h, Vector x0) throws Exception
    {
        super(t0, t1, h);
        result = new Matrix(new double[][]{
                {x0.data[0]},
                {x0.data[1]},
                {x0.data[2]},
                {x0.data[3]},
                {x0.data[4]},
                {x0.data[5]},
        });
        this.x0 = x0;
        setTM("C:\\Users\\maxso\\OneDrive\\Рабочий стол\\Уирс\\TM60.txt");
    }

    private void setTM(String path) throws Exception
    {
        File file = new File(path);

        Scanner scanner = new Scanner(file);
        ArrayList<Double> list = new ArrayList<>();
        int i = 0;

        while (scanner.hasNextLine())
        {
            String s = scanner.nextLine();
            String[] words = s.split("\t");
            for(String word : words)
            {
                arrayList.add(Double.valueOf(word));
            }
        }
        scanner.close();
        for (int j = 0; j < 60; j++) {
            tm60.add(new Vector(new double[]{1e-10*arrayList.get(j*4), 1e3*arrayList.get(j*4+1), 1e3*arrayList.get(j*4+2), 1e3*arrayList.get(j*4+3)}));
        }
    }



    double getC(int n)
    {
        if (n==2)
            return -J2/Math.sqrt(2.*n+1);
        else if (n==4)
            return -J4/Math.sqrt(2.*n+1);
        else if (n==6)
            return -J6/Math.sqrt(2.*n+1);
        else if (n==8)
            return -J8/Math.sqrt(2.*n+1);
        else return 0;
    }

    private double LegandrP(double fi, int n, int m)
    {

        if (n == 0 && m ==0)
        {
            return 1;
        }
        else if (n < m)
        {
            return 0;
        }
        else if (n > m)
        {
            double a = LegandrP(fi, n-1, m)*Math.sin(fi)*Math.sqrt((4.*n*n-1) / (n*n - m*m));
            double b = LegandrP(fi, n-2, m)*Math.sqrt(( Math.pow(n-1, 2) - m*m)*(2.*n+1)/(n*n - m*m)/(2.*n-3));
            return a-b;
        }
        else if (n == m && m!=0 && n!=0)
        {
            double a = LegandrP(fi, n-1, m-1)*Math.cos(fi)*Math.sqrt((2.*n+1) / (2.*n*delta(m-1)));
            return a;
        }
        else
            return 0;
    }

    private double getdP(double fi, int n)
    {
        double tmp = LegandrP(fi, n, 1);
        return Math.sqrt(0.5*n*(n+1))*LegandrP(fi, n, 1);
    }
    private double delta(int k)
    {
        if (k==0)
        {
            return 0.5;
        }
        else return 1.;
    }

    
    @Override
    public Vector getRight(Vector vec, double t)
    {
        Vector deltagx = new Vector(new double[3]);
        Vector sferical = new Vector(new double[3]);
        double norm = Math.sqrt(Math.pow(vec.data[0], 2) + Math.pow(vec.data[1], 2)
                + Math.pow(vec.data[2], 2));
        double r0 = Math.sqrt(Math.pow(vec.data[0], 2) + Math.pow(vec.data[1], 2));
        Vector V = new Vector(new double[]{vec.data[3], vec.data[4], vec.data[5]});
        Vector X = new Vector(new double[]{vec.data[0], vec.data[1], vec.data[2]});
        double sum = 0;

        double ro = Math.sqrt(Math.pow(vec.data[0], 2) + Math.pow(vec.data[1], 2)
                +Math.pow(vec.data[2], 2));
        double fi = Math.atan2(vec.data[2] , Math.sqrt(Math.pow(vec.data[0], 2) + Math.pow(vec.data[1], 2)));
        double lambda0 = Math.atan2(x0.data[1], x0.data[0]);

        for (int i = 2; i <= 8; i+=2) {
            sum+=(i+1)*Math.pow(a/ro,i)*getC(i)*LegandrP(fi, i, 0);
        }

        double d2ro = -nu/Math.pow(ro, 2)- nu/Math.pow(ro, 2)*sum;

        sum = 0;
        for (int i = 2; i <= 8; i+=2) {
            double tm1 = getC(i);
            double tm2 = getdP(fi, i);
            sum+=Math.pow(a/ro, i)*getC(i)*getdP(fi, i);
        }

        double d2fi = nu/Math.pow(ro, 2)*sum;

        double d2lambda = 0;
        sferical.data[0] = d2ro;
        sferical.data[1] = d2fi;
        sferical.data[2] = d2lambda;
        listSferical.add(sferical);
        Matrix m1 = new Matrix(new double[][]{
                {vec.data[0]/norm, -vec.data[0]*vec.data[2]/(norm*r0), -vec.data[1]/r0},
                {vec.data[1]/norm, -vec.data[1]*vec.data[2]/(norm*r0), vec.data[0]/r0},
                {vec.data[2]/norm, r0/norm, 0}
        });
        V = m1.mult(sferical);

        for (int i = 0; i < 60; i++) {
            Vector T = tm60.get(i);
            double dist = Math.sqrt(Math.pow(vec.data[0] - T.data[1], 2) +
                    Math.pow(vec.data[1] - T.data[2], 2) + Math.pow(vec.data[2] - T.data[3], 2));
            deltagx.data[0]+=-nu*T.data[0]*(vec.data[0] - T.data[1])/Math.pow(dist, 3);
            deltagx.data[1]+=-nu*T.data[0]*(vec.data[1] - T.data[2])/Math.pow(dist, 3);
            deltagx.data[2]+=-nu*T.data[0]*(vec.data[2] - T.data[3])/Math.pow(dist, 3);
        }
        Vector res = new Vector(new double[]{
                vec.data[3],
                vec.data[4],
                vec.data[5],
                V.data[0] + deltagx.data[0],
                V.data[1] + deltagx.data[1],
                V.data[2] + deltagx.data[2]
        });
        return res;
    }
}
