package com.bancodellitoral.demo.util.excel;

import java.util.function.Function;


public class CxRowMapping<T> {

    private final String colName;
    private final Function<T, Object> rowSupplier;

    public CxRowMapping(String colName, Function<T, Object> rowSupplier) {
        this.colName = colName;
        this.rowSupplier = rowSupplier;
    }

    public static <T> CxRowMapping<T> of(String colName, Function<T, Object> rowSupplier) {
        return new CxRowMapping<>(colName, rowSupplier);
    }

    public String colName() {
        return colName;
    }

    public Object getCxRValue(T itemRow) {
        return rowSupplier.apply(itemRow);
    }
}
