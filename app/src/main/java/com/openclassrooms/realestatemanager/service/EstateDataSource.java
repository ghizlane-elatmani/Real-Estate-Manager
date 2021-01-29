package com.openclassrooms.realestatemanager.service;

import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.DateUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EstateDataSource {

    public static List<Estate> FAKE_ESTATES = Arrays.asList(
            new Estate(1, "Duplex", 420000, "426 Evergreen St. Brooklyn, NY 11233", 30, 2, "Real Estate Manager offers you a studio full of charm in duplex, in one of the most famous streets of NYC. It is made up of: a fitted AND equipped american kitchen open to the living room, a bathroom with WC, a cozy bedroom on the mezzanine as well as an office corner. Visit without delay.", "https://photo.superimmo.com/photos/listings/f/c/8/7/6/fc876af3e39ecce51b7cddccdac059a0b65f9c3c-widest.jpg", "Pedestrian street, immediate proximity to shops and transport.", "Disponible",
                    DateUtils.convertStringToDate("07/10/2020"), new Date(), 1),

            new Estate(2, "Duplex", 420000, "426 Evergreen St. Brooklyn, NY 11233", 30, 2, "Real Estate Manager offers you a studio full of charm in duplex, in one of the most famous streets of NYC. It is made up of: a fitted AND equipped american kitchen open to the living room, a bathroom with WC, a cozy bedroom on the mezzanine as well as an office corner. Visit without delay.", "https://photo.superimmo.com/photos/listings/f/c/8/7/6/fc876af3e39ecce51b7cddccdac059a0b65f9c3c-widest.jpg", "Pedestrian street, immediate proximity to shops and transport.", "Disponible",
                    new Date(), new Date(), 1),

            new Estate(3, "Duplex", 420000, "426 Evergreen St. Brooklyn, NY 11233", 30, 2, "Real Estate Manager offers you a studio full of charm in duplex, in one of the most famous streets of NYC. It is made up of: a fitted AND equipped american kitchen open to the living room, a bathroom with WC, a cozy bedroom on the mezzanine as well as an office corner. Visit without delay.", "https://photo.superimmo.com/photos/listings/f/c/8/7/6/fc876af3e39ecce51b7cddccdac059a0b65f9c3c-widest.jpg", "Pedestrian street, immediate proximity to shops and transport.", "Disponible",
                    new Date(), new Date(), 1),

            new Estate(4, "Duplex", 420000, "426 Evergreen St. Brooklyn, NY 11233", 30, 2, "Real Estate Manager offers you a studio full of charm in duplex, in one of the most famous streets of NYC. It is made up of: a fitted AND equipped american kitchen open to the living room, a bathroom with WC, a cozy bedroom on the mezzanine as well as an office corner. Visit without delay.", "https://photo.superimmo.com/photos/listings/f/c/8/7/6/fc876af3e39ecce51b7cddccdac059a0b65f9c3c-widest.jpg", "Pedestrian street, immediate proximity to shops and transport.", "Disponible",
                    new Date(), new Date(), 1)
    );

}
