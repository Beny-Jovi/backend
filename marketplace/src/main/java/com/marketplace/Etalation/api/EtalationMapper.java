package com.marketplace.Etalation.api;

import org.springframework.stereotype.Component;

import com.marketplace.Etalation.domain.Etalation;

@Component
public class EtalationMapper {
    
    public Etalation toEtalation(EtalationRequestDto etalationDto) {
        return new Etalation(etalationDto.etalationName());
    }

    public EtalationDto toDto(Etalation etalation){
        return new EtalationDto(etalation.getEtalationName());
    }

}
