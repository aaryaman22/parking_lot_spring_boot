package com.example.PARKING_LOT_SYSTEM.Services.Implementation;

import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Repository.VehicleRepo;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImplementation implements VehicleServices {
    @Autowired
    private VehicleRepo vehicleRepo;

    @Override
    public VehicleModel createAndSave(String registrationNumber, String type, int slotOfLevel)
    {
        VehicleDetails vehicleDetails = new VehicleDetails();
        vehicleDetails.setRegestrationNumber(registrationNumber);
        vehicleDetails.setType(type);
        vehicleDetails.setSlotSrNumber(slotOfLevel);
        vehicleDetails.setLocalDateTime(LocalDateTime.now());
        vehicleRepo.save(vehicleDetails);
        VehicleModel vehicleModel = VehicleModel.builder().regestrationNumber(vehicleDetails.getRegestrationNumber()).type(vehicleDetails.getType()).slotSrNumber(vehicleDetails.getSlotSrNumber()).localDateTime(LocalDateTime.now()).build();
        return vehicleModel;
    }
    @Override
    public Response<VehicleModel> getVehicleInfo(String registrationNumber) {
        Optional<VehicleDetails> vehicleDetails = vehicleRepo.findById(registrationNumber);

        if (vehicleDetails.isEmpty()) return new Response<>(null, HttpStatus.NOT_FOUND);
        return new Response<>(vehicleDetails.map(value -> new VehicleModel(value.getRegestrationNumber(), value.getType(), value.getSlotSrNumber() , value.getLocalDateTime())).orElse(null));
    }

    @Override
    public void deleteVehicleOnSlotId(int slotId) {  // deleting vehicle entity by slot
        Optional<VehicleDetails> vehicalOnSpot = vehicleRepo.findBySlotSrNumber(slotId);
        if (vehicalOnSpot.isPresent()) {
            vehicleRepo.delete(vehicalOnSpot.get());
        }
    }

    @Override
    public List<VehicleModel> getVehicle() {
        List<VehicleDetails> allVehicle = vehicleRepo.findAll();
        return allVehicle.stream().map(x -> new VehicleModel(x.getRegestrationNumber() , x.getType() , x.getSlotSrNumber() , x.getLocalDateTime())).toList();
    }
}
