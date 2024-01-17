package com.example.PARKING_LOT_SYSTEM.Model;

import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotModel {
    private int id;
    private int slotNumber;
    private int levelId;
    private String type;
    private Boolean occupied;
    private VehicleModel vehicleModel;

}
