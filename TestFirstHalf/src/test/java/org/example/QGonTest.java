package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QGonTest {

    QGon qgon;
    double startX=0, startY=1;
    @BeforeEach
    void setUp(){
        Point2D[] points = new Point2D[4];
        for (int i = 0; i < 4; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startX;
            Point2D p = new Point2D(x);
            p = p.rot(2*Math.PI/4*i);
            points[i] = p;
        }
        qgon = new QGon(points);
    }

    @Test
    void square() {
        assertEquals(qgon.p[0].sub(qgon.p[1]).abs()*qgon.p[1].sub(qgon.p[2]).abs(),
                        qgon.square());
    }

    @Test
    void testToString() {
        assertTrue(qgon.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(qgon.toBson() instanceof Document);
    }
}