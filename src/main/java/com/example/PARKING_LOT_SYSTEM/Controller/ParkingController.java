package com.example.PARKING_LOT_SYSTEM.Controller;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingController {
    @Autowired
    private ParkingServices parkingServices;


    @PostMapping("/addLevel")
    ResponseEntity<Boolean> addLevel(@RequestBody ParkingModel parkingModel)
    {
        Response<Boolean> response = parkingServices.addLevel(parkingModel);
        return null;
    }
    @PostMapping("/parking/{type}/{regNo}")

    ResponseEntity<VehicleModel> addVehicle(@PathVariable("type") String type , @PathVariable("regNo") String registrationNumber)
    {
        Response<VehicleModel> response = parkingServices.addVehicle(type , registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }
    @DeleteMapping("/unpark/{regNo}")
    ResponseEntity<Boolean> unparkVehicle(@PathVariable("regNo") String registrationNumber)
    {
        Response<Boolean> response = parkingServices.removeVehicle(registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }

    @GetMapping("/getinfo/{regNo}")
    ResponseEntity<VehicleModel> getInfo(@PathVariable("regNo") String registrationNumber)
    {
        Response<VehicleModel> response = parkingServices.getVehicleInfo(registrationNumber);
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }
    @DeleteMapping("/decreaseLevel")
    ResponseEntity<Integer> deleteLevel()
    {
        Response<Integer> response = parkingServices.deleteLevel();
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }
    @GetMapping("/statistics")
    ResponseEntity<List<ParkingModel>> getStatics()
    {
        Response<List<ParkingModel>> response= parkingServices.getStatistics();
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }

}
