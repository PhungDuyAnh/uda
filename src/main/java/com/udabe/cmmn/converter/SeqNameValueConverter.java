package com.udabe.cmmn.converter;

import javax.persistence.AttributeConverter;

public class SeqNameValueConverter implements AttributeConverter<SeqNameValue, Long> {

    @Override
    public Long convertToDatabaseColumn(SeqNameValue attribute) {

        Long convertSeq = null;

        if (attribute != null && attribute.getSeq() != null) {
            convertSeq = attribute.getSeq();
        }

        return convertSeq;
    }

    @Override
    public SeqNameValue convertToEntityAttribute(Long dbData) {
        return new SeqNameValue(dbData);
    }
}
