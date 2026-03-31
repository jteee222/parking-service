package com.assessment.parkingservice.mapper;

import com.assessment.parkingservice.dto.CheckoutResponseDTO;
import com.assessment.parkingservice.dto.LotStatusDTO;
import com.assessment.parkingservice.dto.ParkingLotDTO;
import com.assessment.parkingservice.dto.ParkingSessionDTO;
import com.assessment.parkingservice.dto.VehicleDTO;
import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.entity.Vehicle;
import com.assessment.parkingservice.enums.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    VehicleDTO toDTO(Vehicle entity);

    @Mapping(target = "type", source = "type")
    Vehicle toEntity(VehicleDTO dto);

    ParkingLotDTO toDTO(ParkingLot entity);
    ParkingLot toEntity(ParkingLotDTO dto);

    @Mapping(source = "parkingLot.lotId", target = "lotId")
    @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
    ParkingSessionDTO toDTO(ParkingSession entity);

    @Mapping(source = "session.vehicle.licensePlate", target = "licensePlate")
    @Mapping(source = "session.parkingLot.lotId", target = "lotId")
    @Mapping(source = "minutes", target = "minutesParked")
    @Mapping(source = "cost", target = "totalCost")
    CheckoutResponseDTO toCheckoutDTO(ParkingSession session, long minutes, double cost);

    @Mapping(source = "lot.lotId", target = "lotId")
    @Mapping(source = "lot.capacity", target = "capacity")
    @Mapping(source = "occupied", target = "occupied")
    @Mapping(source = "available", target = "available")
    LotStatusDTO toLotStatusDTO(ParkingLot lot, long occupied, long available);

    default VehicleType mapStringToEnum(String type) {
        if (type == null) return null;
        return VehicleType.valueOf(type.toUpperCase());
    }
}
