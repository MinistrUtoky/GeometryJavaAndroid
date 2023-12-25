package com.example.androiddemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.androiddemo.databinding.ActivityRemoveFigureBinding;
import com.google.android.material.snackbar.Snackbar;

import org.example.IShape;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveFigureActivity extends AppCompatActivity {
    private ArrayList<IShape> shapes;
    private ActivityRemoveFigureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRemoveFigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String canvas = (String) getIntent().getSerializableExtra("Canvas");

        shapes = DrawView.UndbundleStringIntoShapesArray(canvas);

        ArrayList<String> shapeStrings = new ArrayList<>(Arrays.asList(DrawView.ShapesToStrings(shapes)));
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shapeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        binding.RemoveFigureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() == null)
                    Toast.makeText(getApplicationContext(), "No figure selected", Toast.LENGTH_SHORT).show();
                else{
                    shapes.remove(spinner.getSelectedItemPosition());
                    shapeStrings.remove(spinner.getSelectedItemPosition());
                    adapter.notifyDataSetChanged();
                    spinner.setAdapter(adapter);
                    Snackbar.make(view, "Item removed", Snackbar.LENGTH_SHORT)
                            .setGestureInsetBottomIgnored(true)
                            .setAction("Action", null).show();
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
    }
}