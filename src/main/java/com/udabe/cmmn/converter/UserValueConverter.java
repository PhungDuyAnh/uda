package com.udabe.cmmn.converter;

import com.udabe.cmmn.service.CmmnService;

import javax.persistence.Converter;

@Converter
public class UserValueConverter extends SeqNameValueConverter {

    private final CmmnService cmmnService;

    public UserValueConverter(CmmnService cmmnService) {
        this.cmmnService = cmmnService;
    }

    @Override
    public SeqNameValue convertToEntityAttribute(Long dbData) {

        SeqNameValue value = new SeqNameValue();

        if (dbData != null) {
            value = cmmnService.findUserValue(dbData);
        }

        return value;
    }
}
