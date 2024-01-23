package com.example.PARKING_LOT_SYSTEM.Executor;

import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class Executor {
    @Autowired
    VehicleServices vehicleServices;

    @Autowired
    ParkingServices parkingServices;
    private LocalDateTime currLocalDateTime;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void forceUnpark() // yet to be implmented
    {
        List<VehicleModel> vehicles = vehicleServices.getVehicle();
        for (VehicleModel vehicle : vehicles)
        {
            LocalDateTime value = vehicle.getLocalDateTime().minus(Duration.ofMinutes(2));
            currLocalDateTime = LocalDateTime.now();
            if(value.isAfter(currLocalDateTime))
            {
                System.out.println(vehicle.getSlotSrNumber());
                log.info("--------------Unparking Vehicle-------------\n" + vehicle.getSlotSrNumber());
                parkingServices.removeVehicle(vehicle.getRegestrationNumber());
            }
        }
    }
}
