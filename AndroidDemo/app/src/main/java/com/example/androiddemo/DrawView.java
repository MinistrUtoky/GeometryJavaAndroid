package com.example.androiddemo;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.example.*;
import org.example.Circle;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class DrawView extends View
{
    public int[] redColoredShapesIndices;
    public ArrayList<IShape> shapes;
    private final Paint paint;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shapes = new ArrayList<>();
        paint = new Paint();
        redColoredShapesIndices = new int[] {-1, -1};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawLine(getWidth() / 2.0f, 0, getWidth() / 2.0f, getHeight(), paint);
        canvas.drawLine(0, getHeight() / 2.0f, getWidth(), getHeight() / 2.0f, paint);
        DrawShapes(canvas);
    }
    public void ClearCanvas(){
        Canvas emptyCanvas = new Canvas();
        emptyCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        shapes.clear();
        draw(emptyCanvas);
    }

    public void AddShape(IShape i)
    {
        shapes.add(i);
    }
    public String[] ShapeStrings(){
        return ShapesToStrings(shapes);
    }
    public static String[] ShapesToStrings(ArrayList<IShape> someShapes){
        String[] shapeStrings = new String[someShapes.size()];
        for (int i = 0; i < someShapes.size(); i++)
            shapeStrings[i] = someShapes.get(i).toString();
        return shapeStrings;
    }
    public double AreaOfShape(int index) {
        return shapes.get(index).square();
    }
    public double PerimeterOfShape(int index) {
        return shapes.get(index).square();
    }
    public static String ShapesToCSVText(ArrayList<IShape> someShapes){
        StringBuilder sb = new StringBuilder();
        for (IShape shape : someShapes) {
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                sb.append("Circle\n");
                sb.append("(Center:(" + circle.getP().getX()[0] + "," + circle.getP().getX()[1]
                        + ");Radius:" + circle.getR() + ")");
            }
            else if (shape instanceof Segment){
                Segment segment = (Segment) shape;
                sb.append("Segment\n");
                sb.append("[(" + segment.getStart().getX()[0] + "," + segment.getStart().getX()[1] +
                        ");(" + segment.getFinish().getX()[0] + "," + segment.getFinish().getX()[1] + ")]");
            }
            else {
                if (shape instanceof Polyline)
                    sb.append("Polyline\n[");
                else if (shape instanceof TGon)
                    sb.append("Triangle\n{");
                else if (shape instanceof Rectangle)
                    sb.append("Rectangle\n{");
                else if (shape instanceof Trapeze)
                    sb.append("Trapeze\n{");
                else if (shape instanceof QGon)
                    sb.append("Quadrilateral\n{");
                else if (shape instanceof NGon)
                    sb.append("Polygon\n{");
                else
                    throw new NullPointerException("Nonexistent type of shape");

                if (shape instanceof Polyline){
                    for (Point2D point : ((Polyline) shape).getP()){
                        if (point != ((Polyline) shape).getP(0)){
                            sb.append(";");
                        }
                        sb.append("(");
                        sb.append(point.getX()[0]);
                        sb.append(",");
                        sb.append(point.getX()[1]);
                        sb.append(")");
                    }
                    sb.append("]");
                }
                else {
                    for (Point2D point : ((NGon) shape).getP()){
                        if (point != ((NGon) shape).getP()[0]){
                            sb.append(";");
                        }
                        sb.append("(");
                        sb.append(point.getX()[0]);
                        sb.append(",");
                        sb.append(point.getX()[1]);
                        sb.append(")");
                    }
                    sb.append("}");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public String CanvasToCSVText(){
        return ShapesToCSVText(shapes);
    }
    public void PutInCanvas(ArrayList<String> s){
        shapes.clear();
        shapes = UnbundleCSVIntoShapesArray(s);
    }
    public static ArrayList<IShape> UnbundleCSVIntoShapesArray(ArrayList<String> s){
        ArrayList<IShape> newShapes = new ArrayList<IShape>();
        String[] specialSymbols = { "[", "{", "(", ")", "}", "]", "Center:", "Radius:"};
        String params = "";
        for (int i = 0; i < s.size() - 1; i+=2) {
            params = s.get(i+1);
            for (String sym : specialSymbols)
                params = String.join("", params.split(Pattern.quote(sym)));
            String[] points = params.split(";");
            Point2D[] point2DCollection = new Point2D[points.length];
            if (s.get(i).equals("Circle")){
                Point2D p = new Point2D(new double[]{Double.parseDouble(points[0].split(",")[0]),
                        Double.parseDouble(points[0].split(",")[1])
                });
                double r = Double.parseDouble(points[1]);
                org.example.Circle circle = new Circle(p, r);
                newShapes.add(circle);
            }
            else {
                if (s.get(i).equals("Segment")) {
                    Point2D p1 = new Point2D(new double[]{Double.parseDouble(points[0].split(",")[0]),
                            Double.parseDouble(points[0].split(",")[1])
                    });
                    Point2D p2 = new Point2D(new double[]{Double.parseDouble(points[1].split(",")[0]),
                            Double.parseDouble(points[1].split(",")[1])
                    });
                    Segment segment = new Segment(p1, p2);
                    newShapes.add(segment);
                }
                else {
                    for (int j = 0; j < points.length; j++){
                        point2DCollection[j] = new Point2D(new double[]{Double.parseDouble(points[j].split(",")[0]),
                                Double.parseDouble(points[j].split(",")[1])
                        });
                    }
                    if (s.get(i).equals("Polyline")) {
                        newShapes.add(new Polyline(point2DCollection));
                    }
                    else if (s.get(i).equals("Polygon")) {
                        newShapes.add(new NGon(point2DCollection));
                    }
                    else if (s.get(i).equals("Triangle")) {
                        newShapes.add(new TGon(point2DCollection));
                    }
                    else if (s.get(i).equals("Quadrilateral")) {
                        newShapes.add(new QGon(point2DCollection));
                    }
                    else if (s.get(i).equals("Rectangle")) {
                        newShapes.add(new Rectangle(point2DCollection));
                    }
                    else if (s.get(i).equals("Trapeze")) {
                        newShapes.add(new Trapeze(point2DCollection));
                    }
                }
            }
        }
        return newShapes;
    }
    public static ArrayList<IShape> UndbundleStringIntoShapesArray(String s)
    {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (String str : s.split("\n")){
            stringArrayList.add(str);
        }
        return UnbundleCSVIntoShapesArray(stringArrayList);
    }
    private void DrawShapes(Canvas canvas)
    {
        for (int i = 0; i < shapes.size(); i++) {
            if (redColoredShapesIndices[0] == i || redColoredShapesIndices[1] == i)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.BLACK);
            DrawShape(shapes.get(i), canvas);
        }
        invalidate();
    }

    private void DrawShape(IShape shape, Canvas canvas){
        if (shape instanceof NGon)
            DrawPolygon(canvas, ((NGon) shape).getP());
        else if (shape instanceof Polyline)
            DrawPolyline(canvas, ((Polyline) shape).getP());
        else if (shape instanceof Segment)
            DrawSegment(canvas, ((Segment) shape).getStart().getX(), ((Segment) shape).getFinish().getX());
        else if (shape instanceof org.example.Circle)
            DrawCircle(canvas, ((org.example.Circle) shape).getP().getX(), ((org.example.Circle) shape).getR());
    }

    private Segment DrawSegment(Canvas canvas, double[] start, double[] end)
    {
        canvas.drawLine((float) (getWidth() / 2 + start[0]),
                (float) (getHeight() / 2 - start[1]),
                (float) (getWidth() / 2 + end[0]),
                (float) (getHeight() / 2 - end[1]), paint);
        return new Segment(new Point2D(start), new Point2D(end));
    }
    private void DrawCircle(Canvas canvas, double[] center, double R)
    {
        canvas.drawCircle((float) (center[0] + getWidth() / 2), (float) (getHeight() / 2 - center[1]), (float) R, paint);
    }
    private void DrawPolyline(Canvas canvas, Point2D[] points)
    {
        for (int i = 0; i < points.length - 1; i++) {
            DrawSegment(canvas, points[i].getX(), points[i + 1].getX());
        }
    }
    private void DrawPolygon(Canvas canvas, Point2D[] points)
    {
        DrawPolyline(canvas, points);
        DrawSegment(canvas, points[points.length - 1].getX(), points[0].getX());
    }

    public double TotalPerimeter(){
        double P = 0;
        for (IShape shape : shapes)
            P += shape.length();
        return P;
    }
    public double TotalArea(){
        double S = 0;
        for (IShape shape : shapes)
            S += shape.square();
        return S;
    }
}
