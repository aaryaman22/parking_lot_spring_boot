package com.example.PARKING_LOT_SYSTEM.Controller;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class InformationController {
    @Autowired
    private ParkingServices parkingServices;
    @GetMapping("/getinfo/{regNo}")
    ResponseEntity<VehicleModel> getInfo(@PathVariable("regNo") String registrationNumber)
    {
        Response<VehicleModel> response = parkingServices.getVehicleInfo(registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }

    @GetMapping("/statistics")
    ResponseEntity<List<ParkingModel>> getStatics()
    {
        Response<List<ParkingModel>> response= parkingServices.getStatistics();
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }
}
