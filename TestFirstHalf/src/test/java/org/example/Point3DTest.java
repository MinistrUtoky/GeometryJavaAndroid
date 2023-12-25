package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {
    Point3D point;
    double x=1,y=1,z=1;
    @BeforeEach
    void setUp() {
        double[] p = new double[3];
        p[0]=x;p[1]=y;p[2]=z;
        point = new Point3D(p);
    }
    @Test
    void cross_prod() {
        double[] p = new double[3];
        p[0]=x;p[1]=y;p[2]=z;
        Point3D otherPoint = new Point3D(); otherPoint.setX(p);
        assertArrayEquals(new double[3], Point3D.cross_prod(point, otherPoint).x);
    }

    @Test
    void testCross_prod() {
        double[] p = new double[3];
        p[0]=x;p[1]=y;p[2]=z;
        assertArrayEquals(new double[3], point.cross_prod(new Point3D(p)).x);
    }

    @Test
    void mix_prod() {
        double[] p1 = new double[3];
        p1[0]=x;p1[1]=0;p1[2]=0;
        double[] p2 = new double[3];
        p2[0]=0;p2[1]=y;p2[2]=0;
        assertEquals(x*y*z, Point3D.mix_prod(point, new Point3D(p1), new Point3D(p2)));
    }

    @Test
    void testMix_prod() {
        double[] p1 = new double[3];
        p1[0]=x;p1[1]=0;p1[2]=0;
        double[] p2 = new double[3];
        p2[0]=0;p2[1]=y;p2[2]=0;
        assertEquals(Point3D.mix_prod(point, new Point3D(p1), new Point3D(p2)),
                    point.mix_prod(new Point3D(p1), new Point3D(p2)));
    }
    @Test
    void testToBson() {
        assertTrue(point.toBson() instanceof Document);
    }
}