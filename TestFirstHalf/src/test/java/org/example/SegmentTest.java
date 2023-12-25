package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {

    Segment segment;
    double x1=0,y1=0,x2=1,y2=1;
    @BeforeEach
    void setUp() {
        double[] p1 = new double[2], p2 = new double[2];
        p1[0]=x1;p1[1]=y1;p2[0]=x2;p2[1]=y2;
        segment = new Segment(new Point2D(p1), new Point2D(p2));
    }

    @Test
    void getStart() {
        assertAll(
                () -> assertEquals(x1, segment.getStart().x[0]),
                () -> assertEquals(y1, segment.getStart().x[1])
        );
    }

    @Test
    void setStart() {
        segment.setStart(segment.getFinish());
        assertAll(
                () -> assertEquals(x2, segment.getStart().x[0]),
                () -> assertEquals(y2, segment.getStart().x[1])
        );
    }

    @Test
    void getFinish() {
        assertAll(
                () -> assertEquals(x2, segment.getFinish().x[0]),
                () -> assertEquals(y2, segment.getFinish().x[1])
        );
    }

    @Test
    void setFinish() {
        segment.setFinish(segment.getStart());
        assertAll(
                () -> assertEquals(x1, segment.getFinish().x[0]),
                () -> assertEquals(y1, segment.getFinish().x[1])
        );
    }

    @Test
    void length() {
        assertEquals(segment.getFinish().sub(segment.getStart()).abs(), segment.length());
    }

    @Test
    void shift() {
        segment.shift(segment.getFinish());
        assertAll(
                () -> assertEquals(x1+x2, segment.getStart().x[0]),
                () -> assertEquals(y1+y2, segment.getStart().x[1]),
                () -> assertEquals(x2+x2, segment.getFinish().x[0]),
                () -> assertEquals(y2+y2, segment.getFinish().x[1])
        );
    }

    @Test
    void rot() {
        segment.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(Math.abs(-x1), Math.abs(Math.round(segment.getStart().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(y1), Math.abs(Math.round(segment.getStart().x[1]*100.0)/100.0))
        );
        assertAll("Rotated",
                () -> assertEquals(Math.abs(-x2), Math.abs(Math.round(segment.getFinish().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(y2), Math.abs(Math.round(segment.getFinish().x[1]*100.0)/100.0))
        );
        segment.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(Math.abs(-x1), Math.abs(Math.round(segment.getStart().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(-y1), Math.abs(Math.round(segment.getStart().x[1]*100.0)/100.0))
        );
        assertAll("Rotated",
                () -> assertEquals(Math.abs(-x2), Math.abs(Math.round(segment.getFinish().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(-y2), Math.abs(Math.round(segment.getFinish().x[1]*100.0)/100.0))
        );
        segment.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(Math.abs(x1), Math.abs(Math.round(segment.getStart().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(-y1), Math.abs(Math.round(segment.getStart().x[1]*100.0)/100.0))
        );
        assertAll("Rotated",
                () -> assertEquals(Math.abs(x2), Math.abs(Math.round(segment.getFinish().x[0]*100.0)/100.0)),
                () -> assertEquals(Math.abs(-y2), Math.abs(Math.round(segment.getFinish().x[1]*100.0)/100.0))
        );
    }

    @Test
    void symAxis() {
        segment.symAxis(0);
        assertEquals(-y1, segment.getStart().x[1]);
        assertEquals(-y2, segment.getFinish().x[1]);
        segment.symAxis(1);
        assertEquals(-x1, segment.getStart().x[0]);
        assertEquals(-x2, segment.getFinish().x[0]);
    }

    @Test
    void cross() {
        double[] p1 = new double[2]; p1[0]=x2; p1[1]=y1;
        double[] p2 = new double[2]; p2[0]=x1; p2[1]=y2;
        double[] p3 = new double[2]; p3[0] = 3; p3[1] = 3;
        Segment segment2 = new Segment(new Point2D(p1), new Point2D(p2));
        Circle circle2 = new Circle(new Point2D(segment.getStart().x), segment.length());
        NGon triangle = new NGon(new Point2D[]{ new Point2D(p2), new Point2D(p3), new Point2D(p1) });
        Polyline not_triangle = new Polyline(new Point2D[]{ new Point2D(p1), new Point2D(p2), new Point2D(p3) });
        NGon dot = new NGon(new Point2D[]{ new Point2D(p3), new Point2D(p3), new Point2D(p3) });
        assertAll(
                () -> assertTrue(segment.cross(segment2)),
                () -> assertTrue(segment.cross(circle2)),
                () -> assertFalse(segment.cross(dot)),
                () -> assertTrue(segment.cross(triangle)),
                () -> assertTrue(segment.cross(not_triangle))
        );
    }

    @Test
    void counterclockwise() {
        double[] p1 = new double[2]; p1[0]=x2; p1[1]=y1;
        double[] p2 = new double[2]; p2[0]=x1; p2[1]=y2;
        Segment segment2 = new Segment(new Point2D(p1), new Point2D(p2));
        assertAll(
                () -> assertNotEquals(
                        segment.counterclockwise(segment.getStart(), segment2.getStart(), segment2.getFinish()),
                        segment.counterclockwise(segment.getFinish(), segment2.getStart(), segment2.getFinish())),
                () -> assertNotEquals(
                        segment.counterclockwise(segment.getStart(), segment.getFinish(), segment2.getStart()),
                        segment.counterclockwise(segment.getStart(), segment.getFinish(), segment2.getFinish()))
        );
    }
    @Test
    void square(){
        assertEquals(0, segment.square());
    }
    @Test
    void testToString() {
        assertTrue(segment.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(segment.toBson() instanceof Document);
    }
}