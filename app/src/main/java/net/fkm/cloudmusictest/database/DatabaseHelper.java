package net.fkm.cloudmusictest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.fkm.cloudmusictest.model.MusicModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "music.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "favorite_music";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MUSIC_ID = "music_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_POSTER = "poster";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MUSIC_ID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AUTHOR + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PATH + " TEXT,"
                + COLUMN_POSTER + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertMusic(MusicModel musicModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSIC_ID, musicModel.getMusicId());
        values.put(COLUMN_NAME, musicModel.getName());
        values.put(COLUMN_AUTHOR, musicModel.getAuthor());
        values.put(COLUMN_DESCRIPTION, musicModel.getRemark());
        values.put(COLUMN_PATH, musicModel.getPath());
        values.put(COLUMN_POSTER, musicModel.getPoster());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<MusicModel> getAllMusic() {
        List<MusicModel> musicList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                MusicModel musicModel = new MusicModel();
                musicModel.setMusicId(cursor.getString(cursor.getColumnIndex(COLUMN_MUSIC_ID)));
                musicModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                musicModel.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                musicModel.setRemark(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                musicModel.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_PATH)));
                musicModel.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER)));
                musicList.add(musicModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return musicList;
    }

    public void deleteMusic(String musicId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_MUSIC_ID + " = ?", new String[]{musicId});
        db.close();
    }

    public boolean isMusicLiked(String musicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_MUSIC_ID + "=?", new String[]{musicId}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
