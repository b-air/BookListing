package com.example.android.booklisting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 14/07/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    ArrayList<Book> mBook;
    private static OnItemClickListener mListener;
    MainActivity mContext;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    //default constructor
    public BookAdapter(MainActivity context, ArrayList<Book> book, OnItemClickListener listener) {
        mContext = context;
        mBook = book;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, int position) {
        Book book = mBook.get(position);

        holder.bookTitleTextView.setText(book.getTitle());
        holder.bookAuthorTextView.setText(book.getAuthor());
        holder.bookLinkTextView.setText(book.getLink());

        //Picasso Library to convert the url from JSONObject imageLinks to a image(@thumbnail)
        Picasso.with(mContext).load(book.getCover()).into(holder.bookThumbnailImageView);

        holder.bind(mBook.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mBook.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bookThumbnailImageView;
        TextView bookTitleTextView;
        TextView bookAuthorTextView;
        TextView bookLinkTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookThumbnailImageView = (ImageView) itemView.findViewById(R.id.book_image);
            bookTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthorTextView = (TextView) itemView.findViewById(R.id.book_author);
            bookLinkTextView = (TextView) itemView.findViewById(R.id.book_link);
        }

        public void bind(final Book book, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(book);
                }
            });
        }
    }


    public void clear(){
        mBook.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Book> book){
        mBook.addAll(book);
        notifyDataSetChanged();
    }
}
