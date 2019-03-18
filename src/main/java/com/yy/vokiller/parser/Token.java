package com.yy.vokiller.parser;

import java.util.List;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/17 23:47
 */
public class Token {

    private String original;
    private String originalSubField;
    private String fieldName;
    private String fieldValue;
    private List<Token> subFields;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalSubField() {
        return originalSubField;
    }

    public void setOriginalSubField(String originalSubField) {
        this.originalSubField = originalSubField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public List<Token> getSubFields() {
        return subFields;
    }

    public void setSubFields(List<Token> subFields) {
        this.subFields = subFields;
    }

    @Override
    public String toString() {
        return "Token{" +
                "original='" + original + '\'' +
                ", originalSubField='" + originalSubField + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", subFields=" + subFields +
                '}';
    }
}
