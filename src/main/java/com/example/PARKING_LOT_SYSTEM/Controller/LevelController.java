package com.example.PARKING_LOT_SYSTEM.Controller;

import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LevelController {
    @Autowired
    private ParkingServices parkingServices;
    @PostMapping("/addLevel")
    ResponseEntity<Boolean> addLevel(@RequestBody ParkingModel parkingModel)
    {
        Response<Boolean> response = parkingServices.addLevel(parkingModel);
        return null;
    }

    @DeleteMapping("/decreaseLevel")
    ResponseEntity<Integer> deleteLevel()
    {
        Response<Integer> response = parkingServices.deleteLevel();
        return new ResponseEntity<>(response.getReturnObject() , response.getHttpStatus());
    }
}
