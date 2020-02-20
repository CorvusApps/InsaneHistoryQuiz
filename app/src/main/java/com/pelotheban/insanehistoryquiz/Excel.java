package com.pelotheban.insanehistoryquiz;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class Excel extends AppCompatActivity {


    // Needed for upload to Firebase

    int QuestionNumber3;


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

    ArrayList<ZZZjcExcel> uploadData;
    ListView lvInternalStorage;

    private CoordinatorLayout loutExcelActLOX; //primarily used for snackbars


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);

        loutExcelActLOX = findViewById(R.id.loutExcelActLO);

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

                String aaaqno = columns[0];
                String bbbcategory = columns[1];
                String cccquestion = columns[2];
                String dddcorrectansw = columns[3];
                String eeewrongans1 = columns[4];
                String fffwrongans2 = columns[5];
                String gggwrongans3 = columns[6];
                String hhhwrongans4 = columns[7];
                String iiiexpanded = columns[8];
                String kkkanstoggle = columns[9];
                String lllepoch = columns[10];
                String mmmera = columns[11];


                String cellInfo = "(aaaqno, bbbcategory, cccquestion, dddcorrectansw, eeewrongans1, fffwrongans2, gggwrongans3, hhhwrongans4, iiiexpanded, kkkanstoggle, lllepoch, mmmera)" +
                        ": (" + aaaqno + "," + bbbcategory + ", " + cccquestion + ", " + dddcorrectansw + ", " + eeewrongans1 + ", " + fffwrongans2 + ", " + gggwrongans3 + ", " + hhhwrongans4 + "" +
                        ", " + iiiexpanded + ", " + kkkanstoggle + " , " + lllepoch + ", " + mmmera + ")";

                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new ZZZjcExcel(aaaqno, bbbcategory, cccquestion, dddcorrectansw, eeewrongans1,fffwrongans2, gggwrongans3, hhhwrongans4, iiiexpanded, kkkanstoggle, lllepoch, mmmera));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();

    }
    private void printDataToLog() {

        Log.d(TAG, "printDataToLog: Printing data to log...");

        for(int i = 0; i< uploadData.size(); i++){

            final String aaaqno = uploadData.get(i).getAaaqno();
            String bbbcategory = uploadData.get(i).getBbbcategory();
            String cccquestion = uploadData.get(i).getCccquestion();
            String dddcorrectansw = uploadData.get(i).getDddcorrectansw();
            String eeewrongans1 = uploadData.get(i).getEeewrongans1();
            String fffwrongans2 = uploadData.get(i).getFffwrongans2();
            String gggwrongans3 = uploadData.get(i).getGggwrongans3();
            String hhhwrongans4 = uploadData.get(i).getHhhwrongans4();
            String iiiexpanded = uploadData.get(i).getIiiexpanded();
            String kkkanstoggle = uploadData.get(i).getKkkanstoggle();
            String lllepoch = uploadData.get(i).getLllepoch();
            String mmmera = uploadData.get(i).getMmmera();


            final int questionCounter = i +1; //coin counter for toast


            Log.d(TAG, "printDataToLog: (aaaqno, bbbcategory, cccquestion, dddcorrectansw, eeewrongans1, fffwrongans2, gggwrongans3, hhhwrongans4, iiiexpanded, kkkanstoggle, lllepoch, mmmera): (" + aaaqno + "," + bbbcategory + ", " + cccquestion + ", " + dddcorrectansw + ", " + eeewrongans1 + ", " + fffwrongans2 + ", " + gggwrongans3 + ", " + hhhwrongans4 + "" +
                    ", " + iiiexpanded + " , " + kkkanstoggle + ", " + lllepoch + ". " + mmmera + ")");

            String uid = FirebaseAuth.getInstance().getUid();

            //////// this and the noted data put below gets the uid key for this snapshot so we can use it later on item click

            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("questions");
            DatabaseReference dbPushReference = dbReference.push();
            String questionUidX = dbPushReference.getKey(); // this then is the key for the question



            // converting RIC input into integer or setting to zero to avoid crash if left empty

            Float QuestionNumber2 = Float.parseFloat(String.valueOf(aaaqno));
            QuestionNumber3 = (int)(Math.round(QuestionNumber2));// getting id to be an int before uploading so sorting works well


            HashMap<String, Object> dataMap = new HashMap<>();

            dataMap.put("aaaqno", QuestionNumber3);
            dataMap.put("bbbcategory", bbbcategory);
            dataMap.put("cccquestion", cccquestion);
            dataMap.put("dddcorrectansw", dddcorrectansw);
            dataMap.put("eeewrongans1", eeewrongans1);
            dataMap.put("fffwrongans2", fffwrongans2);
            dataMap.put("gggwrongans3", gggwrongans3);
            dataMap.put("hhhwrongans4", hhhwrongans4);
            dataMap.put("iiiexpanded", iiiexpanded);

            dataMap.put("kkkanstoggle", kkkanstoggle);

            dataMap.put("lllepoch", lllepoch);
            dataMap.put("mmmera", mmmera);




            //////
            dataMap.put("jjjquid", questionUidX); // the unique question UID which we can then use to link back to this coin

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
