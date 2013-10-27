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
 * 主界面模块存放数据的DBAdapter
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
	 * 打开数据库
	 * 
	 * @return 返回的数据库(this)
	 * @throws SQLException
	 */
	public MainFrameDbAdapter open() throws SQLException {
		db = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * 往表中加入图片
	 * 
	 * @param img_id
	 *            图片的id号（唯一确定，即在主界面中显示的顺序号:0,1,2,...）
	 * @param bitmap
	 *            位图
	 * @return
	 * @see http://www.cnblogs.com/hedalixin/archive/2011/01/21/1941390.html
	 */
	public long insertImage(int img_id, Bitmap bitmap) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IMAGE_ID, img_id);

		// 处理bitmap，转换为Blob
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 95, os);
		initialValues.put(KEY_IMAGE_RAW, os.toByteArray());
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * 删除某ID的图片记录
	 * 
	 * @param img_id
	 *            图片ID
	 * @return
	 */
	public boolean deleteImage(long img_id) {
		return db.delete(DATABASE_TABLE, KEY_IMAGE_ID + "=" + img_id, null) > 0;
	}

	/**
	 * 获得所有图片记录
	 * 
	 * @return
	 */
	public Cursor getAllImages() {
		return db.query(DATABASE_TABLE, new String[] { KEY_IMAGE_ID,
				KEY_IMAGE_RAW }, null, null, null, null, null);
	}

	/**
	 * 获得指定id的图片记录
	 * 
	 * @param img_id
	 *            图片ID
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
	 * 更新图片
	 * 
	 * @param img_id
	 *            图片id
	 * @param img
	 *            图片bitmap
	 * @return
	 */
	public boolean updateImage(long img_id, Bitmap bitmap) {
		ContentValues args = new ContentValues();
		args.put(KEY_IMAGE_ID, img_id);

		// 处理bitmap，转换为Blob
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 95, os);
		args.put(KEY_IMAGE_RAW, os.toByteArray());
		return db.update(DATABASE_TABLE, args, KEY_IMAGE_ID + "=" + img_id, null)>0;
	}
	
	/**
	 * 更改图片的ID
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
	 * 获取当前已存取的图片数量
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
	 * 私用的DBHelper
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
			// DB创建
			try {
				Log.d("MainFrameDbAdapter", "CREATING TABLEs...");
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// DB版本更新
			Log.w(TAG, "Database updated to version " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
}
