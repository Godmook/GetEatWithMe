package OSS.geteatwithme.Connection;

import android.location.Address;

import com.google.gson.annotations.SerializedName;


import java.util.List;

public class ResultSearchKeyword {
    @SerializedName("documents")
    public List<Document> documentList;
    @SerializedName("meta")
    public Meta meta;

    public static class Document{
        @SerializedName("address")
        private Address address;
        @SerializedName("road_address_name")
        private String road_address_name;
        @SerializedName("x")
        private String x;
        @SerializedName("y")
        private String y;
        @SerializedName("id")
        private String id;
        @SerializedName("distance")
        private String distance;
        @SerializedName("place_name")
        private String place_name;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public String getRoad_address_name() {
            return road_address_name;
        }

        public void setRoad_address_name(String road_address_name) {
            this.road_address_name = road_address_name;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getPlace_name() {
            return place_name;
        }

        public void setPlace_name(String place_name) {
            this.place_name = place_name;
        }
    }

    public static class Meta{
        @SerializedName("is_end")
        private boolean is_end;
        @SerializedName("pageable_count")
        private int pageable_count;
        @SerializedName("total_count")
        private int total_count;

        public boolean isIs_end() {
            return is_end;
        }

        public void setIs_end(boolean is_end) {
            this.is_end = is_end;
        }

        public int getPageable_count() {
            return pageable_count;
        }

        public void setPageable_count(int pageable_count) {
            this.pageable_count = pageable_count;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }
    }
}