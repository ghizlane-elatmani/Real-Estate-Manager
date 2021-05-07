package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EstateDaoTest {

    private RealEstateDatabase database;

    private static final Estate estate = new Estate("penthouse", 550000, 150, 3,
            "House in New York with garage.", "9735 Carson Ave. Staten Island", 10306, "NY",40.573660, -74.112530,
            "Transportation, school, park", false, null, null, "Thea Queen", 0);

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }


    @Test
    public void insertAndGetRealEstate() throws InterruptedException {
        database.estateDao().insertEstate(estate);

        List<Estate> estateList = LiveDataTestUtil.getValue(database.estateDao().getAllEstate());

        Estate estate1 = estateList.get(0);
        assertEquals(estate.getType(), estate1.getType());
        assertEquals(estate.getPrice(), estate1.getPrice());
        assertEquals(estate.getSurface(), estate1.getSurface());
        assertEquals(estate.getNumber_rooms(), estate1.getNumber_rooms());
        assertEquals(estate.getDescription(), estate1.getDescription());
        assertEquals(estate.getAddress(), estate1.getAddress());
        assertEquals(estate.getZipCode(), estate1.getZipCode());
        assertEquals(estate.getCity(), estate1.getCity());
        assertEquals(estate.getPoints_interest(), estate1.getPoints_interest());
    }
}

