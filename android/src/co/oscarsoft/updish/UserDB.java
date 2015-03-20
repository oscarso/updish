package co.oscarsoft.updish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDB extends SQLiteOpenHelper {
	private static UserDB mInstance = null;

    private static final String DB_NAME = "updish";
    private static final String DB_TABLE = "ud_user";
    private static final int DB_VERSION = 1;

    private Context mCtx;

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LEVEL = "level";

    public static UserDB getInstance(Context ctx) {
    	if (mInstance == null) {
    		mInstance = new UserDB(ctx.getApplicationContext());
    	}
    	return mInstance;
    }

    private UserDB(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        this.mCtx = ctx;
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INFO_TABLE =
        		"CREATE TABLE " + DB_TABLE +
        		"("
        		+ KEY_ID + " TEXT PRIMARY KEY"
        		+ ", "
        		+ KEY_LEVEL + " INTEGER"
        		+ ")";
        Log.d(DebugInfo.TAG, "UserDB: onCreate: sql=" + CREATE_INFO_TABLE);
        db.execSQL(CREATE_INFO_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
 
        // Create tables again
        onCreate(db);
    }

    public User getUser() {
    	Log.d(DebugInfo.TAG, "getUser");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
        		DB_TABLE, //table name
        		new String[] { KEY_ID, KEY_LEVEL }, //columns
        		null, //selection
                null, //selectionArgs
                null, //groupBy
                null, //having
                null, //orderBy
                "1"   //limit
        		);
        if ((cursor != null) &&
        	cursor.moveToFirst()) {
        	Log.d(DebugInfo.TAG, "getUser:    ID: " + cursor.getString(0));
        	Log.d(DebugInfo.TAG, "getUser: level: " + cursor.getInt(1));
            User user = new User(
            		cursor.getString(0),
            		cursor.getInt(1)
            		);
            db.close();
            return user;
        } else {
        	db.close();
        	return null;
        }
    }

    public void setUser(User user) {
    	Log.d(DebugInfo.TAG, "setUser");
    	if (getUser() != null) {
    		// if User has already been set, overwrite is not allowed
    		Log.d(DebugInfo.TAG, "setUser: User has already been set");
    		return;
    	}

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_LEVEL, user.getLevel());
        Log.d(DebugInfo.TAG, "setUser:    ID: " + user.getId());
        Log.d(DebugInfo.TAG, "setUser: level: " + user.getLevel());
     
        // Inserting Row
        db.insert(DB_TABLE, null, values);
        db.close(); // Closing database connection
    }
}