package com.example.PARKING_LOT_SYSTEM.Services.Implementation;

import com.example.PARKING_LOT_SYSTEM.Entity.Parking;
import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Repository.LevelRepo;
import com.example.PARKING_LOT_SYSTEM.Repository.SlotReop;
import com.example.PARKING_LOT_SYSTEM.Repository.VehicleRepo;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingImplementation implements ParkingServices {
    @Autowired
    private LevelRepo levelRepo;
    @Autowired
    private SlotReop slotReop;
    @Autowired
    private VehicleRepo vehicleRepo;
    public Response<Boolean> addLevel(ParkingModel parkingModel)
    {
        Parking level = new Parking();
        level.setOccupiedBikeSpot(0);
        level.setOccupiedCarSpot(0);
        level.setOccupiedBusSpot(0);

        level.setAvailableBikeSpot(parkingModel.getAvailableBikeSpot());
        level.setAvailableCarSpot(parkingModel.getAvailableCarSpot());
        level.setAvailableBusSpot(parkingModel.getAvailableBusSpot());
        levelRepo.save(level);

        Slot creatingSlot;
        // when we create a new slot id is also created

        for(int i = 0 ; i < level.getAvailableBikeSpot() ; i++)
        {
            creatingSlot = new Slot();
            creatingSlot.setLevelId(level.getId());
            creatingSlot.setSlotNumber(i);
            creatingSlot.setType("bike");
            creatingSlot.setOccupied(false);
            slotReop.save(creatingSlot);
        }

        for(int i = 0 ; i < level.getAvailableCarSpot() ; i++)
        {
            creatingSlot = new Slot();
            creatingSlot.setLevelId(level.getId());
            creatingSlot.setSlotNumber(i);
            creatingSlot.setType("car");
            creatingSlot.setOccupied(false);
            slotReop.save(creatingSlot);
        }

        for(int i = 0 ; i < level.getAvailableBikeSpot() ; i++)
        {
            creatingSlot = new Slot();    // why not global
            creatingSlot.setLevelId(level.getId());
            creatingSlot.setSlotNumber(i);
            creatingSlot.setType("bus");
            creatingSlot.setOccupied(false);
            slotReop.save(creatingSlot);
        }
        return new Response<>(true);
    }

    @Override
    public Response<VehicleModel> addVehicle(String type, String registrationNumber) {
//        slotReop.findByLevelIdAnd (1);
        for(Slot slot : slotReop.findAll())
        {
            if(!slot.getOccupied() && slot.getType().equals(type))
            {
                slot.setOccupied(true);
                Optional<Parking> updateAvailability = levelRepo.findById(slot.getLevelId());
                Parking parking = updateAvailability.get(); //

                switch (type) {
                    case "bike":
                        parking.setOccupiedBikeSpot(parking.getOccupiedBikeSpot()+1);
                        parking.setAvailableBikeSpot(parking.getAvailableBikeSpot()-1);
                        break;
                    case "car":
                        parking.setOccupiedCarSpot(parking.getOccupiedCarSpot()+1);
                        parking.setAvailableCarSpot(parking.getAvailableCarSpot()-1);
                        break;
                    case "bus":
                        parking.setOccupiedBusSpot(parking.getOccupiedBusSpot()+1);
                        parking.setOccupiedBusSpot(parking.getOccupiedBusSpot()-1);
                        break;
                }
                levelRepo.save(parking);

                VehicleDetails vehicleDetails = new VehicleDetails();
                vehicleDetails.setRegestrationNumber(registrationNumber);
                vehicleDetails.setType(type);
                vehicleDetails.setLevelId(slot.getLevelId());
                vehicleDetails.setSlotSrNumber(slot.getId());
                vehicleRepo.save(vehicleDetails);

                VehicleModel vehicleModel = VehicleModel.builder().regestrationNumber(vehicleDetails.getRegestrationNumber()).type(vehicleDetails.getType()).levelId(vehicleDetails.getLevelId()).slotSrNumber(vehicleDetails.getSlotSrNumber()).build();
                return new Response<>(vehicleModel);
            }
        }
        return new Response<>(null , HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public Response<Boolean> removeVehicle(String registrationNumber) {
        Optional<VehicleDetails> vehicalDetails = vehicleRepo.findById(registrationNumber);
        VehicleDetails vehical = vehicalDetails.get();

        int slotSerialNumber = vehical.getSlotSrNumber();
        Optional<Slot> slotDetails = slotReop.findById(slotSerialNumber);
        Slot slot = slotDetails.get();
        slot.setOccupied(false);

            //-----updating available/occupied spots-------//
        Optional<Parking> updateAvailability = levelRepo.findById(slot.getLevelId());
        Parking parking = updateAvailability.get(); //

        String type = slot.getType();
        switch (type) {
            case "bike":
                parking.setOccupiedBikeSpot(parking.getOccupiedBikeSpot() - 1);
                parking.setAvailableBikeSpot(parking.getAvailableBikeSpot() + 1);
                break;
            case "car":
                parking.setOccupiedCarSpot(parking.getOccupiedCarSpot() - 1);
                parking.setAvailableCarSpot(parking.getAvailableCarSpot() + 1);
                break;
            case "bus":
                parking.setOccupiedBusSpot(parking.getOccupiedBusSpot() - 1);
                parking.setOccupiedBusSpot(parking.getOccupiedBusSpot() + 1);
                break;
        }
        vehicleRepo.deleteById(registrationNumber);

        return new Response<>(true);
    }

    @Override
    public Response<VehicleModel> getVehicleInfo(String registrationNumber) {
        Optional<VehicleDetails> vehicleDetails = vehicleRepo.findById(registrationNumber);

        if(vehicleDetails.isEmpty())return new Response<>(null , HttpStatus.NOT_FOUND);
        return new Response<>(vehicleDetails.map(value-> new VehicleModel(value.getRegestrationNumber(),value.getType() ,value.getLevelId() , value.getSlotSrNumber())).orElse(null));
    }

    @Override
    public Response<Integer> deleteLevel() {
        int latestLevel = -1;
        List<Optional<Slot>> myList = new ArrayList<Optional<Slot>>();
        for(Parking parking : levelRepo.findAll())
        {
            latestLevel = parking.getId();
        }
        for(VehicleDetails vehicleDetails:vehicleRepo.findAll())
        {
            if(vehicleDetails.getLevelId()==latestLevel)
            {
                vehicleRepo.delete(vehicleDetails);
            }
        }

        for(Slot slot:slotReop.findAll())
        {
            if(slot.getLevelId()==latestLevel)
            {
                slotReop.delete(slot);
            }
        }

        Optional<Parking> lastLevelInfo = levelRepo.findById(latestLevel);
        //Parking dmo = levelRepo.findById(latestLevel).get(); jus a try
        Parking lastLavel = lastLevelInfo.get();
        levelRepo.delete(lastLavel);

        if(latestLevel==-1)return new Response<>(null , HttpStatus.BAD_REQUEST);
        return  new Response<>(latestLevel , HttpStatus.ACCEPTED);
    }

    @Override
    public Response<List<ParkingModel>> getStatistics() {
        List<ParkingModel> myList =new ArrayList<ParkingModel>();
        for(Parking parking : levelRepo.findAll())
        {
            ParkingModel parkingModel = ParkingModel.builder().id(parking.getId()).occupiedBikeSpot(parking.getOccupiedBikeSpot()).occupiedCarSpot(parking.getOccupiedCarSpot()).occupiedBusSpot(parking.getOccupiedBusSpot()).availableBikeSpot(parking.getAvailableBikeSpot()).availableCarSpot(parking.getAvailableCarSpot()).availableBusSpot(parking.getAvailableBusSpot()).build();
            myList.add(parkingModel);
        }
        if(myList.size()>0)return new Response<>(myList);
        return new Response<>(null , HttpStatus.NO_CONTENT);
    }

}
