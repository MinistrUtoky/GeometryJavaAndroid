package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NGonTest {

    NGon ngon;
    double startX=0, startY=1;
    int size = 5;

    Point2D[] NewRegularPointCollection(int newSize){
        Point2D[] points = new Point2D[newSize];
        for (int i = 0; i < newSize; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startY;
            Point2D p = new Point2D(x);
            p=p.rot(2*Math.PI/newSize*i);
            points[i] = p;
        }
        return points;
    }
    @BeforeEach
    void setUp(){
        Point2D[] points = new Point2D[size];
        for (int i = 0; i < size; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startY;
            Point2D p = new Point2D(x);
            p=p.rot(2*Math.PI/size*i);
            points[i] = p;
        }
        ngon = new NGon(points);
    }

    @Test
    void getN() {
        assertEquals(size, ngon.getN());
    }

    @Test
    void getP() {
        Point2D[] points = NewRegularPointCollection(size);
        for (int i = 0; i < size; i++)
            assertArrayEquals(ngon.getP()[i].x, points[i].x);
    }

    @Test
    void setP() {
        Point2D[] points = NewRegularPointCollection(size-1);
        ngon.setP(points);
        for (int i = 0; i < size-1; i++)
            assertArrayEquals(ngon.getP()[i].x, points[i].x);
    }

    @Test
    void testSetP() {
        Point2D[] points = NewRegularPointCollection(size);
        for (int i = 0; i < size-1; i++)
            ngon.setP(points[i] ,i);
        for (int i = 0; i < size-1; i++)
            assertArrayEquals(ngon.getP()[i].x, points[i].x);
    }

    @Test
    void square() {
        TGon tgon = new TGon(NewRegularPointCollection(3));
        NGon ngon1 = new NGon(NewRegularPointCollection(3));
        Rectangle rect = new Rectangle(NewRegularPointCollection(4));
        NGon ngon2 = new NGon(NewRegularPointCollection(4));
        NGon ngon3 = new NGon(NewRegularPointCollection(5));
        NGon ngon4 = new NGon(NewRegularPointCollection(6));
        assertAll(
                () -> assertEquals(Math.round(tgon.square()*100.0)/100.0,
                                    Math.round(ngon1.square()*100.0)/100.0),
                () -> assertEquals(Math.round(rect.square()*100.0)/100.0,
                                    Math.round(ngon2.square()*100.0)/100.0),
                () -> assertEquals(Math.round(0.25*Math.sqrt(5*(5+2*Math.sqrt(5)))*Math.pow(Point.sub(ngon3.getP()[1],                                 ngon3.getP()[0]).abs(), 2)*100.0)/100.0,
                                        Math.round(ngon3.square()*100.0)/100.0),
                () -> assertEquals(Math.round(0.5*3*Math.sqrt(3)*Math.pow(Point.sub(ngon4.getP()[1],
                                                    ngon4.getP()[0]).abs(), 2)*100.0)/100.0,
                                            Math.round(ngon4.square()*100.0)/100.0),
                () -> assertEquals(0, new NGon(NewRegularPointCollection(2)).square())
        );
    }

    @Test
    void length() {
        double l = 0;
        for (int i = 1; i < size; i++) {
            l += Point.sub(ngon.getP()[i], ngon.getP()[i-1]).abs();
        }
        l += Point.sub(ngon.getP()[0], ngon.getP()[ngon.getP().length-1]).abs();
        assertEquals(l, ngon.length());
    }

    @Test
    void shift() {
        Point2D[] points = NewRegularPointCollection(size);
        ngon.shift(ngon.getP()[0]);
        for (int i = 0; i < size; i++){
            points[i].x[0] += points[0].x[0];
            points[i].x[1] += points[0].x[1];
        }
        for (int i = 0; i < size; i++)
            assertArrayEquals(ngon.getP()[i].x, points[i].x);
    }

    @Test
    void rot() {
        Point2D[] points = NewRegularPointCollection(size);
        for (int i = 0; i < size; i++)
            points[i].rot(Math.PI/2);
        ngon.rot(Math.PI/2);
        for (int i = 0; i < size; i++)
            assertArrayEquals(ngon.getP()[i].x, points[i].x);
    }

    @Test
    void symAxis() {
        Point2D[] points = NewRegularPointCollection(size);
        Polyline polyline2 = new Polyline(points);
        ngon.symAxis(0);
        ngon.symAxis(1);
        for (int i = 0; i < size; i++) {
            assertEquals(-polyline2.getP(i).x[1], ngon.getP()[i].x[1]);
            assertEquals(-polyline2.getP(i).x[0], ngon.getP()[i].x[0]);
        }
    }

    @Test
    void cross() {
        double[] p1 = new double[2], p2 = new double[2];
        p1[0]=0; p1[1]=0;
        p2[0]=startY; p2[1]=startX;
        Segment segment = new Segment(new Point2D(p1.clone()), new Point2D(p2.clone()));
        p2[1] = startY;
        Segment segment2 = new Segment(new Point2D(p1.clone()), new Point2D(p2.clone()));
        double[] x = new double[2]; x[0]=startX*10; x[1]=startY*10;
        Circle circle2 = new Circle(ngon.getP()[1], Point.sub(ngon.getP()[1], ngon.getP()[0]).abs());
        circle2.shift(new Point2D(x));
        assertAll(
                () -> assertTrue(ngon.cross(segment)),
                () -> assertFalse(ngon.cross(circle2)),
                () -> assertTrue(ngon.cross(segment2))
        );
    }

    @Test
    void testToString() {
        assertTrue(ngon.toString() instanceof String);
    }
    @Test
    void testToBson() {
        assertTrue(ngon.toBson() instanceof Document);
    }
}