package com.example.PARKING_LOT_SYSTEM.Model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {
    private String regestrationNumber;
    private String type;
    private int slotSrNumber;
}
