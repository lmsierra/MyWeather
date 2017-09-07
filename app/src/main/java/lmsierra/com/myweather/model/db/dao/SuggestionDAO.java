package lmsierra.com.myweather.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.lang.ref.WeakReference;

import lmsierra.com.myweather.model.Suggestion;
import lmsierra.com.myweather.model.Suggestions;
import lmsierra.com.myweather.model.db.DBHelper;

public class SuggestionDAO {
	public static String TAG = SuggestionDAO.class.toString();

	public static final String TABLE_SUGGESTION = "SUGGESTION";

	// Table field constants 
	public static final String KEY_SUGGESTION_ID = "_id";
	public static final String KEY_SUGGESTION_TITLE = "name";

	public static final String SQL_CREATE_RECENT_SUGGESTION_TABLE =
			"create table "  + TABLE_SUGGESTION
					+ "( " + KEY_SUGGESTION_ID + " integer primary key autoincrement, "
					+ KEY_SUGGESTION_TITLE + " text not null"
					+ ");";

	public static final String[] allColumns = {
			KEY_SUGGESTION_ID,
			KEY_SUGGESTION_TITLE,
	};

	private static final long INVALID_ID_DELETE_ALL_RECORDS = 0;
	private static final long INSERTING_EXISTING_ELEMENT = -2;

	private WeakReference<Context> context;
	private final String databaseName;

	public SuggestionDAO(Context context) {
		this(context, "MyWeather.sqlite");
	}

	public SuggestionDAO(Context context, String databaseName){
		this.context = new WeakReference<>(context);
		this.databaseName = databaseName;
	}

	public long insert(Suggestion suggestion) {

        if (suggestion == null) {
			throw new IllegalArgumentException("Passing NULL Suggestion");
		}

        if (context.get() == null) {
            throw new IllegalStateException("Context NULL");
        }

		Log.d(TAG, "Inserting suggestion with title " + suggestion.getTitle() + "into database");

		if(!isSuggestionCreated(suggestion)) {

			DBHelper db = DBHelper.getInstance(this.databaseName, context.get());
			long id = db.getWritableDatabase().insert(TABLE_SUGGESTION, null, this.getContentValues(suggestion));
			suggestion.setId(id);
			db.close();
			db = null;

			return id;
		}else{
			return INSERTING_EXISTING_ELEMENT;
		}
	}

	private boolean isSuggestionCreated(Suggestion suggestion) {

        DBHelper db = DBHelper.getInstance(this.databaseName, context.get());

		Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_SUGGESTION + " WHERE " + KEY_SUGGESTION_TITLE + " =? ", new String[]{suggestion.getTitle()});

		if(cursor.getCount() <= 0){
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}

	public int update(long id, Suggestion suggestion) {

        if (suggestion == null) {
			throw new IllegalStateException("Passing NULL suggestion" );
		}

		if (context.get() == null) {
			throw new IllegalStateException("Context NULL");
		}

		Log.d(TAG, "Updating suggestion with title " + suggestion.getTitle() + " in database");

        DBHelper db = DBHelper.getInstance(this.databaseName, context.get());

		int numberOfRowsUpdated = db.getWritableDatabase().update(TABLE_SUGGESTION, this.getContentValues(suggestion), KEY_SUGGESTION_ID + "=?", new String[]{"" + id});

		db.close();
		db = null;

		return numberOfRowsUpdated;
	}

	public void delete(long id) {
		Log.d(TAG, "Deleting record with id " + String.valueOf(id) + " from database");

        DBHelper db = DBHelper.getInstance(this.databaseName, context.get());

        if (id == INVALID_ID_DELETE_ALL_RECORDS) {
			db.getWritableDatabase().delete(TABLE_SUGGESTION, null, null);
		} else {
			db.getWritableDatabase().delete(TABLE_SUGGESTION, KEY_SUGGESTION_ID + " = " + id, null);
		}

        db.close();
		db=null;
	}

	public void deleteAll() {
		Log.d(TAG, "Deleting all records from database");
		delete(INVALID_ID_DELETE_ALL_RECORDS);
	}

	public static ContentValues getContentValues(Suggestion suggestion) {
		ContentValues content = new ContentValues();
		content.put(KEY_SUGGESTION_TITLE, suggestion.getTitle());

		return content;
	}

	public static Suggestion suggestionFromCursor(Cursor c) {

        assert c != null;

		String title  = c.getString(c.getColumnIndex(KEY_SUGGESTION_TITLE));
		long id = c.getLong(c.getColumnIndex(KEY_SUGGESTION_ID));

		Suggestion suggestion = new Suggestion(title);
		suggestion.setId(id);

		return suggestion;
	}


	/**
	 * *Returns all radars in DB inside a Cursor
	 * @return cursor with all records
	 */
	public Cursor queryCursor() {
		// select
		DBHelper db = DBHelper.getInstance(this.databaseName, context.get());

		Cursor c = db.getReadableDatabase().query(TABLE_SUGGESTION, allColumns, null, null, null, null, null);

		return c;
	}

	/**
	 * Returns a Radar object from its id
	 * @param id - the radar id in db
	 * @return Radar object if found, null otherwise
	 */
	public Suggestion query(long id) {

        Suggestion suggestion = null;

		DBHelper db = DBHelper.getInstance(this.databaseName, context.get());

		String where = KEY_SUGGESTION_ID + "=" + id;
		Cursor c = db.getReadableDatabase().query(TABLE_SUGGESTION, allColumns, where, null, null, null, null);

        if (c != null) {
			if (c.getCount() > 0) {
				c.moveToFirst();
				suggestion = suggestionFromCursor(c);
			}
		}

        c.close();
		db.close();
		return suggestion;
	}


	public Suggestions query(){
		Suggestions suggestions = new Suggestions();

		Cursor cursor = queryCursor();

		cursor.moveToFirst();

        if(cursor!=null && cursor.getCount()>0) {
			do {
				Suggestion suggestion = suggestionFromCursor(cursor);
				suggestions.add(suggestion);
			} while (cursor.moveToNext());
		}

        return suggestions;
	}
}
