package com.example.tep_timeshareexchangeplatform.Until.EmumClass

import android.content.Context
import com.example.tep_timeshareexchangeplatform.R


enum class MapsAmenityType(val type: String, val iconResId: Int) {
    SCHOOL("school", R.drawable.ic_school),
    HOSPITAL("hospital", R.drawable.ic_hospital),
    RESTAURANT("restaurant", R.drawable.ic_restaurant),
    CAFE("cafe", R.drawable.ic_cafe),
    BAR("bar", R.drawable.ic_bar),
    PARKING("parking", R.drawable.ic_parking),
    BANK("bank", R.drawable.ic_bank),
    ATM("atm", R.drawable.ic_atm),
    BUS_STATION("bus_station", R.drawable.ic_bus_station),
    FUEL("fuel", R.drawable.ic_fuel),
    LIBRARY("library", R.drawable.ic_library),
    POST_OFFICE("post_office", R.drawable.ic_post_office),
    POLICE("police", R.drawable.ic_police),
    FIRE_STATION("fire_station", R.drawable.ic_fire_station),
    PLACE_OF_WORSHIP("place_of_worship", R.drawable.ic_place_of_worship),
    TOILETS("toilets", R.drawable.ic_toilets),
    FOUNTAIN("fountain", R.drawable.ic_fountain),
    TOWNHALL("townhall", R.drawable.ic_townhall),
    CINEMA("cinema", R.drawable.ic_cinema),
    THEATRE("theatre", R.drawable.ic_theatre),
    PARK("park", R.drawable.ic_park),
    PLAYGROUND("playground", R.drawable.ic_play_ground),
    SPORTS_CENTRE("sports_centre", R.drawable.ic_sports_centre),
    STADIUM("stadium", R.drawable.ic_stadium),
    MARKETPLACE("marketplace", R.drawable.ic_marketplace),
    SUPERMARKET("supermarket", R.drawable.ic_supermarkets),
    PHARMACY("pharmacy", R.drawable.ic_pharmacy),
    BICYCLE_RENTAL("bicycle_rental", R.drawable.ic_bicycle_rental),
    CAR_RENTAL("car_rental", R.drawable.ic_car_rental),
    HOTEL("hotel", R.drawable.ic_hotel),
    MOTEL("motel", R.drawable.ic_motel),
    COLLEGE("college", R.drawable.ic_college),
    UNIVERSITY("university", R.drawable.ic_university),
    KINDERGARTEN("kindergarten", R.drawable.ic_kindergarten),
    CLINIC("clinic", R.drawable.ic_clinic),
    CHARGING_STATION("charging_station", R.drawable.ic_charging_station),
    COMMUNITY_CENTRE("community_centre", R.drawable.ic_community_centre),
    RECYCLING("recycling", R.drawable.ic_recycling),


    CONVENIENCE("convenience", R.drawable.ic_convenience),
    BAKERY("bakery", R.drawable.ic_bakery),
    BUTCHER("butcher", R.drawable.ic_butcher),
    BEVERAGES("beverages", R.drawable.ic_beverages),
    DELI("deli", R.drawable.ic_deli),
    ALCOHOL("alcohol", R.drawable.ic_alcohol),
    CLOTHES("clothes", R.drawable.ic_clothes),
    SHOES("shoes", R.drawable.ic_shoes),
    JEWELRY("jewelry", R.drawable.ic_jewelry),
    ELECTRONICS("electronics", R.drawable.ic_electronics),
    MOBILE_PHONE("mobile_phone", R.drawable.ic_mobile_phone),
    FURNITURE("furniture", R.drawable.ic_furniture),
    HARDWARE("hardware", R.drawable.ic_hardware),
    GARDEN_CENTRE("garden_centre", R.drawable.ic_garden_centre),
    PET("pet", R.drawable.ic_pet_shop),
    BOOKS("books", R.drawable.ic_shop_books),
    STATIONERY("stationery", R.drawable.ic_shop_stationery),
    TOYS("toys", R.drawable.ic_shop_toys),
    GIFT("gift", R.drawable.ic_shop_gift),
    COSMETICS("cosmetics", R.drawable.ic_shop_cosmetics);

    /**
     * Lấy tên hiển thị từ string resources
     */


    companion object {
        /**
         * Tìm kiếm `MapsAmenityType` dựa trên giá trị `type`
         */
        fun fromValue(value: String): MapsAmenityType? {
            return values().find { it.type == value }
        }
    }
    fun getDisplayName(context: Context): String {
        return context.getString(
            when (this) {
                SCHOOL -> R.string.amenity_school
                HOSPITAL -> R.string.amenity_hospital
                RESTAURANT -> R.string.amenity_restaurant
                CAFE -> R.string.amenity_cafe
                BAR -> R.string.amenity_bar
                PARKING -> R.string.amenity_parking
                BANK -> R.string.amenity_bank
                ATM -> R.string.amenity_atm
                BUS_STATION -> R.string.amenity_bus_station
                FUEL -> R.string.amenity_fuel
                LIBRARY -> R.string.amenity_library
                POST_OFFICE -> R.string.amenity_post_office
                POLICE -> R.string.amenity_police
                FIRE_STATION -> R.string.amenity_fire_station
                PLACE_OF_WORSHIP -> R.string.amenity_place_of_worship
                TOILETS -> R.string.amenity_toilets
                FOUNTAIN -> R.string.amenity_fountain
                TOWNHALL -> R.string.amenity_townhall
                CINEMA -> R.string.amenity_cinema
                THEATRE -> R.string.amenity_theatre
                PARK -> R.string.amenity_park
                PLAYGROUND -> R.string.amenity_playground
                SPORTS_CENTRE -> R.string.amenity_sports_centre
                STADIUM -> R.string.amenity_stadium
                MARKETPLACE -> R.string.amenity_marketplace
                SUPERMARKET -> R.string.amenity_supermarket
                PHARMACY -> R.string.amenity_pharmacy
                BICYCLE_RENTAL -> R.string.amenity_bicycle_rental
                CAR_RENTAL -> R.string.amenity_car_rental
                HOTEL -> R.string.amenity_hotel
                MOTEL -> R.string.amenity_motel
                COLLEGE -> R.string.amenity_college
                UNIVERSITY -> R.string.amenity_university
                KINDERGARTEN -> R.string.amenity_kindergarten
                CLINIC -> R.string.amenity_clinic
                CHARGING_STATION -> R.string.amenity_charging_station
                COMMUNITY_CENTRE -> R.string.amenity_community_centre
                RECYCLING -> R.string.amenity_recycling
                CONVENIENCE -> R.string.shop_convenience
                BAKERY -> R.string.shop_bakery
                BUTCHER -> R.string.shop_butcher
                BEVERAGES -> R.string.shop_beverages
                DELI -> R.string.shop_deli
                ALCOHOL -> R.string.shop_alcohol
                CLOTHES -> R.string.shop_clothes
                SHOES -> R.string.shop_shoes
                JEWELRY -> R.string.shop_jewelry
                ELECTRONICS -> R.string.shop_electronics
                MOBILE_PHONE -> R.string.shop_mobile_phone
                FURNITURE -> R.string.shop_furniture
                HARDWARE -> R.string.shop_hardware
                GARDEN_CENTRE -> R.string.shop_garden_centre
                PET -> R.string.shop_pet
                BOOKS -> R.string.shop_books
                STATIONERY -> R.string.shop_stationery
                TOYS -> R.string.shop_toys
                GIFT -> R.string.shop_gift
                COSMETICS -> R.string.shop_cosmetics
            }
        )
    }
}