package com.pelotheban.insanehistoryquiz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ExcelVillain extends AppCompatActivity {

    // Needed for upload to Firebase


    private static final String TAG = "ImportShit";

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;

    private File[] listFile;
    File file;

    // Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ArrayList<ZZZjcVillainsExcel> uploadData;
    ListView lvInternalStorage;

    private CoordinatorLayout loutExcelVillainX; //primarily used for snackbars


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_villain);

        loutExcelVillainX = findViewById(R.id.loutExcelVillain);

        // Toast.makeText(Excel.this, collectionID, Toast.LENGTH_LONG).show();

        lvInternalStorage = (ListView) findViewById(R.id.lvDeviceStorage);
        //btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        //btnSDCard = (Button) findViewById(R.id.btnViewSDCard);

        uploadData = new ArrayList<>();

        //need to check the permissions

        checkFilePermissions();

        ////// to go right into memory

        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){

                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.

                    readExcelData(lastDirectory);

                }else

                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));

                    checkInternalStorage();

                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));

                }

            }

        });


        //Goes up one directory level
        FloatingActionButton fab = findViewById(R.id.fabBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count == 0){

                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");

                }else{

                    pathHistory.remove(count);
                    count--;

                    checkInternalStorage();

                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));

                }
            }
        });
    }

    ///////////////////////////////////// END --------------> ON-CREATE ///////////////////////////////////////

    private void checkFilePermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number

            }

        }else{

            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }

    }

    private void populateListView() {

        count = 0;
        pathHistory = new ArrayList<String>();
        pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
        Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
        checkInternalStorage();

    }

    private void checkInternalStorage() {

        Log.d(TAG, "checkInternalStorage: Started.");

        try{

            if (!Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {

                toastMessage("No SD card found.");

            }

            else{


                // Locate the image folder in your SD Car;d

                file = new File(pathHistory.get(count));

                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));

            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings

            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings

            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {

                // Get the path of the image file

                FilePathStrings[i] = listFile[i].getAbsolutePath();

                // Get the name image file

                FileNameStrings[i] = listFile[i].getName();

            }

            for (int i = 0; i < listFile.length; i++)

            {

                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);

            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){

            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1001) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                populateListView();

            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void readExcelData(String filePath) {

        Log.d(TAG, "readExcelData: Reading Excel File.");


        //declare input file
        File inputFile = new File(filePath);

        try {

            InputStream inputStream = new FileInputStream(inputFile);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowsCount = sheet.getPhysicalNumberOfRows();

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {

                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();

                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {

                    //handles if there are to many columns on the excel sheet.

                    String value = getCellAsString(row, c, formulaEvaluator);

                    String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;

                    Log.d(TAG, "readExcelData: Data from row: " + cellInfo);

                    sb.append(value + ",,"); // need 2 : or , to not confuse with : or , actually in the text



                }

                sb.append("::"); // need 2 : or , to not confuse with : or , actually in the text

            }

            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb);



        }catch (FileNotFoundException e) {

            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );

        } catch (IOException e) {

            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );

        }
    }

    /**

     * Method for parsing imported data and storing in ArrayList<XYValue>

     */

    public void parseStringBuilder(StringBuilder mStringBuilder){

        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split("::"); // need 2 : or , to not confuse with : or , actually in the text

        //Add to the ArrayListrow by row
        for(int i=0; i<rows.length; i++) {

            //Split the columns of the rows
            String[] columns = rows[i].split(",,"); // need 2 : or , to not confuse with : or , actually in the text

            //use try catch to make sure there are no "" that try to parse into doubles.
            try{

                String aaavillainid = columns[0];
                String bbbcorrectvaillainansw = columns[1];
                String cccwrongvillainans1 = columns[2];
                String dddwrongvillainans2 = columns[3];
                String eeewrongvillainans3 = columns[4];
                String fffvillainexpanded = columns[5];


                String cellInfo = "(aaavillainid, bbbcorrectvaillainansw, cccwrongvillainans1, dddwrongvillainans2, eeewrongvillainans3, fffvillainexpanded)" +
                        ": (" + aaavillainid + "," + bbbcorrectvaillainansw + ", " + cccwrongvillainans1 + ", " + dddwrongvillainans2 + ", " + eeewrongvillainans3 + ", " + fffvillainexpanded + ")";

                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new ZZZjcVillainsExcel(aaavillainid, bbbcorrectvaillainansw, cccwrongvillainans1, dddwrongvillainans2, eeewrongvillainans3, fffvillainexpanded));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();

    }
    private void printDataToLog() {

        Log.d(TAG, "printDataToLog: Printing data to log...");

        for(int i = 0; i< uploadData.size(); i++){

            String aaavillainid = uploadData.get(i).getAaavillainid();
            String bbbcorrectvaillainansw = uploadData.get(i).getBbbcorrectvaillainansw();
            String cccwrongvillainsans1 = uploadData.get(i).getCccwrongvillainans1();
            String dddwrongvillainsans2 = uploadData.get(i).getDddwrongvillainans2();
            String eeewrongvillainsans3 = uploadData.get(i).getEeewrongvillainans3();
            String fffvillainexpanded = uploadData.get(i).getFffvillainexpanded();



            final int questionCounter = i +1; //coin counter for toast


            Log.d(TAG, "printDataToLog: (aaavillainid, bbbcorrectvaillainansw, cccwrongvillainsans1, dddwrongvillainsans2, eeewrongvillainsans3, fffpaintexpanded): (" + aaavillainid + "," + bbbcorrectvaillainansw + ", " + cccwrongvillainsans1 + ", " + dddwrongvillainsans2 + ", " + eeewrongvillainsans3 + ")");

            String uid = FirebaseAuth.getInstance().getUid();

            //////// this and the noted data put below gets the uid key for this snapshot so we can use it later on item click

            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("villainquestions");
            DatabaseReference dbPushReference = dbReference.push();
            String questionUidX = dbPushReference.getKey(); // this then is the key for the question



            // converting RIC input into integer or setting to zero to avoid crash if left empty


            HashMap<String, Object> dataMap = new HashMap<>();

            dataMap.put("aaabadgeid", aaavillainid);
            dataMap.put("bbbcorrectpaintansw", bbbcorrectvaillainansw);
            dataMap.put("cccwrongpaintans1", cccwrongvillainsans1);
            dataMap.put("dddwrongpaintans2", dddwrongvillainsans2);
            dataMap.put("eeewrongpaintans3", eeewrongvillainsans3);
            dataMap.put("fffpaintexpanded", fffvillainexpanded);

            //////
            dataMap.put("gggquid", questionUidX); // the unique question UID which we can then use to link back to this coin

            /////

            // getting adjusted values for itemcount and collection value




            dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {


                        String Question3 = "Question: " + questionCounter + " uploaded to IHQ";
                        toastMessage(Question3);

                    }

                }
            });
        }

    }
    /**

     * Returns the cell as a string from the excel file

     * @param row

     * @param c

     * @param formulaEvaluator

     * @return

     */

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {

        String value = "";

        try {

            Cell cell = row.getCell(c);

            CellValue cellValue = formulaEvaluator.evaluate(cell);

            switch (cellValue.getCellType()) {

                case Cell.CELL_TYPE_BOOLEAN:

                    value = ""+cellValue.getBooleanValue();

                    break;

                case Cell.CELL_TYPE_NUMERIC:

                    double numericValue = cellValue.getNumberValue();

                    if(HSSFDateUtil.isCellDateFormatted(cell)) {

                        double date = cellValue.getNumberValue();

                        SimpleDateFormat formatter =

                                new SimpleDateFormat("MM/dd/yy");

                        value = formatter.format(HSSFDateUtil.getJavaDate(date));

                    } else {

                        value = ""+numericValue;

                    }

                    break;

                case Cell.CELL_TYPE_STRING:

                    value = ""+cellValue.getStringValue();

                    break;

                default:

            }

        } catch (NullPointerException e) {


            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );

        }

        return value;

    }
    /**

     * customizable toast

     * @param message

     */

    private void toastMessage(String message){

        Toast.makeText(this,message, Toast.LENGTH_LONG).show();

    }


}
