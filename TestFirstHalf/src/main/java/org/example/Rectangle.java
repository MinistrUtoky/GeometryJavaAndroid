package org.example;

import com.mongodb.BasicDBList;
import lombok.ToString;
import org.bson.Document;

@ToString(exclude="n", callSuper = true)
public class Rectangle extends QGon {
    public Rectangle(Point2D[] p) { super(p); }
    @Override public double square() {
        return Point2D.sub(p[1], p[0]).abs() * Point2D.sub(p[2], p[1]).abs();
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
        document.put("type", "Rectangle");
        document.put("points", points);
        return document;
    }
}
