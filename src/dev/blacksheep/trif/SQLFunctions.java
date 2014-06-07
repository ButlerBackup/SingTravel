package dev.blacksheep.trif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLFunctions {
	public static final String TAG = "TF2Econ[SQLi]";
	public static final String GLOBAL_ROWID = "_id";

	public static final String MARKET_IMAGE = "marketImage";
	public static final String MARKET_COLOR = "marketColor";
	public static final String MARKET_PRICE = "marketPrice";
	public static final String MARKET_NAME = "marketName";
	public static final String MARKET_DATE = "marketDate";
	public static final String MARKET_LINK = "marketLink";
	public static final String MARKET_SUB_CATEGORY = "marketSubCategory";
	public static final String MARKET_COLLECTION = "marketSubCollection";

	public static final String INVENTORY_ITEMS_ID = "invItemsID";
	public static final String INVENTORY_ITEMS_QUANTITY = "invItemsQuantity";

	public static final String MAIN_CATEGORY_NAME = "mcName";

	public static final String SUB_CATEGORY_RELATED_ID = "scRelatedID";
	public static final String SUB_CATEGORY_NAME = "scName";

	private static final String DATABASE_NAME = "csgo";
	private static final String TABLE_MARKET = "market";
	private static final String TABLE_INVENTORY = "inventory";
	private static final String TABLE_MAIN_CATEGORY = "main_category";
	private static final String TABLE_SUB_CATEGORY = "sub_category";

	private static final int DATABASE_VERSION = 2;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_MARKET + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MARKET_NAME + " TEXT NOT NULL, " + MARKET_COLOR + " TEXT NOT NULL, "
					+ MARKET_IMAGE + " TEXT NOT NULL, " + MARKET_PRICE + " TEXT NOT NULL, " + MARKET_DATE + " TEXT NOT NULL, " + MARKET_LINK + " TEXT NOT NULL, " + MARKET_SUB_CATEGORY
					+ " TEXT NOT NULL, " + MARKET_COLLECTION + " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + TABLE_INVENTORY + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + INVENTORY_ITEMS_ID + " TEXT NOT NULL, " + INVENTORY_ITEMS_QUANTITY
					+ " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + TABLE_MAIN_CATEGORY + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MAIN_CATEGORY_NAME + " TEXT NOT NULL);");
			db.execSQL("CREATE TABLE " + TABLE_SUB_CATEGORY + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUB_CATEGORY_RELATED_ID + " TEXT NOT NULL, " + SUB_CATEGORY_NAME
					+ " TEXT NOT NULL);");
			Log.e(TAG, "Created DB");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKET);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_CATEGORY);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORY);
			onCreate(db);
		}

	}

	public SQLFunctions(Context c) {
		ourContext = c;
	}

	public SQLFunctions open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return null;
	}

	public void close() {
		if (ourHelper != null) {
			ourHelper.close();
		} else {
			Log.e(TAG, "You did not open your database. Null error");
		}
	}

	public long unixTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public boolean longerThanTwoHours(String pTime) {
		int prevTime = Integer.parseInt(pTime);
		int currentTime = (int) (System.currentTimeMillis() / 1000L);
		int seconds = currentTime - prevTime;
		int how_many;
		if (seconds > 3600 && seconds < 86400) {
			how_many = (int) seconds / 3600;
			if (how_many >= 2) { // 2 hours
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getLastRowId() {
		String sql = "SELECT * FROM " + TABLE_MARKET + " ORDER BY " + GLOBAL_ROWID + " DESC LIMIT 1";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String id = cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID));
				cursor.close();
				Log.e("LATEST SQL ROW", id);
				return id;
			}
		}
		cursor.close();
		return "";
	}

	public HashMap<String, String> getItemInformation(String name) {
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "SELECT * FROM " + TABLE_MARKET + " WHERE " + MARKET_NAME + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { name });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				map.put("link", cursor.getString(cursor.getColumnIndex(MARKET_LINK)));
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<HashMap<String, String>> loadInventory() {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MARKET + " m LEFT JOIN " + TABLE_INVENTORY + " i ON i." + INVENTORY_ITEMS_ID + " = m." + GLOBAL_ROWID, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					Log.e("IMAGE", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("image", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("quantity", cursor.getString(cursor.getColumnIndex(INVENTORY_ITEMS_QUANTITY)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<HashMap<String, String>> loadMarketPage(boolean loadByDate) {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		Cursor cursor;
		if (!loadByDate) {
			cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MARKET + " ORDER BY " + MARKET_NAME + " ASC", null);
		} else {
			cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MARKET + " ORDER BY " + MARKET_DATE + " DESC", null);
		}
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("name", cursor.getString(cursor.getColumnIndex(MARKET_NAME)));
					hash.put("price", cursor.getString(cursor.getColumnIndex(MARKET_PRICE)));
					hash.put("color", cursor.getString(cursor.getColumnIndex(MARKET_COLOR)));
					hash.put("image", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("id", cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<HashMap<String, String>> loadMarketPage(int page) {
		int previousLimit = 0;
		int nextLimit = page * Consts.ITEMS_LOAD_LIMIT;
		if (page > 1) {
			previousLimit = (page - 1) * Consts.ITEMS_LOAD_LIMIT + 1;
		}
		Log.e("LIMIT", previousLimit + "|" + nextLimit);
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MARKET + " ORDER BY " + MARKET_NAME + " ASC LIMIT " + previousLimit + " , " + nextLimit, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("name", cursor.getString(cursor.getColumnIndex(MARKET_NAME)));
					hash.put("price", cursor.getString(cursor.getColumnIndex(MARKET_PRICE)));
					hash.put("color", cursor.getString(cursor.getColumnIndex(MARKET_COLOR)));
					hash.put("image", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("id", cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public void insertMainCategory(String mainCategory) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_MAIN_CATEGORY + " WHERE " + MAIN_CATEGORY_NAME + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { mainCategory });
		if (cursor.moveToFirst()) {
		} else {
			cv.put(MAIN_CATEGORY_NAME, mainCategory);
			try {
				ourDatabase.insert(TABLE_MAIN_CATEGORY, null, cv);
			} catch (Exception e) {
				Log.e(TAG, "Error creating main category");
			}
		}
		cursor.close();
	}

	public void insertSubCategory(String mainCategory, ArrayList<String> subCategory) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_MAIN_CATEGORY + " WHERE " + MAIN_CATEGORY_NAME + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { mainCategory });
		if (cursor.moveToFirst()) {
			String id = cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID));
			for (String s : subCategory) {
				cv.put(SUB_CATEGORY_RELATED_ID, id);
				cv.put(SUB_CATEGORY_NAME, s);
				try {
					ourDatabase.insert(TABLE_SUB_CATEGORY, null, cv);
				} catch (Exception e) {
					Log.e(TAG, "Error creating sub category");
				}
			}
		} else {
			Log.e(TAG, "Tryin to create sub category without main category -> " + mainCategory);
		}
		cursor.close();
	}

	public void insertMarketItem(String name, String color, String image, String price, String date, String link, String subCategory, String collection) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_MARKET + " WHERE " + MARKET_NAME + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { name });
		if (cursor.moveToFirst()) {
		} else {
			cv.put(MARKET_NAME, name);
			cv.put(MARKET_COLOR, color);
			cv.put(MARKET_IMAGE, image);
			cv.put(MARKET_PRICE, price);
			cv.put(MARKET_DATE, date);
			cv.put(MARKET_LINK, link);
			cv.put(MARKET_SUB_CATEGORY, subCategory);
			cv.put(MARKET_COLLECTION, collection);
			try {
				// Log.e(TAG, "Adding qualities schema data");
				ourDatabase.insert(TABLE_MARKET, null, cv);
			} catch (Exception e) {
				Log.e(TAG, "Error creating market entry", e);
			}
		}
		cursor.close();
	}

	public String getItemPrices(String id) {
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MARKET + " WHERE " + GLOBAL_ROWID + " = '" + id + "'", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String value = cursor.getString(cursor.getColumnIndex(MARKET_PRICE));
				cursor.close();
				return value;
			}
		}
		cursor.close();
		return Consts.MARKET_ITEM_NOT_FOUND;
	}

	public void purchaseMarketItem(String id) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_INVENTORY + " WHERE " + INVENTORY_ITEMS_ID + " = '" + id + "'";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			String squantity = cursor.getString(cursor.getColumnIndex(INVENTORY_ITEMS_QUANTITY));
			Log.e("BUYBUY", "User have " + squantity + " of this item");
			try {
				int quantity = Integer.parseInt(squantity);
				int newQuantity = quantity + 1;
				cv.put(INVENTORY_ITEMS_QUANTITY, String.valueOf(newQuantity));
				String whereClause = INVENTORY_ITEMS_ID + "=?";
				String[] whereArgs = new String[] { id };
				try {
					ourDatabase.update(TABLE_INVENTORY, cv, whereClause, whereArgs);
				} catch (Exception e) {
					Log.e(TAG, "Error updating purchaseMarketItem entry", e);
				}
				cursor.close();
			} catch (Exception e) {
				Log.e("purchaseMarketItem", "User tried to edit inventory quantity?");
			}
		} else {
			cv.put(INVENTORY_ITEMS_ID, id);
			cv.put(INVENTORY_ITEMS_QUANTITY, "1");
			try {
				// Log.e(TAG, "Adding qualities schema data");
				ourDatabase.insert(TABLE_INVENTORY, null, cv);
			} catch (Exception e) {
				Log.e(TAG, "Error creating purchaseMarketItem entry", e);
			}
		}
		cursor.close();
	}

	public ArrayList<HashMap<String, String>> searchItem(String item) {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT * FROM " + TABLE_MARKET + " WHERE " + MARKET_NAME + " LIKE ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { "%" + item.toLowerCase(Locale.getDefault()) + "%" });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("name", cursor.getString(cursor.getColumnIndex(MARKET_NAME)));
					hash.put("price", cursor.getString(cursor.getColumnIndex(MARKET_PRICE)));
					hash.put("color", cursor.getString(cursor.getColumnIndex(MARKET_COLOR)));
					hash.put("image", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("id", cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<String> getMainCategory() {
		ArrayList<String> data = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_MAIN_CATEGORY;
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					Log.e("MAIN CATEGORY", cursor.getString(cursor.getColumnIndex(MAIN_CATEGORY_NAME)));
					data.add(cursor.getString(cursor.getColumnIndex(MAIN_CATEGORY_NAME)));
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return data;
	}

	public ArrayList<String> getSubCategory(String mainCategoryName) {
		ArrayList<String> data = new ArrayList<>();
		String sql = "SELECT s." + SUB_CATEGORY_NAME + " FROM " + TABLE_MAIN_CATEGORY + " m LEFT JOIN " + TABLE_SUB_CATEGORY + " s ON m." + GLOBAL_ROWID + " = s." + SUB_CATEGORY_RELATED_ID
				+ " AND m." + MAIN_CATEGORY_NAME + " =  '" + mainCategoryName + "'";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					String r = cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_NAME));
					if (r != null) {
						data.add(r);
					}
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return data;
	}

	public String getSubCategoryId(String subCategoryName) {
		String sql = "SELECT * FROM " + TABLE_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_NAME + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { subCategoryName });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String r = cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_RELATED_ID));
				Log.e("SUB CATEGORY ID", r);
				cursor.close();
				return r;
			}
		}
		cursor.close();
		return null;
	}

	public ArrayList<HashMap<String, String>> loadMarkeyByCategory(String subCategory) {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT * FROM " + TABLE_MARKET + " WHERE " + MARKET_SUB_CATEGORY + " = '" + getSubCategoryId(subCategory) + "'";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("name", cursor.getString(cursor.getColumnIndex(MARKET_NAME)));
					hash.put("price", cursor.getString(cursor.getColumnIndex(MARKET_PRICE)));
					hash.put("color", cursor.getString(cursor.getColumnIndex(MARKET_COLOR)));
					hash.put("image", cursor.getString(cursor.getColumnIndex(MARKET_IMAGE)));
					hash.put("id", cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

}
