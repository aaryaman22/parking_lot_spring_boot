package com.example.PARKING_LOT_SYSTEM.Services.Implementation;
import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import com.example.PARKING_LOT_SYSTEM.Model.SlotModel;
import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.example.PARKING_LOT_SYSTEM.Repository.SlotReop;
import com.example.PARKING_LOT_SYSTEM.Services.SlotServices;
import com.example.PARKING_LOT_SYSTEM.Services.VehicleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlotServicesImplementations implements SlotServices {
    @Autowired
    private SlotReop slotReop;

    @Autowired
    private VehicleServices vehicleServices;


    public SlotModel removeVehicleAndUpdateSlot(String registrationNumber)
    {

        VehicleModel vehicleModel = vehicleServices.getVehicleInfo(registrationNumber).getReturnObject();
        if(vehicleModel!=null)
        {
            int slotId = vehicleModel.getSlotSrNumber();
            Optional<Slot> slotChk = slotReop.findById(slotId);
            if(slotChk.isPresent())
            {
                Slot slot = slotChk.get();
                SlotModel slotModel = SlotModel.builder().id(slot.getId()).slotNumber(slot.getSlotNumber()).levelId(slot.getLevelId()).type(slot.getType()).occupied(slot.getOccupied()).build();
                vehicleServices.deleteVehicleOnSlotId(slotId);
                updateSlot(slotId , false);
                return slotModel;
            }
            return null;
        }
//        vehicleServices.removeVehicleById(registrationNumber);
        return null;
    }

    @Override
    public Boolean checkIfAlreadyPresent(String registrationNumber) {
        return vehicleServices.getVehicleInfo(registrationNumber).getReturnObject() != null;
    }

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
    public Boolean AddSlots(int bike , int car , int bus , int levelId ) {

        List<Slot> myList= new ArrayList<>();
        for (int i = 0; i < bike; i++) {
            myList.add(InitSpot(levelId, i , "bike"));
        }
        for (int i = 0; i < car; i++) {
            myList.add(InitSpot(levelId , i , "car"));
        }
        for (int i = 0; i < bus; i++) {
            myList.add(InitSpot(levelId , i , "bus"));
        }
        slotReop.saveAll(myList);

        return true;
    }
    @Override
    public Integer firstEmptySlotLevel(String type) {
        Optional<Slot> getSolt = slotReop.findFirstByTypeAndOccupiedFalse(type);
        if (getSolt.isPresent()) {
            Slot slot = getSolt.get();
            return slot.getLevelId();
        }
        return null;
    }

    @Override
    public Integer firstEmptySlotId(String type) {
        Optional<Slot> getSolt = slotReop.findFirstByTypeAndOccupiedFalse(type);
        if (getSolt.isPresent()) {
            Slot slot = getSolt.get();
            return slot.getId();
        }
        return null;
    }

    @Override
    public List<Slot> getAllSlotByLevel(int levelId) {
        return slotReop.findAllByLevelId(levelId);
    }

    @Override
    public void deleteAllSlotsOnLevel(List<Slot> slots) {
        slotReop.deleteAll(slots);
    }

    @Override
    public Integer getSlotLevel(int slotId) {
        Optional<Slot> slot = slotReop.findById(slotId);
//        return slot.map(Slot::getLevelId).orElse(null);
        if(slot.isPresent())  return slot.get().getLevelId();
        return null;
    }

    @Override
    public void updateSlot(int slotId, Boolean value) {
        Optional<Slot> getSlot = slotReop.findById(slotId);
        if(getSlot.isPresent())
        {
            Slot slot = getSlot.get();
            slot.setOccupied(value);
            slot.setVehicleDetails(null);
            slotReop.save(slot);
        }
    }
}
