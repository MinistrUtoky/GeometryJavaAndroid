package org.example;

import com.mongodb.BasicDBList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

@ToString
public class Segment extends OpenFigure{
    @Getter @Setter
    private Point2D start;
    @Getter @Setter
    private Point2D finish;

    public Segment(Point2D s, Point2D f)
    {
        start = s;
        finish = f;
    }
    @Override public double length() { return Point2D.sub(finish, start).abs(); }
    @Override public Segment shift(Point2D a) { start.add(a); finish.add(a); return this; }
    @Override public Segment rot(double phi) { start.rot(phi); finish.rot(phi); return this; }
    @Override public Segment symAxis(int i) { start.symAxis(i); finish.symAxis(i); return this; }

    @Override public boolean cross(IShape i) {
        if (i instanceof Segment) {
            return counterclockwise(this.start, ((Segment)i).start, ((Segment)i).finish)
                    != counterclockwise(this.finish, ((Segment)i).start, ((Segment)i).finish) &&
                    counterclockwise(this.start, this.finish, ((Segment)i).start) !=
                    counterclockwise(this.start, this.finish, ((Segment)i).finish);
        }
        else if (i instanceof Circle) {
            return ((Point2D.sub(start, ((Circle)i).getP())).abs() < ((Circle)i).getR()
                    || (Point2D.sub(finish, ((Circle)i).getP())).abs() < ((Circle)i).getR())
                        && !((Point2D.sub(start, ((Circle)i).getP())).abs() < ((Circle)i).getR()
                    && (Point2D.sub(finish, ((Circle)i).getP())).abs() < ((Circle)i).getR());
        }
        else
        {
            Point2D[] pts = new Point2D[0];
            if (i instanceof Polyline)
                pts = ((Polyline)i).getP();
            else if (i instanceof NGon)
                pts = ((NGon)i).getP();
            Point2D prev = pts[0];
            for (Point2D pt : pts)
            {
                if (new Segment(prev, pt).cross(this))
                    return true;
                prev = pt;
            }
            if (!(i instanceof Polyline))
                if (new Segment(pts[0], pts[pts.length - 1]).cross(this))
                    return true;
            return false;
        }
    }

    public boolean counterclockwise(Point2D a, Point2D b, Point2D c) {
        return (c.getX()[1] - a.getX()[1]) * (b.getX()[0] - a.getX()[0])
                > (b.getX()[1] - a.getX()[1]) * (c.getX()[0] - a.getX()[0]);
        }
    @Override
    public Document toBson(){
        org.bson.Document document = new Document();
        BasicDBList p1 = new BasicDBList();
        BasicDBList p2 = new BasicDBList();
        p1.add(start.x[0]); p1.add(start.x[1]);
        p2.add(finish.x[0]); p2.add(finish.x[1]);
        document.put("type", "Segment");
        document.put("start", p1);
        document.put("finish", p2);
        return document;
    }
}
