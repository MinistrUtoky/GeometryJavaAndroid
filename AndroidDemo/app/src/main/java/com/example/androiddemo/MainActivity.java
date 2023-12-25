package com.example.androiddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.example.Circle;
import org.example.Point2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private DrawView mainCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mainCanvas.AddShape(new Circle(new Point2D(new double[]{0,0}), 10));
    }

    public void UploadFromDB_Click(View view) {

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
        View popupView = SpawnPopup(R.layout.shape_stat_spinner);

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
    /*
    public void SavePresetToDatabase(String masterName) {
        try{
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("project");
            MongoCollection<Document> collection = mongoDatabase.getCollection("collection");

            Document searchQuery = new Document();
            searchQuery.put("master", masterName);
            FindIterable<Document> subCollection = collection.find(searchQuery);
            if (subCollection.cursor().hasNext()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error: The preset with such a name already exists!");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
                return;
            }

            for (IShape shape : shapesList){
                Document item = shape.toBson();
                item.put("master", masterName);
                collection.insertOne(item);
            }
      
    }

    public void UploadPresetFromDatabase(String masterName) {
        
            shapesList.clear();
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("project");
            MongoCollection<Document> collection = mongoDatabase.getCollection("collection");
            Document searchQuery = new Document();
            searchQuery.put("master", masterName);
            FindIterable<Document> cursor = collection.find(searchQuery);
            final MongoCursor<Document> cursorIterator = cursor.cursor();
            while (cursorIterator.hasNext()) {
                var figure = cursorIterator.next();
                if (figure.get("type").toString().equalsIgnoreCase("circle")){
                    ArrayList<Double> center = (ArrayList<Double>)figure.get("center");
                    double[] centerPoint = new double[center.size()];
                    for (int i = 0; i < center.size(); i++)
                        centerPoint[i] = center.get(i);
                    shapesList.add(new Circle(new Point2D(centerPoint), (Double) figure.get("radius")));
                }
                else if (figure.get("type").toString().equalsIgnoreCase("segment")){
                    ArrayList<Double> start = (ArrayList<Double>)figure.get("start"),
                                    finish = (ArrayList<Double>)figure.get("finish");
                    double[] startPoint = new double[start.size()],
                             finishPoint = new double[finish.size()];
                    for (int i = 0; i < start.size(); i++) startPoint[i] = start.get(i);
                    for (int i = 0; i < finish.size(); i++) finishPoint[i] = finish.get(i);
                    shapesList.add(new Segment(new Point2D(startPoint), new Point2D(finishPoint)));
                }
                else {
                    ArrayList<ArrayList<Double>> ps = (ArrayList<ArrayList<Double>>)figure.get("points");
                    double[][] points = new double[ps.size()][ps.get(0).size()];
                    for (int i = 0; i < ps.size(); i++) {
                        points[i] = new double[ps.get(i).size()];
                        for (int j = 0; j < ps.get(i).size(); j++)
                            points[i][j] = ps.get(i).get(j);
                    }
                    Point2D[] shapePoints = new Point2D[points.length];
                    for (int i = 0; i < points.length; i++)
                        shapePoints[i] = new Point2D(points[i]);
                    if (figure.get("type").toString().equalsIgnoreCase("ngon"))
                        shapesList.add(new NGon(shapePoints));
                    else if (figure.get("type").toString().equalsIgnoreCase("polyline"))
                        shapesList.add(new Polyline(shapePoints));
                    else if (figure.get("type").toString().equalsIgnoreCase("qgon"))
                        shapesList.add(new QGon(shapePoints));
                    else if (figure.get("type").toString().equalsIgnoreCase("rectangle"))
                        shapesList.add(new Rectangle(shapePoints));
                    else if (figure.get("type").toString().equalsIgnoreCase("tgon"))
                        shapesList.add(new TGon(shapePoints));
                    else if (figure.get("type").toString().equalsIgnoreCase("trapeze"))
                        shapesList.add(new Trapeze(shapePoints));
                }
                RedrawmainCanvas();
            }
      
    }

    public void SaveToDB_Click(ActionEvent actionEvent) {
        
            log.log(Level.INFO, "Creating shape info popup");
            VBox root = new VBox();
            TextField textField = new TextField();
            Button save = new Button("Save to database");
            save.setMaxSize(200, 20);
            Button cancel = new Button("Cancel");
            cancel.setMaxSize(200, 20);
            save.setDisable(true);
            textField.textProperty().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue observableValue, Object o, Object t1) {
                    save.setDisable(false);
                }
            });
            save.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                SavePresetToDatabase(textField.getText());
                ((Stage) cancel.getScene().getWindow()).close();
            });
            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                ((Stage) cancel.getScene().getWindow()).close();
            });
            root.getChildren().add(textField);
            root.getChildren().add(save);
            root.getChildren().add(cancel);

            Scene scene = new Scene(root, 200, 100);
            Stage stage = new Stage();
            stage.titleProperty().setValue("Saving preset to database");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
      
    }
    public void UploadFromDB_Click(ActionEvent actionEvent) {
        
            log.log(Level.INFO, "Creating shape info popup");
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("project");
            MongoCollection<Document> collection = mongoDatabase.getCollection("collection");

            VBox root = new VBox();
            ComboBox uploadableBox = new ComboBox();
            Button upload = new Button("Upload preset");
            upload.setMaxSize(200, 20);
            Button cancel = new Button("Cancel");
            cancel.setMaxSize(200, 20);

            var cursorIterator = collection.find().iterator();
            while (cursorIterator.hasNext()){
                var master = cursorIterator.next().get("master");
                if (!uploadableBox.getItems().contains(master))
                    uploadableBox.getItems().add(master);
            }
            uploadableBox.setMaxSize(200, 20);
            uploadableBox.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object t1) {
                    upload.setDisable(false);
                }
            });
            upload.setDisable(true);
            upload.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                UploadPresetFromDatabase(uploadableBox.getSelectionModel().getSelectedItem().toString());
                ((Stage) cancel.getScene().getWindow()).close();
            });
            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                ((Stage) cancel.getScene().getWindow()).close();
            });
            root.getChildren().add(uploadableBox);
            root.getChildren().add(upload);
            root.getChildren().add(cancel);

            Scene scene = new Scene(root, 200, 100);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
      
    }*/
}