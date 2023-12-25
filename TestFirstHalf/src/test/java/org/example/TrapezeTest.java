package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrapezeTest {

    Trapeze trapeze;
    double startX=0, startY=1;
    double h=1;
    double upperParallel=1;
    double lowerParallel=2;
    @BeforeEach
    void setUp(){
        Point2D[] points = new Point2D[4];
        double[] x = new double[2];
        double[] x1 = new double[2];
        double[] x2 = new double[2];
        double[] x3 = new double[2];
        x[0] = startX; x[1] = startY;
        x1[0] = startX; x1[1] = startY;
        x2[0] = startX; x2[1] = startY;
        x3[0] = startX; x3[1] = startY;
        x1[0] += (lowerParallel-upperParallel)/2;
        x1[1] -= h;
        x2[0] -= (upperParallel + lowerParallel)/2;
        x2[1] -= h;
        x3[0] -= upperParallel;
        points[0] = new Point2D(x);
        points[1] = new Point2D(x1);
        points[2] = new Point2D(x2);
        points[3] = new Point2D(x3);
        trapeze = new Trapeze(points);
    }

    @Test
    void square() {
        assertEquals(h*Math.abs(upperParallel+lowerParallel)/2 ,Math.round(trapeze.square()*100)/100.0);
    }

    @Test
    void testToString() {
        assertTrue(trapeze.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(trapeze.toBson() instanceof Document);
    }
}