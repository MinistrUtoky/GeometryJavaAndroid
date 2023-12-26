package com.example.androiddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.realm_classes.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm threadRealm;
    private static final int REQUEST_CODE = 1234;
    private DrawView mainCanvas;

    private static final int SCHEMA_V_PREV = 3;
    private static final int SCHEMA_V_NOW = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Realm.init(this);

        setContentView(R.layout.activity_main);
        mainCanvas = findViewById(R.id.drawView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("Canvas")) {
                mainCanvas.shapes = DrawView.UndbundleStringIntoShapesArray((String) data.getSerializableExtra("Canvas"));
                data.removeExtra("Canvas");
            }
            if (data.hasExtra("Coloring")){
                mainCanvas.redColoredShapesIndices = (int[]) data.getSerializableExtra("Coloring");
                data.removeExtra("Coloring");
            }
            if (data.hasExtra("Intersection")){
                if ((boolean)data.getSerializableExtra("Intersection")==true)
                    SpawnInfoPopup(findViewById(R.id.drawView),"Shapes do intersect.");
                else
                    SpawnInfoPopup(findViewById(R.id.drawView),"Shapes do not intersect.");
                data.removeExtra("Intersection");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadRealm.close();
    }
    public static int getSchemaVNow() {
        return SCHEMA_V_NOW;
    }
    public void OpenFigureAdditionForm(View view){
        SwitchGridTo("addfigure");
    }
    public void OpenFigureRemovalForm(View view){
        SwitchGridTo("removefigure");
    }
    public void OpenIntersectionForm(View view){
        SwitchGridTo("intersectfigure");
    }
    public void OpenFigureMovementForm(View view){
        SwitchGridTo("movefigure");
    }
    public void SwitchGridTo(String newView)
    {
        Intent intent = null;
        if (newView=="addfigure")
            intent = new Intent(this, AddfigureActivity.class);
        else if (newView=="movefigure")
            intent = new Intent(this, MoveFigureActivity.class);
        else if (newView=="removefigure")
            intent = new Intent(this, RemoveFigureActivity.class);
        else if (newView=="intersectfigure")
            intent = new Intent(this, IntersectFigureActivity.class);

        if (intent != null){
            intent.putExtra("Canvas", mainCanvas.CanvasToCSVText());
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void Clear_Click(View view) {
        mainCanvas.ClearCanvas();
        Toast.makeText(getApplicationContext(), "Canvas cleared", Toast.LENGTH_SHORT).show();
    }

    public void ShapeArea_Click(View view) {
        if (mainCanvas.shapes.size()==0)
            Toast.makeText(getApplicationContext(), "No shapes", Toast.LENGTH_SHORT).show();
        else
            SpawnShapeStatPopup(view,"Area");
    }
    public void ShapePerimeter_Click(View view) {
        if (mainCanvas.shapes.size()==0)
            Toast.makeText(getApplicationContext(), "No shapes", Toast.LENGTH_SHORT).show();
        else
            SpawnShapeStatPopup(view,"Perimeter");
    }
    public void Area_Click(View view) {
        SpawnInfoPopup(view, "Total Area = " + mainCanvas.TotalArea());
    }
    public void Perimeter_Click(View view) {
        SpawnInfoPopup(view, "Total Perimeter = " + mainCanvas.TotalPerimeter());
    }

    public void SaveToDB_Click(View view) {
        try {
            SpawnInputPopup(view);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public void UploadFromDB_Click(View view) {
        try {
            SpawnImportPopup(view);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public void ShowSaveFileDialog(View view) {
        SaveFile();
    }
    public void ShowUploadFileDialog(View view) {
        UploadFile();
    }
    public void ShowSaveImageDialog(View view) {
        ShowSaveImgDialog();
    }

    private View SpawnPopup(int layout){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);
        return popupView;
    }
    private PopupWindow CreateFocusablePopupFromView(View popupView){
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(10);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return popupWindow;
    }

    public void SpawnInfoPopup(View view, String info) {
        View popupView = SpawnPopup(R.layout.info_popup);
        TextView tw = (TextView)popupView.findViewById(R.id.textView2);
        tw.setText(info);

        PopupWindow popupWindow = CreateFocusablePopupFromView(popupView);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void SpawnShapeStatPopup(View view, String perimeterOrArea) {
        View popupView = SpawnPopup(R.layout.some_spinner);

        String[] shapeStrings = mainCanvas.ShapeStrings();
        Spinner spinner = popupView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shapeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button b = popupView.findViewById(R.id.perimeterOrAreaButton);
        b.setText("Calculate " + perimeterOrArea);


        PopupWindow popupWindow = CreateFocusablePopupFromView(popupView);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                if (perimeterOrArea.toLowerCase().equals("area"))
                    SpawnInfoPopup(view,
                            "Item: " +
                                    spinner.getSelectedItem().toString().substring(0,
                                                                                    Math.min(22, spinner.getSelectedItem().toString().length()))
                                          + "\n\nArea: " + mainCanvas.AreaOfShape(spinner.getSelectedItemPosition()));
                else
                    SpawnInfoPopup(view,
                            "Item: " +
                                    spinner.getSelectedItem().toString().substring(0,
                                            Math.min(22, spinner.getSelectedItem().toString().length()))
                                    + "\n\nPerimeter: " + mainCanvas.PerimeterOfShape(spinner.getSelectedItemPosition()));
                Toast.makeText(getApplicationContext(), "Calculated", Toast.LENGTH_SHORT).show();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void ShowSaveImgDialog() {
            mainCanvas.setDrawingCacheEnabled(true);
            Bitmap bitmap = mainCanvas.getDrawingCache();
            File file = new File(getFilesDir(), "image.png");
            FileOutputStream ostream;
            try {file.createNewFile();
                ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.flush();
                ostream.close();

                Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            mainCanvas.destroyDrawingCache();
    }

    public void SaveFile() {
        File file = new File(getFilesDir(), "file.csv");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(mainCanvas.CanvasToCSVText());
            fileWriter.close();

            Toast.makeText(getApplicationContext(), "File saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public void UploadFile() {
        File file = new File(getFilesDir(), "file.csv");
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            ArrayList<String> s = new ArrayList<>();
            while ((aDataRow = fileReader.readLine()) != null) {
                s.add(aDataRow);
            }
            mainCanvas.PutInCanvas(s);
            fileReader.close();

            Toast.makeText(getApplicationContext(), "File uploaded", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void SpawnImportPopup(View view) {
        View popupView = SpawnPopup(R.layout.some_spinner);


        RealmConfiguration config = new RealmConfiguration.Builder().name("GeometryRealm.realm")
                .schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm geometryRealm = Realm.getInstance(config);
        RealmResults<geometry> allGeometry = geometryRealm.where(geometry.class).findAll();
        String[] geometries = new String[allGeometry.size()];
        for (int i = 0; i < allGeometry.size(); i++){
            geometries[i] = allGeometry.get(i).getName();
        }
        Spinner spinner = popupView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, geometries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button b = popupView.findViewById(R.id.perimeterOrAreaButton);
        b.setText("Select");

        PopupWindow popupWindow = CreateFocusablePopupFromView(popupView);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mainCanvas.UploadFromDB(getApplicationContext(),
                        allGeometry.get(spinner.getSelectedItemPosition()).getObjectIds());
                Toast.makeText(getApplicationContext(), "Retracted from DB", Toast.LENGTH_SHORT).show();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void SpawnInputPopup(View view) {
        View popupView = SpawnPopup(R.layout.input_popup);

        EditText editText = popupView.findViewById(R.id.inputField);

        Button b = popupView.findViewById(R.id.perimeterOrAreaButton);

        PopupWindow popupWindow = CreateFocusablePopupFromView(popupView);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mainCanvas.SaveToDB(getApplicationContext(), editText.getText().toString());
                Toast.makeText(getApplicationContext(), "Saved to DB", Toast.LENGTH_SHORT).show();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}