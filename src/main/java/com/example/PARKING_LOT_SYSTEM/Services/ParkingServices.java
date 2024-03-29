package com.example.PARKING_LOT_SYSTEM.Services;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ParkingServices {
    Response<Boolean> addLevel(ParkingModel parkingModel);

    Response<VehicleModel> addVehicle(String type , String registrationNumber);

    public Response<Boolean> removeVehicle(String registrationNumber);
    void updateAvailibilityInLevel(int levelId , String type);
    //
    // Response<Boolean> removeVehicle(String registrationNumber);
    Response<Integer> deleteLevel();

    Response<List<ParkingModel>> getStatistics();
}
