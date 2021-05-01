package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openclassrooms.realestatemanager.provider.EstateContentProvider.AUTHORITY;
import static com.openclassrooms.realestatemanager.provider.EstateContentProvider.TABLE_ESTATE;
import static com.openclassrooms.realestatemanager.provider.EstateContentProvider.TABLE_PHOTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EstateContentProviderTest {

    private ContentResolver contentResolver;
    private RealEstateDatabase database;
    public static final Uri URI_ESTATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ESTATE);
    public static final Uri URI_PHOTO = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PHOTO);

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
    }

    @After
    public void closeDb(){
        database.close();
    }


    @Test
    public void getNoEstateWhenIDWrong() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_ESTATE, 250000), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }


    @Test
    public void getAllEstate(){
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_ESTATE,0),null,null,null,null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(greaterThan(5)));

    }


    @Test
    public void getEstateThanksToID() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_ESTATE, 1), null, null, null, null);

        //TODO: finish the method

        cursor.close();
    }


    @Test
    public void getPhotosThanksToID() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_PHOTO, 1), null, null, null, null);

        //TODO: finish the method

        cursor.close();
    }

}

