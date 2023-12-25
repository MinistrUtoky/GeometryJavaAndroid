package org.example;

import org.bson.Document;

public class Point3D extends Point{
    public Point3D() { super(3); }

    public Point3D(double[] x) { super(3,x); }

    public static Point3D cross_prod(Point3D p1, Point3D p2) {
        return new Point3D(new double[]
        {   p1.x[1] * p2.x[2] - p1.x[2] * p2.x[1],
            p1.x[2] * p2.x[0] - p1.x[0] * p2.x[2],
            p1.x[0] * p2.x[1] - p1.x[1] * p2.x[0]}
        );
    }

    public Point3D cross_prod(Point3D p)
    {
        Point3D newPoint = cross_prod(this, p);
        x = newPoint.x;
        dim = newPoint.dim;
        return newPoint;
    }

    // (a*[b x c])
    public static double mix_prod(Point3D p1, Point3D p2, Point3D p3) {
        return mult(p1, cross_prod(p2, p3));
    }

    public double mix_prod(Point3D p1, Point3D p2) {
        return mix_prod(this, p1, p2);
    }

    @Override
    public Document toBson(){
        org.bson.Document document = new Document();
        document.put("type", "Point3D");
        document.put("x", x[0]);
        document.put("y", x[1]);
        document.put("z", x[2]);
        return document;
    }
}
