package com.example.PARKING_LOT_SYSTEM.Repository;

import com.example.PARKING_LOT_SYSTEM.Entity.Parking;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepo extends JpaRepository<Parking , Integer> {
//    //@Query("select TOP from Parking u u.LEVEL_ID from u where u.AVAILABLE_BIKE_SPOTS > (0)")
//    public int getBikeLevelNumber();
//
//    //@Query("select TOP u.LEVEL_ID from u where u.AVAILABLE_CAR_SPOTS > (0)")
//    public int getCarLevelNumber();
//    //@Query("select TOP u.LEVEL_ID from u where u.AVAILABLE_BUS_SPOTS > (0)")
//    public int getBusLevelNumber();
    Parking findFirstByOrderByIdDesc();

   // @EntityGraph("level.slots")
    @Query ("select l from Parking l left join fetch l.slotsList s left join fetch s.vehicleDetails where s.vehicleDetails is not null")
    List<Parking> findAllLevels();
}
