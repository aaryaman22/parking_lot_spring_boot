package com.example.PARKING_LOT_SYSTEM.Controller;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingController {
    @Autowired
    private ParkingServices parkingServices;

    @PostMapping("/parking/{type}/{regNo}")

    ResponseEntity<VehicleModel> addVehicle(@PathVariable("type") String type , @PathVariable("regNo") String registrationNumber)
    {
        Response<VehicleModel> response = parkingServices.addVehicle(type , registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }

    @GetMapping("/statistics")
    ResponseEntity<List<ParkingModel>> getStatics()
    {
        Response<List<ParkingModel>> response= parkingServices.getStatistics();
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }

}
