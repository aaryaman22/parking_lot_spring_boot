package com.example.PARKING_LOT_SYSTEM.Model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
