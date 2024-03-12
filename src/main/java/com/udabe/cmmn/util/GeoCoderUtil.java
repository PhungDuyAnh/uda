package com.udabe.cmmn.util;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoCoderUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(GeoCoderUtil.class);
    public static final String GoogleApiKey = "AIzaSyCIfvqaX0-fKsfx9pYbFChlHjaHPAriISk";
    public static com.google.maps.model.LatLng parseLocation(String locationName) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(GoogleApiKey).build();
        LOGGER.info("Parsing address for: {}", locationName);
        try {
            GeocodingResult[] request = GeocodingApi.newRequest(context).address(locationName).await();
            LatLng location = request[0].geometry.location;
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

