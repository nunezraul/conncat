package conncat.conncat;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nunez on 4/11/2016.
 */
public class EventDBHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/conncat.conncat/databases/";
    private static String DB_NAME = "conncat.db";

    private SQLiteDatabase conncat;

    private final Context context;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_HOST = "host";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_SDATE = "start_date";
    public static final String KEY_EDATE = "end_date";
    public static final String KEY_STIME = "start_time";
    public static final String KEY_ETIME = "end_time";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SOURCE = "source";


    public EventDBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        conncat = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public void add(EventData event){
        if(event.getName() == null)
            return;

        String sql = "SELECT * FROM Events WHERE " +
                KEY_NAME + " LIKE '%" + event.getName() + "%';";
        Cursor cursor = conncat.rawQuery(sql, null);
        if(cursor.getCount() <= 0){
            sql = "INSERT into Events (name, host, start_date, end_date, start_time, end_time, address, description, source) VALUES ("
                    + DatabaseUtils.sqlEscapeString(event.getName()) + ", " + DatabaseUtils.sqlEscapeString(event.getHost()) + ", " + DatabaseUtils.sqlEscapeString(event.getStartDate()) + ", " + DatabaseUtils.sqlEscapeString(event.getEndDate()) + ", "
                    + DatabaseUtils.sqlEscapeString(event.startTime) + ", " + DatabaseUtils.sqlEscapeString(event.endTime) + ", " + DatabaseUtils.sqlEscapeString(event.getAddress()) + ", " + DatabaseUtils.sqlEscapeString(event.getDescription()) + ", " + DatabaseUtils.sqlEscapeString(event.getSource()) + ");";
            //sql = DatabaseUtils.sqlEscapeString(sql);
            conncat.execSQL(sql);
        }
    }

    public List<EventData> getAllEvents(){
        String sql = "SELECT * FROM Events ORDER BY date(start_date);";
        Cursor cursor = conncat.rawQuery(sql, null);
        List<EventData> ed = new ArrayList<EventData>();
        if(cursor.moveToFirst()){
            do{
                EventData eventData = new EventData();
                eventData.setName(cursor.getString(cursor.getColumnIndex("name")));
                eventData.setHost(cursor.getString(cursor.getColumnIndex("host")));
                eventData.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
                eventData.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
                eventData.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
                eventData.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                eventData.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                eventData.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                eventData.setSource(cursor.getString(cursor.getColumnIndex("source")));
                ed.add(eventData);

            }while(cursor.moveToNext());
        }
        return ed;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
