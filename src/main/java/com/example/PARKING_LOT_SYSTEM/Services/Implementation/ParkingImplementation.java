package com.example.PARKING_LOT_SYSTEM.Services.Implementation;
import com.example.PARKING_LOT_SYSTEM.Entity.Parking;
import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.SlotModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Repository.LevelRepo;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import com.example.PARKING_LOT_SYSTEM.Services.ParkingServices;
import com.example.PARKING_LOT_SYSTEM.Services.SlotServices;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private SlotServices slotServices;
    @Lazy
    @Autowired
    private VehicleServices vehicleService;

    @Override
    public Response<Boolean> addLevel(ParkingModel parkingModel) {
        Parking level = new Parking();
        level.setOccupiedBikeSpot(0);
        level.setOccupiedCarSpot(0);
        level.setOccupiedBusSpot(0);

        int availableBikeSpots = parkingModel.getAvailableBikeSpot();
        int availableCarSpots = parkingModel.getAvailableCarSpot();
        int availableBusSpots = parkingModel.getAvailableBusSpot();

        level.setAvailableBikeSpot(availableBikeSpots);
        level.setAvailableCarSpot(availableCarSpots);
        level.setAvailableBusSpot(availableBusSpots);
        levelRepo.save(level);

        if(slotServices.AddSlots(availableBikeSpots,availableCarSpots,availableBusSpots,level.getId()))
        {
            return new Response<>(true);
        }
        return new Response<>(false , HttpStatus.NOT_IMPLEMENTED);

    }

    @Override
    public Response<VehicleModel> addVehicle(String type, String registrationNumber) {
//      slotReop.findByLevelIdAnd (1);
        if(!slotServices.checkIfAlreadyPresent(registrationNumber))
        {
            Integer slotId = slotServices.firstEmptySlotId(type);   //getting first empty slot id
            Integer level = slotServices.firstEmptySlotLevel(type); // getting slot level number
            if(level!=null)
            {
                Optional<Parking> parking = levelRepo.findById(level);
                if(parking.isPresent())
                {
                    Parking updateAvailibility = parking.get(); //updating slot

                    switch (type) {
                        case "bike":
                            updateAvailibility.setOccupiedBikeSpot(updateAvailibility.getOccupiedBikeSpot() + 1);
                            updateAvailibility.setAvailableBikeSpot(updateAvailibility.getAvailableBikeSpot() - 1);
                            break;
                        case "car":
                            updateAvailibility.setOccupiedCarSpot(updateAvailibility.getOccupiedCarSpot() + 1);
                            updateAvailibility.setAvailableCarSpot(updateAvailibility.getAvailableCarSpot() - 1);
                            break;
                        case "bus":
                            updateAvailibility.setOccupiedBusSpot(updateAvailibility.getOccupiedBusSpot() + 1);
                            updateAvailibility.setOccupiedBusSpot(updateAvailibility.getOccupiedBusSpot() - 1);
                            break;
                    }

                    VehicleModel newVehicle = vehicleService.createAndSave(registrationNumber,type,slotId);
                    slotServices.updateSlot(slotId , true);

                    if( newVehicle!=null)
                    {
                        return new Response<>(newVehicle);
                    }
                }
            }
            return new Response<>(null , HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new Response<>(null , HttpStatus.BAD_REQUEST);
    }

    @Override
    public void updateAvailibilityInLevel(int levelId, String type)
    {
        Optional<Parking> updateAvailability = levelRepo.findById(levelId);
        if(updateAvailability.isPresent())
        {
            Parking parking = updateAvailability.get(); //
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
            levelRepo.save(parking);
        }
    }

    @Override
    public Response<Integer> deleteLevel() {

        Parking lastLevel = levelRepo.findFirstByOrderByIdDesc();
        List<Slot> slotList = slotServices.getAllSlotByLevel(lastLevel.getId());
        for (int i = 0; i < slotList.size(); i++) {
            Slot particularLevelSlot = slotList.get(i);
            Integer slotId = particularLevelSlot.getId();
            vehicleService.deleteVehicleOnSlotId(slotId);
        }
        slotServices.deleteAllSlotsOnLevel(slotList);
        levelRepo.deleteById(lastLevel.getId());

        if (lastLevel.getId() == -1) return new Response<>(null, HttpStatus.BAD_REQUEST);
        return new Response<>(lastLevel.getId(), HttpStatus.ACCEPTED);
    }

    public Response<Boolean> removeVehicle(String registrationNumber) {
        SlotModel slotModel = slotServices.removeVehicleAndUpdateSlot(registrationNumber);
        if(slotModel!=null)
        {
            updateAvailibilityInLevel(slotModel.getLevelId(), slotModel.getType());
            return new Response<>(true , HttpStatus.ACCEPTED);
        }
        return new Response<>(false , HttpStatus.BAD_REQUEST);
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
        if (!myList.isEmpty()) return new Response<>(myList);
        return new Response<>(null, HttpStatus.NO_CONTENT);
    }

    private List<SlotModel> listOfSlotModels(List<Slot> list) {

        return list.stream().map(x -> new SlotModel(x.getId(), x.getSlotNumber(), x.getLevelId(), x.getType(), x.getOccupied(),
               Optional.ofNullable(x.getVehicleDetails()).map(vd-> new VehicleModel(vd.getRegestrationNumber()
               ,vd.getType() , vd.getSlotSrNumber() , vd.getLocalDateTime())).orElse(null)
        )).toList();

    }

}
