package ru.practicum.location;

import lombok.experimental.UtilityClass;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

@UtilityClass
public class LocationMapper {

    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.getLan(),
                location.getLon()
        );
    }

    public Location toModel(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLan(),
                locationDto.getLan()
        );
    }
}
