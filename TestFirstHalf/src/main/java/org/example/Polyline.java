package org.example;

import com.mongodb.BasicDBList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

@ToString(exclude="n")
public class Polyline extends OpenFigure{
    @Getter
    private int n;
    @Getter @Setter
    private Point2D[] p;
    public Polyline(Point2D[] p) { n = p.length; this.p = p; }
    public Point2D getP(int i) { return p[i]; }
    public void setP(Point2D p, int i) { this.p[i] = p;}
    @Override public double length() {
        double l = 0;
        Point2D prevp = p[0];
        for (int i = 1; i<n; i++) { l += new Segment(prevp, p[i]).length(); prevp = p[i]; }
        return l;
    }
    @Override public Polyline shift(Point2D a) { for (Point2D e : p) e.add(a); return this; }
    @Override public Polyline rot(double phi) { for (Point2D e : p) e.rot(phi); return this; }
    @Override public Polyline symAxis(int i) { for (Point2D e : p) e.symAxis(i); return this; }
    @Override public boolean cross(IShape i) {
        Point2D prev = p[0];
        for (Point2D pt : p)
        {
            if (new Segment(prev, pt).cross(i)) return true;
            prev = pt;
        }
        return false;
    }

    @Override
    public Document toBson(){
        org.bson.Document document = new Document();
        BasicDBList points = new BasicDBList();
        for (int i = 0; i < p.length; i++) {
            BasicDBList point = new BasicDBList();
            point.add(p[i].x[0]); point.add(p[i].x[1]);
            points.add(point);
        }
        document.put("type", "Polyline");
        document.put("points", points);
        return document;
    }
}
