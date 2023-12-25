package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TGonTest {

    TGon tgon;
    double startX=0, startY=1;
    @BeforeEach
    void setUp(){
        Point2D[] points = new Point2D[3];
        for (int i = 0; i < 3; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startY;
            Point2D p = new Point2D(x);
            p=p.rot(2*Math.PI/3*i);
            points[i] = p;
        }
        tgon = new TGon(points);
    }
    @Test
    void square() {
        double a = (Point.sub(tgon.p[0], tgon.p[1]).abs());
        double b = (Point.sub(tgon.p[2], tgon.p[1]).abs());
        assertEquals(Math.round(0.5*a*b*Math.sin(Math.PI/3)*100.0)/100.0,
                        Math.round(tgon.square()*100.0)/100.0);
    }

    @Test
    void testToString() {
        assertTrue(tgon.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(tgon.toBson() instanceof Document);
    }
}