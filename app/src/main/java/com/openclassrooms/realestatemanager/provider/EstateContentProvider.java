package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;

public class EstateContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public static final String TABLE_ESTATE = Estate.class.getSimpleName();
    public static final String TABLE_ESTATE_ITEM = Estate.class.getSimpleName() + "/#";

    public static final String TABLE_PHOTO = Photo.class.getSimpleName();
    public static final String TABLE_PHOTO_ITEM = Photo.class.getSimpleName() + "/#";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_ESTATE, 1);
        URI_MATCHER.addURI(AUTHORITY, TABLE_PHOTO, 2);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ESTATE_ITEM, 3);
        URI_MATCHER.addURI(AUTHORITY, TABLE_PHOTO_ITEM, 4);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if (getContext() != null) {
            final Cursor cursor;
            switch (URI_MATCHER.match(uri)) {
                case 3:
                    long id = ContentUris.parseId(uri);
                    if (id == 0)
                        cursor = RealEstateDatabase.getInstance(getContext()).estateDao().getAllEstateWithCursor();
                    else
                        cursor = RealEstateDatabase.getInstance(getContext()).estateDao().getEstateWithCursor(id);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    cursor.moveToFirst();
                    return cursor;

                case 4:
                    long estateId = ContentUris.parseId(uri);
                    cursor = RealEstateDatabase.getInstance(getContext()).photoDao().getPicturesWithCursor(estateId);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    cursor.moveToFirst();
                    return cursor;

                default:
                    return null;
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri" + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case 1:
                return "vnd.android.cursor.estate/" + AUTHORITY + "." + TABLE_ESTATE;
            case 2:
                return "vnd.android.cursor.photo/" + AUTHORITY + "." + TABLE_PHOTO;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
