package com.okunev.bookshelf.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.okunev.bookshelf.models.Book;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookShelf.db";
    private static final String BOOKS_TABLE_NAME = "books";
    private static final String BOOKS_COLUMN_ID = "id";
    private static final String BOOKS_COLUMN_NAME = "name";
    private static final String BOOKS_COLUMN_AUTHOR = "author";
    private static final String BOOKS_COLUMN_DESCRIPTION = "description";

    private static final String REQUEST_CREATE_TABLE = "create table if not exists "
            + BOOKS_TABLE_NAME + " ("
            + BOOKS_COLUMN_ID + " integer primary key, "
            + BOOKS_COLUMN_NAME + " text, "
            + BOOKS_COLUMN_AUTHOR + " text, "
            + BOOKS_COLUMN_DESCRIPTION + " text)";
    private static final String REQUEST_DROP_TABLE = "DROP TABLE IF EXISTS " + BOOKS_TABLE_NAME;
    private static final String REQUEST_WHERE_CLAUSE = BOOKS_COLUMN_ID + " = ? ";
    private static final String REQUEST_SELECT_ALL = "select * from " + BOOKS_TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REQUEST_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(REQUEST_DROP_TABLE);
        onCreate(db);
    }

    private ContentValues generateContentValues(Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_NAME, book.getName());
        contentValues.put(BOOKS_COLUMN_AUTHOR, book.getAuthor());
        contentValues.put(BOOKS_COLUMN_DESCRIPTION, book.getDescription());
        return contentValues;
    }

    private Book generateBookFromCursor(Cursor res) {
        Book book = new Book();
        book.setId(res.getInt(res.getColumnIndex(BOOKS_COLUMN_ID)));
        book.setName(res.getString(res.getColumnIndex(BOOKS_COLUMN_NAME)));
        book.setAuthor(res.getString(res.getColumnIndex(BOOKS_COLUMN_AUTHOR)));
        book.setDescription(res.getString(res.getColumnIndex(BOOKS_COLUMN_DESCRIPTION)));
        return book;
    }

    public boolean insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(BOOKS_TABLE_NAME, null, generateContentValues(book));
        return true;
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "
                + BOOKS_TABLE_NAME + " where "
                + BOOKS_COLUMN_ID + "=" + id + "", null);
        res.moveToFirst();
        Book book = generateBookFromCursor(res);
        res.close();
        return book;
    }

    public int getNumberOfBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, BOOKS_TABLE_NAME);
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(BOOKS_TABLE_NAME, generateContentValues(book),
                REQUEST_WHERE_CLAUSE, new String[]{Integer.toString(book.getId())});
        return true;
    }

    public Integer deleteBook(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BOOKS_TABLE_NAME, REQUEST_WHERE_CLAUSE, new String[]{Integer.toString(id)});
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(REQUEST_SELECT_ALL, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Book book = generateBookFromCursor(res);
            books.add(book);
            res.moveToNext();
        }
        res.close();
        return books;
    }
}