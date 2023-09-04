package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {
    HashMap<String, Hotel> hotelDb = new HashMap<>();
    HashMap<Integer, User> userDb = new HashMap<>();
    HashMap<Integer, Booking> bookingsDb = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName() == null || hotel == null){
            return "FAILURE";
        }

        String hotelName = hotel.getHotelName();
        if(hotelDb.containsKey(hotelName)){
            return "FAILURE";
        }else{
            hotelDb.put(hotelName, hotel);
        }
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        int id = user.getaadharCardNo();
        userDb.put(id, user);
        return id;
    }

    public String getHotelWithMostFacilities() {
        String hotelWithMostFacilities = "";
        int mostNumOfFacilities = 0;

        for(String hotelName: hotelDb.keySet()){
            int numOfFacilities = hotelDb.get(hotelName).getFacilities().size();
            if(numOfFacilities > mostNumOfFacilities){
                hotelWithMostFacilities = hotelName;
            }else if(numOfFacilities == mostNumOfFacilities){
                int result = hotelWithMostFacilities.compareTo(hotelName);
                if(result >= 0){
                    hotelWithMostFacilities = hotelName;
                }
            }
        }
        return hotelWithMostFacilities;
    }

    public int bookARoom(Booking booking) {
        Hotel hotel = hotelDb.get(booking.getHotelName());

        int availableRooms = hotel.getAvailableRooms();
        int requiredRooms = booking.getNoOfRooms();
        if(requiredRooms > availableRooms) return -1;

        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);

        int amountToBePaid = requiredRooms * (hotel.getPricePerNight());
        booking.setAmountToBePaid(amountToBePaid);

        int adharId = booking.getBookingAadharCard();
        bookingsDb.put(adharId, booking);

        return amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        if(bookingsDb.containsKey(aadharCard)){
            return bookingsDb.get(aadharCard).getBookingAadharCard();
        }
        return aadharCard;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility> oldFacilities = hotelDb.get(hotelName).getFacilities();

        for(Facility newFacility : newFacilities){
            boolean isFacilityFound = false;
            for(Facility oldFacility : oldFacilities){
                if(oldFacility.equals(newFacility)){
                    isFacilityFound = true;
                    break;
                }
            }
            if(!isFacilityFound){
                oldFacilities.add(newFacility);
            }
        }
        return hotelDb.get(hotelName);
    }
}
