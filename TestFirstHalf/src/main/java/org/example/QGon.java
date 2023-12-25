package org.example;

import com.mongodb.BasicDBList;
import lombok.ToString;
import org.bson.Document;

@ToString(exclude="n", callSuper = true)
public class QGon extends NGon {
    public QGon(Point2D[] p) { super(p); }
    @Override public double square() {
        Point2D[] a = new Point2D[] { p[0], p[1], p[2] };
        Point2D[] b = new Point2D[] { p[2], p[3], p[0] };
        return new TGon(a).square() + new TGon(b).square();
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
        document.put("type", "QGon");
        document.put("points", points);
        return document;
    }
}
