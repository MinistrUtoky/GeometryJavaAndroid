package org.example;

import com.mongodb.BasicDBList;
import com.mongodb.client.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

@ToString
public class Circle implements IShape {
    @Getter @Setter
    private Point2D p;
    @Getter @Setter
    private double r;

    public Circle(Point2D p, double r) {
        this.p = p;
        this.r = r;
    }
    public double square() {
        return Math.PI * r * r;
    }

    public double length() {
        return 2 * Math.PI * r;
    }

    public IShape shift(Point2D a) {
        Point newP = Point.add(a, p);
        p = new Point2D(newP.getX());
        return this;
    }

    public IShape rot(double phi) {
        p = p.rot(phi);
        return this;
    }

    public IShape symAxis(int i) {
        p.symAxis(i);
        return this;
    }

    public boolean cross(IShape i) {
        if (i instanceof Circle) {
            if (Point2D.sub(getP(), ((Circle)i).getP()).abs() <= getR() + ((Circle)i).getR() &&
                Point2D.sub(getP(), ((Circle)i).getP()).abs() >= Math.abs(getR() - ((Circle)i).getR()))
                    return true;
            else
                return false;
        } else return i.cross(this);
    }

    @Override
    public Document toBson(){
        org.bson.Document document = new Document();
        BasicDBList point = new BasicDBList();
        point.add(p.x[0]); point.add(p.x[1]);
        document.put("type", "Circle");
        document.put("center", point);
        document.put("radius", r);
        return document;
    }
}