package com.okunev.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.okunev.bookshelf.adapters.BooksRecyclerAdapter;
import com.okunev.bookshelf.interfaces.OnBookClickedListener;
import com.okunev.bookshelf.models.Book;
import com.okunev.bookshelf.utils.DBHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.okunev.bookshelf.utils.Constants.REQUEST_ADD_BOOK;
import static com.okunev.bookshelf.utils.Constants.REQUEST_VIEW_BOOK;

public class MainActivity extends AppCompatActivity implements OnBookClickedListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ArrayList<Book> books;
    private BooksRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int orientation = this.getResources().getConfiguration().orientation;
        int spanCount = orientation == ORIENTATION_PORTRAIT ? 2 : 4;

        books = new ArrayList<>();
        adapter = new BooksRecyclerAdapter(books, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerView.setAdapter(adapter);

        loadBooks();
    }

    private void loadBooks() {
        DBHelper dbHelper = new DBHelper(this);
        books.clear();
        books.addAll(dbHelper.getAllBooks());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            AddBookActivity.display(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookClicked(Book book) {
        BookInfoActivity.display(this, book);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ADD_BOOK || requestCode == REQUEST_VIEW_BOOK) && resultCode == RESULT_OK) {
            recyclerView.post(this::loadBooks);
        }
    }
}
