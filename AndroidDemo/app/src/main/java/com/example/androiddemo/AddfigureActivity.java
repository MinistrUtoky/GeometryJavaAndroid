package com.example.androiddemo;

import android.content.Intent;
import android.net.LinkAddress;
import android.os.Bundle;

import com.example.androiddemo.databinding.ActivityIntersectFigureBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.androiddemo.databinding.ActivityAddfigureBinding;

import org.example.*;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class AddfigureActivity extends AppCompatActivity {
    private ArrayList<IShape> shapes;
    private ActivityAddfigureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddfigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String canvas = (String) getIntent().getSerializableExtra("Canvas");

        shapes = DrawView.UndbundleStringIntoShapesArray(canvas);

        ArrayList<String> types = new ArrayList<>(Arrays.asList(new String[]{"Segment", "Circle",
                                                                "Polyline", "Polygon", "Triangle",
                                                                "Quadrilateral", "Rectangle",
                                                                "Trapeze"}));
        Spinner typeSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        LinearLayout paramsHolder = findViewById(R.id.figureValuesHolder);
        paramsHolder.setNestedScrollingEnabled(true);

        binding.AddFigureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeSpinner.getSelectedItem() == null)
                    Toast.makeText(getApplicationContext(), "Type not selected", Toast.LENGTH_SHORT).show();
                else{
                    if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("segment")){
                        try {
                            EditText X1, Y1, X2, Y2;
                            GridLayout P1, P2;
                            P1 = (GridLayout) paramsHolder.getChildAt(1);
                            P2 = (GridLayout) paramsHolder.getChildAt(3);
                            X1 = (EditText) P1.getChildAt(0); Y1 = (EditText) P1.getChildAt(1);
                            X2 = (EditText) P2.getChildAt(0); Y2 = (EditText) P2.getChildAt(1);
                            shapes.add(
                                    new Segment(
                                            new Point2D(
                                                    new double[]{
                                                            Double.parseDouble(String.valueOf(X1.getText())),
                                                            Double.parseDouble(String.valueOf(Y1.getText()))
                                                    }),
                                            new Point2D(
                                                    new double[]{
                                                            Double.parseDouble(String.valueOf(X2.getText())),
                                                            Double.parseDouble(String.valueOf(Y2.getText()))
                                                    })
                                    )
                            );
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("circle")){
                        try {
                            EditText X, Y, R;
                            GridLayout P;
                            P = (GridLayout) paramsHolder.getChildAt(1);
                            X = (EditText) P.getChildAt(0); Y = (EditText) P.getChildAt(1);
                            R = (EditText) paramsHolder.getChildAt(3);
                            shapes.add(
                                    new Circle(
                                            new Point2D(
                                                    new double[]{
                                                            Double.parseDouble(String.valueOf(X.getText())),
                                                            Double.parseDouble(String.valueOf(Y.getText()))
                                                    }),
                                                    Double.parseDouble(String.valueOf(R.getText()))
                                    )
                            );
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        try {
                            int start = 1;
                            if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("polyline")
                                || typeSpinner.getSelectedItem().toString().toLowerCase().equals("polygon")){
                                start = 3;
                            }
                            ArrayList<Point2D> pointCollection = new ArrayList<>();
                            for (int i = start; i < paramsHolder.getChildCount(); i+=2){
                                EditText X, Y;
                                GridLayout P;
                                P = (GridLayout) paramsHolder.getChildAt(i);
                                X = (EditText) P.getChildAt(0); Y = (EditText) P.getChildAt(1);
                                pointCollection.add(
                                        new Point2D(
                                                new double[]{
                                                        Double.parseDouble(String.valueOf(X.getText())),
                                                        Double.parseDouble(String.valueOf(Y.getText()))
                                                }
                                        ));
                            }
                            if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("polyline")){
                                shapes.add(
                                        new Polyline(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                            else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("polygon")){
                                shapes.add(
                                        new NGon(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                            else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("triangle")){
                                shapes.add(
                                        new TGon(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                            else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("quadrilateral")){
                                shapes.add(
                                        new QGon(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                            else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("rectangle")){
                                shapes.add(
                                        new Rectangle(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                            else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("trapeze")){
                                shapes.add(
                                        new Trapeze(pointCollection.toArray(new Point2D[0]))
                                );
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Successful augmentation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("Canvas", DrawView.ShapesToCSVText(shapes));
                setResult(RESULT_OK, data);
                finish();
            }
        });
        binding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    paramsHolder.removeAllViews();
                    if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("segment")){
                        TextView headline1 = NewHeadline("Start:");
                        paramsHolder.addView(headline1);

                        GridLayout pointsView = EmptyGridLayout(2, 1);

                        Pair<EditText, EditText> XY = NewPointInputFields();

                        pointsView.addView(XY.first);
                        pointsView.addView(XY.second);

                        paramsHolder.addView(pointsView);

                        TextView headline2 = NewHeadline("End:");
                        paramsHolder.addView(headline2);
                        GridLayout pointsView2 = EmptyGridLayout(2, 1);

                        Pair<EditText, EditText> XY2 = NewPointInputFields();

                        pointsView2.addView(XY2.first);
                        pointsView2.addView(XY2.second);

                        paramsHolder.addView(pointsView2);
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("circle")){
                        TextView headline1 = NewHeadline("Center:");
                        paramsHolder.addView(headline1);

                        GridLayout pointsView = EmptyGridLayout(2, 1);

                        Pair<EditText, EditText> XY = NewPointInputFields();

                        pointsView.addView(XY.first);
                        pointsView.addView(XY.second);

                        paramsHolder.addView(pointsView);

                        TextView headline2 = NewHeadline("Radius:");
                        paramsHolder.addView(headline2);
                        EditText radius = new EditText(binding.figureValuesHolder.getContext());
                        ViewGroup.LayoutParams rParams =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        radius.setLayoutParams(rParams);
                        radius.setHint("R");
                        paramsHolder.addView(radius);
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("polyline")
                            || typeSpinner.getSelectedItem().toString().toLowerCase().equals("polygon")){
                        TextView headline = NewHeadline("Number of points:");
                        paramsHolder.addView(headline);
                        EditText numOfPoints = new EditText(binding.figureValuesHolder.getContext());
                        ViewGroup.LayoutParams numOfPointsParams =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        numOfPoints.setLayoutParams(numOfPointsParams);
                        numOfPoints.setHint("N");
                        numOfPoints.setInputType(InputType.TYPE_CLASS_NUMBER);
                        paramsHolder.addView(numOfPoints);
                        numOfPoints.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {}

                            @Override
                            public void afterTextChanged(Editable s) {
                                int N;
                                try {
                                    N = Integer.parseInt(s.toString());
                                } catch (NumberFormatException e) {
                                    return;
                                }
                                while (paramsHolder.getChildCount() > 2){
                                    paramsHolder.removeViewAt(2);
                                }
                                for (int i = 0; i < N; i++){
                                    TextView headline = NewHeadline("Point " + (i+1) + ":");
                                    paramsHolder.addView(headline);
                                    GridLayout pointsView = EmptyGridLayout(2, 1);
                                    Pair<EditText, EditText> XY = NewPointInputFields();
                                    pointsView.addView(XY.first);
                                    pointsView.addView(XY.second);
                                    paramsHolder.addView(pointsView);
                                }
                            }
                        });
                        numOfPoints.setText("1");
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("triangle")){
                        for (int i = 0; i < 3; i++){
                            TextView headline = NewHeadline("Point " + (i+1) + ":");
                            paramsHolder.addView(headline);
                            GridLayout pointsView = EmptyGridLayout(2, 1);
                            Pair<EditText, EditText> XY = NewPointInputFields();
                            pointsView.addView(XY.first);
                            pointsView.addView(XY.second);
                            paramsHolder.addView(pointsView);
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("quadrilateral")){
                        for (int i = 0; i < 4; i++){
                            TextView headline = NewHeadline("Point " + (i+1) + ":");
                            paramsHolder.addView(headline);
                            GridLayout pointsView = EmptyGridLayout(2, 1);
                            Pair<EditText, EditText> XY = NewPointInputFields();
                            pointsView.addView(XY.first);
                            pointsView.addView(XY.second);
                            paramsHolder.addView(pointsView);
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("rectangle")){
                        for (int i = 0; i < 4; i++){
                            TextView headline = NewHeadline("Point " + (i+1) + ":");
                            paramsHolder.addView(headline);
                            GridLayout pointsView = EmptyGridLayout(2, 1);
                            Pair<EditText, EditText> XY = NewPointInputFields();
                            pointsView.addView(XY.first);
                            pointsView.addView(XY.second);
                            paramsHolder.addView(pointsView);
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("trapeze")){
                        for (int i = 0; i < 4; i++){
                            TextView headline = NewHeadline("Point " + (i+1) + ":");
                            paramsHolder.addView(headline);
                            GridLayout pointsView = EmptyGridLayout(2, 1);
                            Pair<EditText, EditText> XY = NewPointInputFields();
                            pointsView.addView(XY.first);
                            pointsView.addView(XY.second);
                            paramsHolder.addView(pointsView);
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private TextView NewHeadline(String headlineText){
        return NewHeadlineFor(binding.figureValuesHolder, headlineText);
    }
    public static TextView NewHeadlineFor(View view, String headlineText){
        ViewGroup.LayoutParams headlineParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView headline = new TextView(view.getContext());
        headline.setTextSize(16);
        headline.setLayoutParams(headlineParams);
        headline.setText(headlineText);
        return headline;
    }
    private Pair<EditText, EditText> NewPointInputFields(){
        return NewPointInputFieldsFor(binding.figureValuesHolder);
    }
    public static Pair<EditText, EditText> NewPointInputFieldsFor(View view){
        EditText X = new EditText(view.getContext());
        EditText Y = new EditText(view.getContext());
        X.setWidth(0); Y.setWidth(0);
        GridLayout.LayoutParams xParams =  new GridLayout.LayoutParams();
        GridLayout.LayoutParams yParams =  new GridLayout.LayoutParams();
        xParams.rowSpec = GridLayout.spec(0,GridLayout.FILL,1f);
        xParams.columnSpec = GridLayout.spec(0,GridLayout.FILL,1f);
        yParams.rowSpec = GridLayout.spec(0,GridLayout.FILL,1f);
        yParams.columnSpec = GridLayout.spec(1,GridLayout.FILL,1f);
        X.setLayoutParams(xParams); Y.setLayoutParams(yParams);
        X.setHint("X"); Y.setHint("Y");
        X.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        Y.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        return new Pair<>(X,Y);
    }
    private GridLayout EmptyGridLayout(int columns, int rows){
        return EmptyGridLayoutFor(binding.figureValuesHolder, columns, rows);
    }
    public static GridLayout EmptyGridLayoutFor(View view, int columns, int rows){
        GridLayout gridLayout = new GridLayout(view.getContext());
        ViewGroup.LayoutParams pointsViewParams =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        gridLayout.setLayoutParams(pointsViewParams);
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);
        return gridLayout;
    }
}