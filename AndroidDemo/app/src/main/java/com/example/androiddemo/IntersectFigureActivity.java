package com.example.androiddemo;

import android.content.Intent;
import android.os.Bundle;

import com.example.androiddemo.databinding.ActivityRemoveFigureBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androiddemo.databinding.ActivityIntersectFigureBinding;

import org.example.IShape;

import java.util.ArrayList;
import java.util.Arrays;

public class IntersectFigureActivity extends AppCompatActivity {
    private ArrayList<IShape> shapes;
    private ActivityIntersectFigureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIntersectFigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String canvas = (String) getIntent().getSerializableExtra("Canvas");

        shapes = DrawView.UndbundleStringIntoShapesArray(canvas);

        ArrayList<String> shapeStrings = new ArrayList<>(Arrays.asList(DrawView.ShapesToStrings(shapes)));
        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shapeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        ArrayList<String> shapeStrings2 = new ArrayList<>(Arrays.asList(DrawView.ShapesToStrings(shapes)));
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shapeStrings2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        binding.IntersectFigureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner1.getSelectedItem() == null || spinner2.getSelectedItem() == null)
                    Toast.makeText(getApplicationContext(), "Not all figures are selected", Toast.LENGTH_SHORT).show();
                else if (spinner1.getSelectedItem().toString().equals(spinner2.getSelectedItem().toString())) {
                    Toast.makeText(getApplicationContext(), "Items are the same", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent data = new Intent();
                    int[] redColoredIndices = new int[]{spinner1.getSelectedItemPosition(), spinner2.getSelectedItemPosition()};
                    data.putExtra("Coloring", redColoredIndices);
                    if (shapes.get(spinner1.getSelectedItemPosition()).cross(shapes.get(spinner2.getSelectedItemPosition())))
                        data.putExtra("Intersection", true);
                    else
                        data.putExtra("Intersection", false);
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
    }
}