package com.example.PARKING_LOT_SYSTEM.Repository;

import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepo extends JpaRepository<VehicleDetails , String> {// arg ka mtlb
//    @Query("select u.SLOT_SR_NUMBER from u where u.REGESTRATION_NUMBER = :registrationNumber")
//    public int slotSerialNumber(String registrationNumber);
}
