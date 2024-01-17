package com.example.PARKING_LOT_SYSTEM.Repository;

import com.example.PARKING_LOT_SYSTEM.Entity.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepo extends JpaRepository<VehicleDetails , String> {// arg ka mtlb
//    @Query("select u.SLOT_SR_NUMBER from u where u.REGESTRATION_NUMBER = :registrationNumber")
//    public int slotSerialNumber(String registrationNumber);
    List<VehicleDetails> findAllBySlotSrNumber(Integer slotSrNumber);
    Optional<VehicleDetails> findBySlotSrNumber(Integer slotSrNumber);
}
