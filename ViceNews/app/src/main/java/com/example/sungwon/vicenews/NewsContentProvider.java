package com.example.sungwon.vicenews;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by SungWon on 9/26/2016.
 */

public class NewsContentProvider extends ContentProvider {

    private ViceDBHelper myDB;
    private static final String AUTHORITY = "com.example.sungwon.vicenews.NewsContentProvider";
    private static final String ARTICLES_RECENT_TABLE = ViceDBHelper.DATABASE_TABLE_NAME_LATEST;
    private static final String ARTICLES_POPULAR_TABLE = ViceDBHelper.DATABASE_TABLE_NAME_POPULAR;
    private static final String ARTICLES_CATEGORY_TABLE = ViceDBHelper.DATABASE_TABLE_NAME_CATEGORY;

    public static final Uri CONTENT_RECENT_URI = Uri.parse("content://"
            + AUTHORITY + "/");
    public static final Uri CONTENT_RECENT_URI_FULL = Uri.parse("content://"
            + AUTHORITY + "/" + ARTICLES_RECENT_TABLE + "/0");
    public static final Uri CONTENT_POPULAR_URI_FULL = Uri.parse("content://"
            + AUTHORITY + "/" + ARTICLES_POPULAR_TABLE + "/0");
    public static final Uri CONTENT_CATEGORY_URI_FULL = Uri.parse("content://"
            + AUTHORITY + "/" + ARTICLES_CATEGORY_TABLE + "/0");

    public static final int ARTICLES_RECENT = 1;
    public static final int ARTICLES_RECENT_ID = 2;
    public static final int ARTICLES_POPULAR = 3;
    public static final int ARTICLES_POPULAR_ID = 4;
    public static final int ARTICLES_CATEGORY = 5;
    public static final int ARTICLES_CATEGORY_ID = 6;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, ARTICLES_RECENT_TABLE, ARTICLES_RECENT);
        sURIMatcher.addURI(AUTHORITY, ARTICLES_RECENT_TABLE + "/#", ARTICLES_RECENT_ID);
        sURIMatcher.addURI(AUTHORITY, ARTICLES_POPULAR_TABLE, ARTICLES_POPULAR);
        sURIMatcher.addURI(AUTHORITY, ARTICLES_POPULAR_TABLE + "/#", ARTICLES_POPULAR_ID);
        sURIMatcher.addURI(AUTHORITY, ARTICLES_CATEGORY_TABLE, ARTICLES_CATEGORY);
        sURIMatcher.addURI(AUTHORITY, ARTICLES_CATEGORY_TABLE + "/*" , ARTICLES_CATEGORY_ID);
    }

    @Override
    public boolean onCreate() {
        myDB = new ViceDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        Cursor cursor = null;

        switch (uriType) {
            case ARTICLES_RECENT:
                //TODOne: Make query for Recent articles auto set to 0
                cursor = myDB.getRecentArticles(null, null);
                break;
            case ARTICLES_RECENT_ID:
                //TODO: Make query for Recent articles
//                cursor = myDB.getRecentArticles(selection, selectionArgs);
                cursor = myDB.getRecentArticles(null, null);
                break;
            case ARTICLES_POPULAR:
                //TODOne: Make query for popular articles auto set to 0
                cursor = myDB.getPopularArticles(null, null);
                break;
            case ARTICLES_POPULAR_ID:
                //TODO: Make query for pop art
//                cursor = myDB.getPopularArticles(uri.getLastPathSegment());
                cursor = myDB.getPopularArticles(null, null);
                break;
            case ARTICLES_CATEGORY:
                cursor = myDB.getCategoryArticles(null, null);
                break;
            case ARTICLES_CATEGORY_ID:
                //TODO: Make query for category art
                cursor = myDB.getCategoryArticles(null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //TODOne: put a switch case based on endpoint of our URI
        String endPoint = "";
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch(uriType){
            case ARTICLES_RECENT:
                id = myDB.addArticleLatest(contentValues);
                endPoint = ARTICLES_RECENT_TABLE;
                break;
            case ARTICLES_RECENT_ID:
                id = myDB.addArticleLatest(contentValues);
                endPoint = ARTICLES_RECENT_TABLE;
//                cursor = myDB.getRecentArticles(selection, selectionArgs);
                break;
            case ARTICLES_POPULAR:
                //TODOne: Make query for popular articles auto set to 0
                id = myDB.addArticlePopular(contentValues);
                endPoint = ARTICLES_POPULAR_TABLE;
                break;
            case ARTICLES_POPULAR_ID:
//                cursor = myDB.getPopularArticles(uri.getLastPathSegment());
                id = myDB.addArticlePopular(contentValues);
                endPoint = ARTICLES_POPULAR_TABLE;
                break;
            case ARTICLES_CATEGORY:
                id = myDB.addArticleCategory(contentValues);
                endPoint = ARTICLES_CATEGORY_TABLE;
                break;
            case ARTICLES_CATEGORY_ID:
                id = myDB.addArticleCategory(contentValues);
                endPoint = ARTICLES_CATEGORY_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(endPoint + "/" + id);
        //TODO: put content resolver notify change method at the end
    }

    @Override
    public int delete(Uri uri, String selection, String[] args) {
        //TODOne: delete both tables
        int rowsDeleted = 0;

        myDB.deleteAllArticlesPopular();
        rowsDeleted = myDB.deleteAllArticlesLatest();
        myDB.deleteAllArticlesCategory();

        getContext().getContentResolver().notifyChange(uri,null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
