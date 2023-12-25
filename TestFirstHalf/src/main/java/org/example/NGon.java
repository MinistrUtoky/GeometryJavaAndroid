package org.example;

import com.mongodb.BasicDBList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

@ToString(exclude="n")
public class NGon implements IShape {
    @Getter
    protected int n;
    @Getter @Setter
    protected Point2D[] p;
    public NGon(Point2D[] p) { this.p = p; n = p.length; }
    public void setP(Point2D p, int i) { this.p[i] = p;}
    public double square()
    {
        if (n > 3)
        {
            Point2D[] a = java.util.Arrays.copyOfRange(p, 0, n - 1);
            Point2D[] b = new Point2D[] { p[0], p[n - 2], p[n - 1] };
            return new NGon(a).square() + new TGon(b).square();
        }
        else if (n == 3) return new TGon(p).square();
        return 0;
    }

    public double length()
    {
        double l = 0;
        Point2D prevp = p[0];
        for (Point2D point : p) { l += new Segment(prevp, point).length(); prevp = point; }
        l += new Segment(p[0], p[n - 1]).length();
        return l;
    }

    public IShape shift(Point2D a) { for (int i = 0; i < n; i++) p[i].add(a); return this; }
    public IShape rot(double phi) { for (int i = 0; i < n; i++) p[i].rot(phi); return this; }
    public IShape symAxis(int i) { for (int j = 0; j < n; j++) p[j].symAxis(i); return this; }
    public boolean cross(IShape i)
    {
        Point2D prev = p[0];
        for (Point2D pt : p)
        {
            if (new Segment(prev, pt).cross(i)) return true;
            prev = pt;
        }
        if (new Segment(p[0], p[n - 1]).cross(i)) return true;
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
        document.put("type", "NGon");
        document.put("points", points);
        return document;
    }
}
