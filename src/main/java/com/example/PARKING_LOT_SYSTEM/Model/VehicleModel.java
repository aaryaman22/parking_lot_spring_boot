package com.example.PARKING_LOT_SYSTEM.Model;

import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {
    private String regestrationNumber;
    private String type;
    private int slotSrNumber;
    private LocalDateTime localDateTime;
}
