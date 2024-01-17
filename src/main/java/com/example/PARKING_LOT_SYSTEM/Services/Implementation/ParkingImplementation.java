package com.example.PARKING_LOT_SYSTEM.Services.Implementation;

import com.example.PARKING_LOT_SYSTEM.Entity.Parking;
import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.SlotModel;
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

    public Slot InitSpot(int levelId , int i ,String type)
    {
        Slot creatingSlot = new Slot();
        creatingSlot.setLevelId(levelId);
        creatingSlot.setSlotNumber(i);
        creatingSlot.setType(type);
        creatingSlot.setOccupied(false);
        return creatingSlot;
    }

    @Override
    public Response<Boolean> addLevel(ParkingModel parkingModel) {
        Parking level = new Parking();
        level.setOccupiedBikeSpot(0);
        level.setOccupiedCarSpot(0);
        level.setOccupiedBusSpot(0);

        level.setAvailableBikeSpot(parkingModel.getAvailableBikeSpot());
        level.setAvailableCarSpot(parkingModel.getAvailableCarSpot());
        level.setAvailableBusSpot(parkingModel.getAvailableBusSpot());
        levelRepo.save(level);

        List<Slot> myList= new ArrayList<>();
        for (int i = 0; i < level.getAvailableBikeSpot(); i++) {
            myList.add(InitSpot(level.getId() , i , "bike"));
        }
        for (int i = 0; i < level.getAvailableCarSpot(); i++) {
            myList.add(InitSpot(level.getId() , i , "car"));
        }
        for (int i = 0; i < level.getAvailableBusSpot(); i++) {
            myList.add(InitSpot(level.getId() , i , "bus"));
        }
        slotReop.saveAll(myList);
        return new Response<>(true);
    }

    @Override
    public Response<VehicleModel> addVehicle(String type, String registrationNumber) {
//      slotReop.findByLevelIdAnd (1);
        Optional<Slot> getSolt = slotReop.findFirstByTypeAndOccupiedFalse(type);
        if (getSolt.isPresent()) {
            Slot slot = getSolt.get();
            slot.setOccupied(true);
            Optional<Parking> updateAvailability = levelRepo.findById(slot.getLevelId());
            Parking parking = updateAvailability.get(); //

            switch (type) {
                case "bike":
                    parking.setOccupiedBikeSpot(parking.getOccupiedBikeSpot() + 1);
                    parking.setAvailableBikeSpot(parking.getAvailableBikeSpot() - 1);
                    break;
                case "car":
                    parking.setOccupiedCarSpot(parking.getOccupiedCarSpot() + 1);
                    parking.setAvailableCarSpot(parking.getAvailableCarSpot() - 1);
                    break;
                case "bus":
                    parking.setOccupiedBusSpot(parking.getOccupiedBusSpot() + 1);
                    parking.setOccupiedBusSpot(parking.getOccupiedBusSpot() - 1);
                    break;
            }
            levelRepo.save(parking);

            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setRegestrationNumber(registrationNumber);
            vehicleDetails.setType(type);
            vehicleDetails.setSlotSrNumber(slot.getId());
            vehicleRepo.save(vehicleDetails);

            VehicleModel vehicleModel = VehicleModel.builder().regestrationNumber(vehicleDetails.getRegestrationNumber()).type(vehicleDetails.getType()).slotSrNumber(vehicleDetails.getSlotSrNumber()).build();
            return new Response<>(vehicleModel);
        }

        return new Response<>(null, HttpStatus.SERVICE_UNAVAILABLE);
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

        if (vehicleDetails.isEmpty()) return new Response<>(null, HttpStatus.NOT_FOUND);
        return new Response<>(vehicleDetails.map(value -> new VehicleModel(value.getRegestrationNumber(), value.getType(), value.getSlotSrNumber())).orElse(null));
    }

    @Override
    public Response<Integer> deleteLevel() {

        Parking lastLevel = levelRepo.findFirstByOrderByIdDesc();
        List<Slot> slotList = slotReop.findAllByLevelId(lastLevel.getId());// list of slot entities
        System.out.println(slotList);
        for (int i = 0; i < slotList.size(); i++) {
            Slot particularLevelSlot = slotList.get(i);
            Integer serialNumberOfSlot = particularLevelSlot.getId();
            Optional<VehicleDetails> vehicalOnSpot = vehicleRepo.findBySlotSrNumber(serialNumberOfSlot);
            if (vehicalOnSpot.isPresent()) {
                vehicleRepo.delete(vehicalOnSpot.get());
            }
        }
        slotReop.deleteAll(slotList);
        levelRepo.deleteById(lastLevel.getId());

        if (lastLevel.getId() == -1) return new Response<>(null, HttpStatus.BAD_REQUEST);
        return new Response<>(lastLevel.getId(), HttpStatus.ACCEPTED);
    }

    @Override
    public Response<List<ParkingModel>> getStatistics() {
        List<ParkingModel> myList = new ArrayList<ParkingModel>();
        List<Parking> parkings = levelRepo.findAllLevels();
        for (Parking parking : parkings) {
            ParkingModel parkingModel = ParkingModel.builder().id(parking.getId()).slotList(listOfSlotModels(parking.getSlotsList())).occupiedBikeSpot(parking.getOccupiedBikeSpot()).occupiedCarSpot(parking.getOccupiedCarSpot()).occupiedBusSpot(parking.getOccupiedBusSpot()).availableBikeSpot(parking.getAvailableBikeSpot()).availableCarSpot(parking.getAvailableCarSpot()).availableBusSpot(parking.getAvailableBusSpot()).build();
            myList.add(parkingModel);
        }

        for(int i=0;i<parkings.size();i++){
            Parking parking=parkings.get(i);
        }
        if (myList.size() > 0) return new Response<>(myList);
        return new Response<>(null, HttpStatus.NO_CONTENT);
    }

    private List<SlotModel> listOfSlotModels(List<Slot> list) {

        return list.stream().map(x -> new SlotModel(x.getId(), x.getSlotNumber(), x.getLevelId(), x.getType(), x.getOccupied(),
               Optional.ofNullable(x.getVehicleDetails()).map(vd-> new VehicleModel(vd.getRegestrationNumber()
               ,vd.getType() , vd.getSlotSrNumber())).orElse(null)
//        new VehicleModel(x.getVehicleDetails().getRegestrationNumber(), x.getVehicleDetails().getType(), x.getId())
        )).toList();

    }

}
