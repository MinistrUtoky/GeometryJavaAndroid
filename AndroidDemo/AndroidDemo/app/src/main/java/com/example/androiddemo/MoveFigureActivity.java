package com.example.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.databinding.ActivityMoveFigureBinding;

import org.example.IShape;
import org.example.Point2D;

import java.util.ArrayList;
import java.util.Arrays;

public class MoveFigureActivity extends AppCompatActivity {
    private ArrayList<IShape> shapes;
    private ActivityMoveFigureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMoveFigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String canvas = (String) getIntent().getSerializableExtra("Canvas");
        shapes = DrawView.UndbundleStringIntoShapesArray(canvas);

        ArrayList<String> shapeStrings = new ArrayList<>(Arrays.asList(DrawView.ShapesToStrings(shapes)));
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shapeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayList<String> types = new ArrayList<>(Arrays.asList(new String[]{"Translate", "Rotate", "Mirror against axis"}));
        Spinner typeSpinner = findViewById(R.id.movementTypeSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        LinearLayout movementHolder = findViewById(R.id.movementValuesHolder);
        movementHolder.setNestedScrollingEnabled(true);

        binding.MoveFigureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() == null)
                    Toast.makeText(getApplicationContext(), "No figure selected", Toast.LENGTH_SHORT).show();
                else if (typeSpinner.getSelectedItem() == null)
                    Toast.makeText(getApplicationContext(), "No movement type selected", Toast.LENGTH_SHORT).show();
                else{
                    if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("translate")){
                        try {
                            GridLayout g = (GridLayout) movementHolder.getChildAt(1);
                            EditText X = (EditText) g.getChildAt(0);
                            EditText Y = (EditText) g.getChildAt(1);
                            shapes.get(spinner.getSelectedItemPosition()).shift(
                                    new Point2D(
                                            new double[]{Double.parseDouble(String.valueOf(X.getText())),
                                                         Double.parseDouble(String.valueOf(Y.getText()))}));
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("rotate")){
                        try {
                            EditText angle = (EditText) movementHolder.getChildAt(1);
                            shapes.get(spinner.getSelectedItemPosition()).rot(Double.parseDouble(String.valueOf(angle.getText())));
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("mirror against axis")){
                        try {
                            Spinner axis = (Spinner) movementHolder.getChildAt(1);
                            shapes.get(spinner.getSelectedItemPosition()).symAxis(axis.getSelectedItemPosition());
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Shape succsessfully altered", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("Canvas", DrawView.ShapesToCSVText(shapes));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                movementHolder.removeAllViews();
                if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("translate")){
                    TextView headline = AddfigureActivity.NewHeadlineFor(binding.movementValuesHolder, "Shift vector:");
                    movementHolder.addView(headline);
                    GridLayout pointsView = AddfigureActivity.EmptyGridLayoutFor(binding.movementValuesHolder,2, 1);
                    Pair<EditText, EditText> XY2 = AddfigureActivity.NewPointInputFieldsFor(binding.movementValuesHolder);

                    pointsView.addView(XY2.first);
                    pointsView.addView(XY2.second);

                    movementHolder.addView(pointsView);
                }
                else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("rotate")){
                    TextView headline = AddfigureActivity.NewHeadlineFor(binding.movementValuesHolder, "Rotation angle (counterclockwise):");
                    movementHolder.addView(headline);
                    EditText angle = new EditText(binding.movementValuesHolder.getContext());
                    ViewGroup.LayoutParams aParams =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    angle.setLayoutParams(aParams);
                    angle.setHint("Radians");
                    angle.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    movementHolder.addView(angle);
                }
                else if (typeSpinner.getSelectedItem().toString().toLowerCase().equals("mirror against axis")){
                    TextView headline = AddfigureActivity.NewHeadlineFor(binding.movementValuesHolder, "Axis:");
                    movementHolder.addView(headline);
                    ArrayList<String> xy = new ArrayList<>(Arrays.asList(new String[]{"x", "y"}));
                    Spinner xySpinner = new Spinner(binding.movementValuesHolder.getContext());
                    xySpinner.setMinimumHeight(30);
                    ArrayAdapter<String> xyAdapter = new ArrayAdapter(binding.movementValuesHolder.getContext(),
                                                                    android.R.layout.simple_spinner_item, xy);
                    xyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    xySpinner.setAdapter(xyAdapter);
                    movementHolder.addView(xySpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}