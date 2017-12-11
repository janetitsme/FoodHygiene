package com.example.janet.foodhygiene;

/**
 * Created by janet on 19/04/16.
 */
public class Restaurant {
    private int id;
    private String BusinessName;
    private String AddressLine1;
    private String AddressLine2;
    private String AddressLine3;
    private String PostCode;
    private int RatingValue;
    private String RatingDate;
    private double Latitude;
    private double Longitude;

    public Restaurant(int id, String businessName, String addressLine1, String addressLine2, String addressLine3, String postcode, int ratingValue, String ratingDate, Double latitude, Double longitude)
    {
        this.id=id;
        BusinessName=businessName;
        AddressLine1=addressLine1;
        AddressLine2=addressLine2;
        AddressLine3=addressLine3;
        PostCode=postcode;
        RatingValue=ratingValue;
        RatingDate=ratingDate;
        Latitude=latitude;
        Longitude=longitude;

    }
public Restaurant() {

}

   /* public String toString()
    {
        return "Restaurant{"+ "id="+id+", BusinessName='"+BusinessName+"', Address1='"+AddressLine1+
                "', AddressLine2='"+AddressLine2+"',AddressLine3='"+AddressLine3+"',PostCode='"+
                PostCode+"',RatingValue="+RatingValue+",RatingDate='"+RatingDate+"'}";
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return AddressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        AddressLine3 = addressLine3;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public int getRatingValue() {
        return RatingValue;
    }

    public void setRatingValue(int ratingValue) {
        RatingValue = ratingValue;
    }

    public String getRatingDate() {
        return RatingDate;
    }

    public void setRatingDate(String ratingDate) {
        RatingDate = ratingDate;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
