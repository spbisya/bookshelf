package com.okunev.bookshelf;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.okunev.bookshelf.models.Book;
import com.okunev.bookshelf.utils.DBHelper;
import com.okunev.bookshelf.utils.ImageHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.okunev.bookshelf.utils.Constants.ARG_BOOK;
import static com.okunev.bookshelf.utils.Constants.KEY_IS_DIALOG_VISIBLE;
import static com.okunev.bookshelf.utils.Constants.REQUEST_CHANGE_BOOK;
import static com.okunev.bookshelf.utils.Constants.REQUEST_VIEW_BOOK;

public class BookInfoActivity extends AppCompatActivity {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.description)
    TextView description;

    private Book book;
    private DBHelper dbHelper;
    private boolean isDialogVisible;

    public static void display(Activity activity, Book book) {
        Intent intent = new Intent(activity, BookInfoActivity.class);
        intent.putExtra(ARG_BOOK, book);
        activity.startActivityForResult(intent, REQUEST_VIEW_BOOK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        ButterKnife.bind(this);

        isDialogVisible = false;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.book_info);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dbHelper = new DBHelper(this);

        if (getIntent().hasExtra(ARG_BOOK)) {
            book = (Book) getIntent().getSerializableExtra(ARG_BOOK);
            setupScreen();
        }
    }

    private void setupScreen() {
        name.setText(book.getName());
        author.setText(book.getAuthor());
        description.setText(book.getDescription());
        Picasso.get().load(ImageHelper.getRandomImageUrl()).into(image);
    }

    private void askForDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.book_deletion))
                .setMessage(getString(R.string.book_deletion_message))
                .setNegativeButton(R.string.no, (dialog, which) -> dismissDialog(dialog))
                .setPositiveButton(R.string.yes, (dialog, which) -> deleteBook());
        builder.create();
        if (!isFinishing()) {
            builder.show();
            isDialogVisible = true;
        }
    }

    private void dismissDialog(DialogInterface dialog) {
        dialog.dismiss();
        isDialogVisible = false;
    }

    private void deleteBook() {
        dbHelper.deleteBook(book.getId());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                AddBookActivity.display(this, book);
                break;
            case R.id.delete:
                askForDeletion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_IS_DIALOG_VISIBLE, isDialogVisible);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getBoolean(KEY_IS_DIALOG_VISIBLE)) {
            askForDeletion();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_BOOK && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(ARG_BOOK)) {
                book = (Book) data.getSerializableExtra(ARG_BOOK);
                setupScreen();
                setResult(RESULT_OK);
            }
        }
    }
}
