package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point2DTest {

    Point2D point;
    double x=1,y=1;
    @BeforeEach
    void setUp() {
        double[] p = new double[2];
        p[0]=x;p[1]=y;
        point = new Point2D(p);
    }

    @Test
    void rot() {
        assertAll("Rotated",
                () -> assertEquals(-x, Math.round(Point2D.rot(point,Math.PI/2).x[0]*100.0)/100.0),
                () -> assertEquals(y, Math.round(Point2D.rot(point,Math.PI/2).x[1]*100.0)/100.0),
                () -> assertEquals(-x, Math.round(Point2D.rot(point,Math.PI).x[0]*100.0)/100.0),
                () -> assertEquals(-y, Math.round(Point2D.rot(point,Math.PI).x[1]*100.0)/100.0),
                () -> assertEquals(x, Math.round(Point2D.rot(point,Math.PI*3/2).x[0]*100.0)/100.0),
                () -> assertEquals(-y, Math.round(Point2D.rot(point,Math.PI*3/2).x[1]*100.0)/100.0),
                () -> assertEquals(0, Math.round(Point2D.rot(new Point2D(),Math.PI/2).x[0]*100.0)/100.0),
                () -> assertEquals(0, Math.round(Point2D.rot(new Point2D(),Math.PI/2).x[1]*100.0)/100.0)
        );
    }

    @Test
    void testRot() {
        point.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(-x, Math.round(point.x[0]*100.0)/100.0),
                () -> assertEquals(y, Math.round(point.x[1]*100.0)/100.0)
        );
        point.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(-x, Math.round(point.x[0]*100.0)/100.0),
                () -> assertEquals(-y, Math.round(point.x[1]*100.0)/100.0)
        );
        point.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(x, Math.round(point.x[0]*100.0)/100.0),
                () -> assertEquals(-y, Math.round(point.x[1]*100.0)/100.0)
        );
    }

    @Test
    void testToBson() {
        assertTrue(point.toBson() instanceof Document);
    }
}