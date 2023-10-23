package ru.practicum.location;

import lombok.experimental.UtilityClass;
import ru.practicum.location.dto.Location;
import ru.practicum.location.model.LocationEntity;

@UtilityClass
public class LocationMapper {

    public Location toDto(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getLan(),
                locationEntity.getLon()
        );
    }

    public LocationEntity toModel(Location location) {
        return new LocationEntity(
                null,
                location.getLan(),
                location.getLan()
        );
    }
}
