package com.example.PARKING_LOT_SYSTEM.Model;

import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingModel {
    private int id;
    private int occupiedBikeSpot;
    private int occupiedCarSpot;
    private int occupiedBusSpot;
    private int availableBikeSpot;
    private int availableCarSpot;
    private int availableBusSpot;
    private List<SlotModel> slotList;

    @Override
    public String toString() {
        return ("id is " + id + "name is" + "bike cap is ");
    }
}
