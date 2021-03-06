package com.intrepid.travel.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wwang on 15-03-31.
 */
public class Country implements Serializable{
    public String id;

    public String name;
    public String population;
    public String area;

    public String latitude;

    public String longitude;
    public String countryCode;

    public String currencyCode;
    public String capital;
    public String capitalId;

    public Content content;
    public String images;
    public String localeCode;
    public Time createdAt;
    public Time updatedAt;
    
    public Country (JSONObject obj) throws JSONException {
    	name = obj.getString("name");
    	countryCode = obj.getString("country_code");
    	
		if (obj.has("updated_at")) {
			JSONObject time = obj.getJSONObject("updated_at");
			updatedAt = new Time(time);
		}
		
		if (obj.has("created_at")) {
			JSONObject time = obj.getJSONObject("created_at");
			createdAt = new Time(time);
		}

    }

    public static class Time implements Serializable {
        public String date;
        public String timeZoneType;
        public String timeZone;

        public Time (JSONObject obj) throws JSONException {
            date = obj.getString("date");
            timeZoneType = obj.getString("timezone_type");
            timeZone = obj.getString("timezone");

        }

    }

    public static class Content implements Serializable {
        public String location;

        public Content (JSONObject obj) throws JSONException {
            location = obj.getString("location");
        }
    }
}
