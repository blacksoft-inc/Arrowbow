package com.blacksoft.arrowbow.items;

import androidx.annotation.NonNull;

import com.blacksoft.arrowbow.networking.RequestHeader;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * Holds key value pairs
 * <p>
 * example: Content-length: 1024;
 * <p>
 * example: file_path: path ...
 */
public class Field {

    private String name, value;

    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    /**
     * @param value: field value,
     *               <p>
     *               possible field values can be found in {@link RequestHeader.DataType} and others like
     *               Integer numbers for Content-length field.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param name: field name example: Content-type.
     *              <p>
     *              possible headers values can be found in {@link RequestHeader.Field}.
     */
    public void setField(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nname: " + name
                + "\nvalue: " + value;
    }
}