package com.energysistem.energylauncher.tvboxlauncher.database;

/**
 * Created by pgc on 21/11/2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;

import org.w3c.dom.Comment;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDAO {
        //COMMIT PABLO
    // Database fields
    private SQLiteDatabase database;
    private SqliteHelper dbHelper;
    private String[] allColumns = {SqliteHelper.COLUMN_ID,
            SqliteHelper.COLUMN_TITULO, SqliteHelper.COLUMN_URL, SqliteHelper.COLUMN_POSI, SqliteHelper.COLUMN_FAVORITO};

    public BookmarkDAO(Context context) {
        dbHelper = new SqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public WebPageInfo createBookmark(int _id, String _titulo, String _url, int _posi, int _fav) {

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COLUMN_ID, _id);
        values.put(SqliteHelper.COLUMN_TITULO, _titulo);
        values.put(SqliteHelper.COLUMN_URL, _url);
        values.put(SqliteHelper.COLUMN_POSI, _posi);
        values.put(SqliteHelper.COLUMN_FAVORITO, _fav);


        long insertId = database.insert(SqliteHelper.TABLE_BOOKMARKS, null,
                values);
        Cursor cursor = database.query(SqliteHelper.TABLE_BOOKMARKS,
                allColumns, SqliteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        WebPageInfo newBookmark = cursorTobookmark(cursor);
        cursor.close();
        return newBookmark;
    }

    public WebPageInfo createBookmark(WebPageInfo _bookmark) {

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COLUMN_ID, _bookmark.getId());
        values.put(SqliteHelper.COLUMN_TITULO, _bookmark.getTitle());
        values.put(SqliteHelper.COLUMN_URL, _bookmark.getPageUrl().toString());
        values.put(SqliteHelper.COLUMN_POSI, _bookmark.getPosi());
        values.put(SqliteHelper.COLUMN_FAVORITO, _bookmark.getFav());

        long insertId = database.insert(SqliteHelper.TABLE_BOOKMARKS, null,
                values);
        Cursor cursor = database.query(SqliteHelper.TABLE_BOOKMARKS,
                allColumns, SqliteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        WebPageInfo newBookmark = cursorTobookmark(cursor);
        cursor.close();
        return newBookmark;
    }

    public void deleteBookmark(WebPageInfo bookmark) {
        long id = bookmark.getId();
        System.out.println("Bookmark deleted with id: " + id);
        database.delete(SqliteHelper.TABLE_BOOKMARKS, SqliteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<WebPageInfo> getAllBookmarks() {
        ArrayList<WebPageInfo> bookmarks = new ArrayList<WebPageInfo>();

        Cursor cursor = database.query(SqliteHelper.TABLE_BOOKMARKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WebPageInfo bookmark = cursorTobookmark(cursor);
            bookmarks.add(bookmark);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return bookmarks;
    }

     /*info = new WebPageInfo(mTxtUri.getText().toString());
        info.setTitle(mTxtName.getText().toString());*/

    private WebPageInfo cursorTobookmark(Cursor cursor) {
        WebPageInfo bookmark = null;
        try {
            bookmark = new WebPageInfo(cursor.getInt(0),cursor.getString(2));
            bookmark.setTitle(cursor.getString(1));
            bookmark.setPosi(cursor.getInt(3));
            bookmark.setFav(cursor.getInt(4));
            if(bookmark.getFav()==1)
                bookmark.checked=true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bookmark;
    }

    public void updateBookmark(WebPageInfo info) {
        String strFilter = "id=" + info.getId();
        ContentValues args = new ContentValues();
        Log.e("favorito:",info.getFav()+"");
        args.put(SqliteHelper.COLUMN_POSI, info.getPosi());
        args.put(SqliteHelper.COLUMN_FAVORITO, info.getFav());



        database.update(SqliteHelper.TABLE_BOOKMARKS, args, strFilter, null);
    }
}
