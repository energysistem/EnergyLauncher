package com.energysistem.energylauncher.tvboxlauncher.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOKMARKS = "bookmarks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_POSI = "posi";
    public static final String COLUMN_FAVORITO = "favorito";
    private final String PREFS_LIST_APPS = "SelectedDesktopApps";
    private static final String HEADLISTFAVS = "ListFav_";
    private static final String FAVS_LIST_SIZE = "FavsListSize";

    private static final String DATABASE_NAME = "bookmarks.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_BOOKMARKS + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_TITULO
            + " text, "+COLUMN_URL+ " text, "+COLUMN_POSI+ " integer, "+COLUMN_FAVORITO+" integer);";
    private SharedPreferences sharedPreferences;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
        sharedPreferences = mContext.getSharedPreferences(PREFS_LIST_APPS, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL("insert into " + TABLE_BOOKMARKS + "(" + COLUMN_ID + ","
                + COLUMN_TITULO + "," + COLUMN_URL + ","+ COLUMN_POSI + ","+ COLUMN_FAVORITO +
                ") values(0,'Energy Sistem','http://www.energysistem.com',10,1)");

        //insertItemEnd("com.android.vending");
    }

    public void insertItemEnd(String appName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int size = sharedPreferences.getInt(FAVS_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString(HEADLISTFAVS + (size), appName);

        size = size + 1;
        editor.putInt(FAVS_LIST_SIZE, size);

        editor.commit();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        onCreate(db);
    }

}