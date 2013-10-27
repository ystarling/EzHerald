package com.herald.ezherald.mainframe;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * ������ģ�������ݵ�DBAdapter
 * 
 * @author BorisHe
 * 
 */
public class MainFrameDbAdapter {

	static final String TAG = "MainFrameDbAdapter";

	static final String KEY_IMAGE_ID = "img_id";
	static final String KEY_IMAGE_RAW = "img_raw";

	static final String DATABASE_NAME = "MAIN_FRAME";
	static final String DATABASE_TABLE = "banner";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + "("
			+ KEY_IMAGE_ID + " integer primary key, " + KEY_IMAGE_RAW
			+ " blob not null" + ");";

	final Context mContext;
	DatabaseHelper mDbHelper;
	SQLiteDatabase db;

	public MainFrameDbAdapter(Context context) {
		mContext = context;
		mDbHelper = new DatabaseHelper(context);
	}

	/**
	 * �����ݿ�
	 * 
	 * @return ���ص����ݿ�(this)
	 * @throws SQLException
	 */
	public MainFrameDbAdapter open() throws SQLException {
		db = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * �ر����ݿ�
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * �����м���ͼƬ
	 * 
	 * @param img_id
	 *            ͼƬ��id�ţ�Ψһȷ������������������ʾ��˳���:0,1,2,...��
	 * @param bitmap
	 *            λͼ
	 * @return
	 * @see http://www.cnblogs.com/hedalixin/archive/2011/01/21/1941390.html
	 */
	public long insertImage(int img_id, Bitmap bitmap) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IMAGE_ID, img_id);

		// ����bitmap��ת��ΪBlob
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 95, os);
		initialValues.put(KEY_IMAGE_RAW, os.toByteArray());
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * ɾ��ĳID��ͼƬ��¼
	 * 
	 * @param img_id
	 *            ͼƬID
	 * @return
	 */
	public boolean deleteImage(long img_id) {
		return db.delete(DATABASE_TABLE, KEY_IMAGE_ID + "=" + img_id, null) > 0;
	}

	/**
	 * �������ͼƬ��¼
	 * 
	 * @return
	 */
	public Cursor getAllImages() {
		return db.query(DATABASE_TABLE, new String[] { KEY_IMAGE_ID,
				KEY_IMAGE_RAW }, null, null, null, null, null);
	}

	/**
	 * ���ָ��id��ͼƬ��¼
	 * 
	 * @param img_id
	 *            ͼƬID
	 * @return Cursor
	 */
	public Cursor getImage(long img_id) {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_IMAGE_ID,
				KEY_IMAGE_RAW }, KEY_IMAGE_ID + "=" + img_id, null, null, null,
				null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param img_id
	 *            ͼƬid
	 * @param img
	 *            ͼƬbitmap
	 * @return
	 */
	public boolean updateImage(long img_id, Bitmap bitmap) {
		ContentValues args = new ContentValues();
		args.put(KEY_IMAGE_ID, img_id);

		// ����bitmap��ת��ΪBlob
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 95, os);
		args.put(KEY_IMAGE_RAW, os.toByteArray());
		return db.update(DATABASE_TABLE, args, KEY_IMAGE_ID + "=" + img_id, null)>0;
	}
	
	/**
	 * ����ͼƬ��ID
	 * @param oldId
	 * @param newId
	 * @return
	 */
	public boolean alterImageId(long oldId, long newId){
		ContentValues args = new ContentValues();
		args.put(KEY_IMAGE_ID, newId);
		return db.update(DATABASE_TABLE, args, KEY_IMAGE_ID + "=" + oldId, null)>0;
	}
	
	/**
	 * ��ȡ��ǰ�Ѵ�ȡ��ͼƬ����
	 * @return
	 */
	public int getCurrentImageCount(){
		String sqlCommand = "select count(*) from " + DATABASE_TABLE;
		Cursor cursor = db.rawQuery(sqlCommand, null);
		if(cursor != null && cursor.moveToFirst()){
			return cursor.getInt(0);			
		}
		return 0;
	}
	
	

	/**
	 * ˽�õ�DBHelper
	 * 
	 * @author BorisHe
	 * 
	 */
	public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// DB����
			try {
				Log.d("MainFrameDbAdapter", "CREATING TABLEs...");
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// DB�汾����
			Log.w(TAG, "Database updated to version " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
}
