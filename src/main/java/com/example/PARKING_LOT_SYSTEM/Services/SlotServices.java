package com.example.PARKING_LOT_SYSTEM.Services;

import com.example.PARKING_LOT_SYSTEM.Entity.Parking;
import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import com.example.PARKING_LOT_SYSTEM.Model.ParkingModel;
import com.example.PARKING_LOT_SYSTEM.Model.SlotModel;
import com.example.PARKING_LOT_SYSTEM.Responses.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SlotServices {
    Boolean AddSlots(int bike , int car , int bus , int levelId );
    Integer firstEmptySlotLevel(String type);
    Integer firstEmptySlotId(String type);

    List<Slot> getAllSlotByLevel(int levelId);

    public SlotModel removeVehicleAndUpdateSlot(String registrationNumber);
    Boolean checkIfAlreadyPresent(String registrationNumber);

    void deleteAllSlotsOnLevel(List<Slot> slots);

    Integer getSlotLevel(int slotId);

    void updateSlot(int slotId , Boolean value);
}
