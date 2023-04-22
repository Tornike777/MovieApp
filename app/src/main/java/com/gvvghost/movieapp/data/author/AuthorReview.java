package com.gvvghost.movieapp.data.author;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AuthorReview {
    @SerializedName("author")
    private final String name;

    @SerializedName("content")
    private final String content;

    @SerializedName("author_details")
    private final AuthorDetails authorDetail;

    public AuthorReview(String name,
                        String content,
                        AuthorDetails authorDetail) {
        this.name = name;
        this.content = content;
        this.authorDetail = authorDetail;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public AuthorDetails getAuthorDetail() {
        return authorDetail;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthorReview{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", authorDetail=" + authorDetail +
                '}';
    }
}
