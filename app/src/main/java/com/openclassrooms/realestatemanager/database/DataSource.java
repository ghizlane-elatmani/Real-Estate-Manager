package com.openclassrooms.realestatemanager.database;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.DateUtils;

public class DataSource {

    @NonNull
    public static Agent[] getAllAgent() {
        return new Agent[]{
                new Agent(1, "Alexandre", "Lemaitre", "alexandre_lemaitre@gmail.com"),
                new Agent(2, "Laurine", "Lance", "laurine_lance@gmail.com"),
                new Agent(3, "Marie", "Flash", "marie_flash@gmail.com"),
                new Agent(4, "Oliver", "Queen", "oliver_queen@gmail.com"),
        };
    }

    public static Estate[] getAllEstate() {
        return new Estate[]{
                new Estate("Manor",
                        1399000,
                        "5435 ARLINGTON AVENUE, NORTH RIVERDALE, BRONX, NY",
                        30,
                        4,
                        "Lovely brick split level one family home in the estate section of Riverdale. Located in a Cul de sac off of 254th st and Arlington Ave. This home is a total renovation in mint move in condition.The house has all new windows throughout. Large living room with wood burning fireplace. Central air throughout.Dining room has a bay window. Kitchen has all stainless steel appliances and a moveable center island. Master bedroom has en suite bathroom. 2 other large bedrooms on this floor and another full bathroom. Lower level (street level) has separate entrance. Here you have forth bedroom, full bathroom, family room, laundry room entrance to the 2 car garage.Lovely patio and garden out back with entrance from the family room.",
                        "https://img-de.gtsstatic.net/reno/imagereader.aspx?imageurl=http%3A%2F%2Fmmsmh.gothamphotocompany.com%2FMedia%2FPhotography%2FDEGI%2F785256%2Fb8461f2d-fff6-4c56-b9b2-7c1a849bd8c2.jpg%3Fdate%3D2020-12-23&option=N&idlisting=listingmedia&w=1024&permitphotoenlargement=false&fallbackimageurl=https%3A%2F%2Fstatic-ind-elliman-newyorkcity-production.gtsstatic.net%2Fresources%2Fsiteresources%2Fcommonresources%2Fimages%2Fnophoto%2Fde_website_holder_1600x600.jpg",
                        "Pedestrian street, immediate proximity to shops and transport.",
                        "Sold",
                        DateUtils.convertStringToDate("07/10/2020"),
                        DateUtils.convertStringToDate("02/02/2021"),
                        1),

                new Estate("Penthouse",
                        749000,
                        "1611 Plymouth Ave, BRONX, NY 10461",
                        200,
                        5,
                        "Large families will appreciate 1611 Plymouth Avenue, a state of the art newly renovated single family townhouse offering over 3,000 sq/ft of living space! Sitting on a 25x100 lot featuring a wide private driveway, and garage for 2 cars! Nestled on a beautiful tree lined street of Pehlam Bay.\n" +
                                "Expansive sun drenched living area provides great space for entertaining. Spacious formal dining area leads into eat in kitchen any chef will love. Chef inspired granite kitchen features custom floor to ceiling cabinetry, boast full sized stainless steel appliances & leads out into lush rear yard. King sized bedrooms equipped with ample closet space. Fully tiled bathrooms boast state of the art wall & floor tiles & much more! Must see to appreciate!\n" +
                                "Renovations include brand new hardwood flooring, recessed lighting, electrical, heating, plumbing , AC systems!\n" +
                                "Full finished basement can be used as storage space, or additional recreational space.",
                        "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/418ec6a4ed4029b346a7c3e0bd27fe32-full.webp",
                        "1611 Plymouth avenue is conveniently located with close proximity to major transportation which makes commuting a breeze, schools, shopping centers, highways, restaurants, cafes, parks and many other neighborhood amenities.",
                        "Available",
                        DateUtils.convertStringToDate("07/10/2020"),
                        null,
                        1),

        };
    }


}
