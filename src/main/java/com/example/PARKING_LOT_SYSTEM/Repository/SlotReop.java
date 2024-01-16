package com.example.PARKING_LOT_SYSTEM.Repository;

import com.example.PARKING_LOT_SYSTEM.Entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlotReop extends JpaRepository<Slot, Integer> {
    Slot findByLevelId(int i);
//    @Query("select TOP u.SR_NUMBER from u where u.LEVEL_ID = :levelId AND u.TYPE = :type AND u.occupied = :false")
//    public int slotSerialNumber(int levelId , String type);
//
//    @Query("update u set u.occupied = (true) where u.SR_NUMBER = :slotId")
//    public void updateSpotToGet(int slotId);
//
//    @Query("update u set u.occupied = (false) where u.SR_NUMBER = :slotId")
//    public void updateSpotToLeave(int slotId);

}
