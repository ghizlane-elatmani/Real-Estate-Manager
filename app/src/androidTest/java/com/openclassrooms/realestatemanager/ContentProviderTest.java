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
public class ContentProviderTest {

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
        assertThat(cursor.getCount(), is(greaterThan(2)));

    }


    @Test
    public void getEstateThanksToID() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_ESTATE, 1), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());

        assertEquals(1, cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        assertEquals("penthouse", cursor.getString(cursor.getColumnIndexOrThrow("type")));
        assertEquals(450000, cursor.getInt(cursor.getColumnIndexOrThrow("price")));
        assertEquals(200, cursor.getInt(cursor.getColumnIndexOrThrow("surface")));
        assertEquals(4, cursor.getInt(cursor.getColumnIndexOrThrow("number_rooms")));
        assertEquals("House in New York with garage. Close to all amenities. It has 3 large bedrooms, 2 bathrooms and a large living room.", cursor.getString(cursor.getColumnIndexOrThrow("description")));
        assertEquals("7409 West Sussex Lane Bay Shore", cursor.getString(cursor.getColumnIndexOrThrow("address")));
        assertEquals(11706, cursor.getInt(cursor.getColumnIndexOrThrow("zipCode")));
        assertEquals("NY", cursor.getString(cursor.getColumnIndexOrThrow("city")));
        assertEquals(40.711100, cursor.getDouble(cursor.getColumnIndexOrThrow("lat")), 0);
        assertEquals(-73.253030, cursor.getDouble(cursor.getColumnIndexOrThrow("lng")), 0);
        assertEquals("Transportation, school", cursor.getString(cursor.getColumnIndexOrThrow("points_interest")));
        assertEquals("Oliver Queen", cursor.getString(cursor.getColumnIndexOrThrow("agent_name")));
        assertEquals(6, cursor.getInt(cursor.getColumnIndexOrThrow("number_picture")));

        cursor.close();
    }


    @Test
    public void getPhotosThanksToID() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(URI_PHOTO, 1), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertEquals(6, cursor.getCount());
        assertTrue(cursor.moveToFirst());

        assertEquals("android.resource://com.openclassrooms.realestatemanager/drawable/house",cursor.getString(cursor.getColumnIndexOrThrow("uri")));
        assertEquals("Facade",cursor.getString(cursor.getColumnIndexOrThrow("label")));
        assertEquals(1,cursor.getLong(cursor.getColumnIndexOrThrow("estate_id")));

        cursor.moveToNext();

        assertEquals("android.resource://com.openclassrooms.realestatemanager/drawable/house_g",cursor.getString(cursor.getColumnIndexOrThrow("uri")));
        assertEquals("Garage",cursor.getString(cursor.getColumnIndexOrThrow("label")));
        assertEquals(1,cursor.getLong(cursor.getColumnIndexOrThrow("estate_id")));

        cursor.close();
    }

}

