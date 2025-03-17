package com.marketplace.Etalation;

import org.springframework.stereotype.Component;

@Component
public class EtalationMapper {
    
    public Etalation toEtalation(EtalationRequestDto etalationDto) {
        return new Etalation(etalationDto.etalationName());
    }

    public EtalationDto toDto(Etalation etalation){
        return new EtalationDto(etalation.getEtalationName());
    }

}
