package conncat.conncat;

import android.content.ContentUris;
import android.content.ContentValues;
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

    public static final String KEY_EVENTS = "Events";
    public static final String KEY_CATEGORIES = "Categories";
    public static final String KEY_CATEGORY = "type";

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
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, event.getName());
            values.put(KEY_HOST, event.getHost());
            values.put(KEY_SDATE, event.getStartDate());
            values.put(KEY_EDATE, event.getEndDate());
            values.put(KEY_STIME, event.startTime);
            values.put(KEY_ETIME, event.endTime);
            values.put(KEY_ADDRESS, event.getAddress());
            values.put(KEY_DESCRIPTION, event.getDescription());
            values.put(KEY_SOURCE, event.getSource());
            long rowid = conncat.insert(KEY_EVENTS, null, values);
            if(!event.categories.isEmpty()){
                for(int i = 0; i < event.categories.size(); i++){
                    ContentValues cat = new ContentValues();
                    cat.put(KEY_ROWID, rowid);
                    cat.put(KEY_CATEGORY, event.categories.get(i));
                    conncat.insert(KEY_CATEGORIES, null, cat);
                }
            }
        }
    }

    public List<EventData> getAllEvents(){
        String sql = "SELECT * FROM Events ORDER BY date(start_date);";
        Cursor cursor = conncat.rawQuery(sql, null);
        List<EventData> ed = new ArrayList<EventData>();
        if(cursor.moveToFirst()){
            do{
                EventData eventData = new EventData();
                eventData.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                eventData.setHost(cursor.getString(cursor.getColumnIndex(KEY_HOST)));
                eventData.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_SDATE)));
                eventData.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_EDATE)));
                eventData.setStartTime(cursor.getString(cursor.getColumnIndex(KEY_STIME)));
                eventData.setEndTime(cursor.getString(cursor.getColumnIndex(KEY_ETIME)));
                eventData.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                eventData.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                eventData.setSource(cursor.getString(cursor.getColumnIndex(KEY_SOURCE)));

                String getCat = "SELECT * FROM Categories WHERE _id = " + cursor.getString(cursor.getColumnIndex(KEY_ROWID)) + ";";
                Cursor cat = conncat.rawQuery(getCat, null);
                if(cat.moveToFirst()){
                    do{
                        eventData.addCategory(cat.getString(cat.getColumnIndex(KEY_CATEGORY)));
                    }while(cat.moveToNext());
                }
                ed.add(eventData);

            }while(cursor.moveToNext());
        }
        return ed;

    }

    public EventData getEvent(long id){
        EventData eventData = new EventData();
        String sql = "SELECT * FROM Events WHERE _id = " + id + ";";
        Cursor cursor = conncat.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            eventData.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            eventData.setHost(cursor.getString(cursor.getColumnIndex(KEY_HOST)));
            eventData.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_SDATE)));
            eventData.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_EDATE)));
            eventData.setStartTime(cursor.getString(cursor.getColumnIndex(KEY_STIME)));
            eventData.setEndTime(cursor.getString(cursor.getColumnIndex(KEY_ETIME)));
            eventData.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            eventData.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            eventData.setSource(cursor.getString(cursor.getColumnIndex(KEY_SOURCE)));

            String getCat = "SELECT * FROM Categories WHERE _id = " + cursor.getString(cursor.getColumnIndex(KEY_ROWID)) + ";";
            Cursor cat = conncat.rawQuery(getCat, null);
            if(cat.moveToFirst()){
                do{
                    eventData.addCategory(cat.getString(cat.getColumnIndex(KEY_CATEGORY)));
                }while(cat.moveToNext());
            }
        }
        return eventData;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
