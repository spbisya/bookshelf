package com.okunev.bookshelf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.okunev.bookshelf.R;
import com.okunev.bookshelf.interfaces.OnBookClickedListener;
import com.okunev.bookshelf.models.Book;
import com.okunev.bookshelf.utils.ImageHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.BookViewHolder> {
    private ArrayList<Book> books;
    private OnBookClickedListener onBookClickedListener;

    public BooksRecyclerAdapter(ArrayList<Book> books, OnBookClickedListener onBookClickedListener) {
        this.books = books;
        this.onBookClickedListener = onBookClickedListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, onBookClickedListener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root)
        LinearLayout root;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.author)
        TextView author;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(final Book book, final OnBookClickedListener onBookClickedListener) {
            root.setOnClickListener(v -> onBookClickedListener.onBookClicked(book));
            name.setText(book.getName());
            author.setText(book.getAuthor());
            Picasso.get().load(ImageHelper.getRandomImageUrl()).into(image);
        }
    }
}
