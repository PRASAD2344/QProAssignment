package com.qpro.configuration;

import com.qpro.bo.Item;
import com.qpro.dto.StoryDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Item.class, StoryDTO.class).addMappings(mapper -> {
            mapper.map(Item::getTime,StoryDTO::setSubmittedAt);
            mapper.map(Item::getBy,StoryDTO::setSubmittedBy);
        });
        return modelMapper;
    }
}
