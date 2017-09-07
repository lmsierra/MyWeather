package lmsierra.com.myweather.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import lmsierra.com.myweather.model.db.dao.SuggestionDAO;

public class DBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;

	public static final String[] CREATE_DATABASE_SCRIPTS = {
		SuggestionDAO.SQL_CREATE_RECENT_SUGGESTION_TABLE
	};

	private static DBHelper sInstance;
	
	public DBHelper(String databaseName, Context context) {
		super(context, databaseName, null, DATABASE_VERSION);
	}

	public static DBHelper getInstance(String databaseName, Context context) {
	    if (sInstance == null) {
	      sInstance = new DBHelper(databaseName,context.getApplicationContext());
	    }
	    return sInstance;
	 }
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		
		db.execSQL("PRAGMA foreign_keys = ON");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
		//TODO: upgrade for version 1->2
		}
		
	}
	
	private void createDB(SQLiteDatabase db) {
		for (String sql: CREATE_DATABASE_SCRIPTS) {
			db.execSQL(sql);
		}
	}
	
	// Convenience methods to convert types
	public static int convertBooleanToInt(boolean b) {
		return b ? 1 : 0;
	}
	
	public static boolean convertIntToBoolean(int b) {
		return b != 0;
	}
	
	
	public static Long convertDateToLong(Date date) {
	    if (date != null) {
	        return date.getTime();
	    }
	    return null;
	}
	
	public static Date convertLongToDate(Long dateAsLong) {
	    if (dateAsLong == null) {
	        return null;
	    }
	    return new Date(dateAsLong);
	}
}
