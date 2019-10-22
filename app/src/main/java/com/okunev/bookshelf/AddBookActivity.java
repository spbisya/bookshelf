package com.okunev.bookshelf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.okunev.bookshelf.models.Book;
import com.okunev.bookshelf.utils.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static com.okunev.bookshelf.utils.Constants.ARG_BOOK;
import static com.okunev.bookshelf.utils.Constants.KEY_AUTHOR;
import static com.okunev.bookshelf.utils.Constants.KEY_DESCRIPTION;
import static com.okunev.bookshelf.utils.Constants.KEY_EXISTING_BOOK_ID;
import static com.okunev.bookshelf.utils.Constants.KEY_NAME;
import static com.okunev.bookshelf.utils.Constants.REQUEST_ADD_BOOK;
import static com.okunev.bookshelf.utils.Constants.REQUEST_CHANGE_BOOK;

public class AddBookActivity extends AppCompatActivity {
    @BindView(R.id.nameField)
    EditText nameField;
    @BindView(R.id.authorField)
    EditText authorField;
    @BindView(R.id.descriptionField)
    EditText descriptionField;
    @BindView(R.id.save)
    Button save;

    private DBHelper dbHelper;
    @Nullable
    private Integer existingBookId;

    public static void display(Activity activity) {
        activity.startActivityForResult(new Intent(activity, AddBookActivity.class), REQUEST_ADD_BOOK);
    }

    public static void display(Activity activity, Book book) {
        Intent intent = new Intent(activity, AddBookActivity.class);
        intent.putExtra(ARG_BOOK, book);
        activity.startActivityForResult(intent, REQUEST_CHANGE_BOOK);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        dbHelper = new DBHelper(this);

        Observable<Boolean> nameObservable = RxTextView.textChanges(nameField).map(text -> text.length() > 0).distinctUntilChanged();
        Observable<Boolean> authorObservable = RxTextView.textChanges(authorField).map(text -> text.length() > 0).distinctUntilChanged();
        Observable<Boolean> descriptionObservable = RxTextView.textChanges(descriptionField).map(text -> text.length() > 0).distinctUntilChanged();
        Observable.combineLatest(nameObservable, authorObservable, descriptionObservable,
                (nameResult, authorResult, descriptionResult) -> nameResult && authorResult && descriptionResult)
                .distinctUntilChanged()
                .subscribe(enable -> save.setEnabled(enable));

        save.setOnClickListener(v -> saveBook());

        if (getIntent().hasExtra(ARG_BOOK)) {
            Book book = (Book) getIntent().getSerializableExtra(ARG_BOOK);
            if (book != null) {
                existingBookId = book.getId();
                nameField.setText(book.getName());
                authorField.setText(book.getAuthor());
                descriptionField.setText(book.getDescription());
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(existingBookId == null ? R.string.add : R.string.edit);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void saveBook() {
        int id = existingBookId == null ? dbHelper.getNumberOfBooks() : existingBookId;
        Book book = new Book();
        book.setId(id);
        book.setName(nameField.getText().toString());
        book.setAuthor(authorField.getText().toString());
        book.setDescription(descriptionField.getText().toString());
        if (existingBookId != null) {
            dbHelper.updateBook(book);
        } else {
            dbHelper.insertBook(book);
        }
        Intent intent = new Intent();
        if (existingBookId != null) {
            intent.putExtra(ARG_BOOK, book);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_NAME, nameField.getText().toString());
        outState.putString(KEY_AUTHOR, authorField.getText().toString());
        outState.putString(KEY_DESCRIPTION, descriptionField.getText().toString());
        if (existingBookId != null) {
            outState.putInt(KEY_EXISTING_BOOK_ID, existingBookId);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        nameField.setText(savedInstanceState.getString(KEY_NAME));
        authorField.setText(savedInstanceState.getString(KEY_AUTHOR));
        descriptionField.setText(savedInstanceState.getString(KEY_DESCRIPTION));

        if (savedInstanceState.containsKey(KEY_EXISTING_BOOK_ID)) {
            existingBookId = savedInstanceState.getInt(KEY_EXISTING_BOOK_ID);
        }

        //no need in focus on startup
        nameField.clearFocus();
        authorField.clearFocus();
        descriptionField.clearFocus();
    }
}
