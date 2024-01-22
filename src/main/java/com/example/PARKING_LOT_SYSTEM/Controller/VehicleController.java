package com.example.PARKING_LOT_SYSTEM.Controller;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleController {
    @Autowired
    private VehicleServices vehicleServices;
    @GetMapping("/getinfo/{regNo}")
    ResponseEntity<VehicleModel> getInfo(@PathVariable("regNo") String registrationNumber)
    {
        Response<VehicleModel> response = vehicleServices.getVehicleInfo(registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }


}
