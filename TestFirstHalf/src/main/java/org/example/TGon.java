package org.example;

import com.mongodb.BasicDBList;
import lombok.ToString;
import org.bson.Document;

@ToString(exclude="n", callSuper = true)
public class TGon extends NGon{
    public TGon(Point2D[] p) { super(p); }
    @Override public double square()
    {
        double ab = Point2D.sub(p[1], p[0]).abs(),
                bc = Point2D.sub(p[2], p[1]).abs(),
                ca = Point2D.sub(p[0], p[2]).abs(),
                pp = (ab + bc + ca)/2;
        return Math.sqrt(pp * (pp - ab)*(pp - bc)*(pp - ca));
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
        document.put("type", "TGon");
        document.put("points", points);
        return document;
    }
}
