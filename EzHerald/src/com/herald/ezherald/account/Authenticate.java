package com.herald.ezherald.account;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class Authenticate {
	public static final String DATABASE_NAME = "Info.db"; // 定义数据库名称
	public static final int DATABASE_VERSION = 1;// 定义数据库版本
	public static final String TABLE_NAME = "account";// 定义数据表名称
	public static final String IDCARD_TYPE = "idcard";
	public static final String TYX_TYPE = "tyx";
	public static final String LIBRARY_TYPE = "library";
	private static DatabaseHelper dbHelper = null;

	// 若已登录一卡通账户则返回类型为UserAccount的对象，若没有登录则返回null
	public static UserAccount getIDcardUser(Context context) {
		UserAccount userAccount = null;
		dbHelper = new DatabaseHelper(context, "Info.db");
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "username",
				"password" }, "type=?", new String[] { IDCARD_TYPE }, null,
				null, null);
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = EncryptionHelper.decryptDES(cursor.getString(cursor
					.getColumnIndex("password")), EncryptionHelper.KEY);
			
			
			userAccount = new UserAccount(username, password);
		}
		cursor.close();
		database.close();

		return userAccount;
	}

	public static UserAccount getTyxUser(Context context) {
		UserAccount userAccount = null;
		dbHelper = new DatabaseHelper(context, "Info.db");
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "username",
				"password" }, "type=?", new String[] { TYX_TYPE }, null, null,
				null);
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = EncryptionHelper.decryptDES(cursor.getString(cursor
					.getColumnIndex("password")), EncryptionHelper.KEY);
			userAccount = new UserAccount(username, password);
		}
		cursor.close();
		database.close();

		return userAccount;
	}

	public static UserAccount getLibUser(Context context) {
		UserAccount userAccount = null;
		dbHelper = new DatabaseHelper(context, "Info.db");
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "username",
				"password" }, "type=?", new String[] { LIBRARY_TYPE }, null,
				null, null);
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = EncryptionHelper.decryptDES(cursor.getString(cursor
					.getColumnIndex("password")), EncryptionHelper.KEY);
			userAccount = new UserAccount(username, password);
		}
		cursor.close();
		database.close();

		return userAccount;
	}

	public static void close() {
		// NOTE: openHelper must now be a member of CallDataHelper;
		// you currently have it as a local in your constructor
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
