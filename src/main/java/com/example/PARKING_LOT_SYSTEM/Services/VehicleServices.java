package com.example.PARKING_LOT_SYSTEM.Services;

import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface VehicleServices {
    public VehicleModel createAndSave(String regNo , String type , int slotSerialNo);
    public Response<VehicleModel> getVehicleInfo(String registrationNumber);

    public void deleteVehicleOnSlotId(int slotId);
}
