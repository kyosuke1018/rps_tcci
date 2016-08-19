/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.rfc;

import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.util.JCoUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class InputBuilder {

    private RfcProxyInput input;
    
    public InputBuilder(String clientCode, String functionName, String sapClientCode, String operator){
        input = new RfcProxyInput(clientCode, functionName, sapClientCode, operator);
    }

    public InputBuilder(String clientCode, String functionName, String sapClientCode) {
        input = new RfcProxyInput(clientCode, functionName, sapClientCode);
    }

    public InputBuilder(String functionName, String sapClientCode) {
        input = new RfcProxyInput(functionName, sapClientCode);
    }

    public RfcProxyInput build() {
        return input;
    }

    public InputBuilder inputTable(String table, String key, Object value) {
        Map<String, List<Map<String, Object>>> tables = input.getTables();
        if (null == tables) {
            tables = new HashMap<>();
            input.setTables(tables);
        }
        List<Map<String, Object>> list = tables.get(table);
        if (null == list) {
            list = new ArrayList<>();
            tables.put(table, list);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        list.add(map);
        return this;
    }
    
    public InputBuilder inputTable(String table, String scope, String opera, Object low, Object high) {
        Map<String, List<Map<String, Object>>> tables = input.getTables();
        if (null == tables) {
            tables = new HashMap<>();
            input.setTables(tables);
        }
        List<Map<String, Object>> list = tables.get(table);
        if (null == list) {
            list = new ArrayList<>();
            tables.put(table, list);
        }
        list.add(JCoUtil.buildSimpleImpMap(scope, opera, low, high));
        return this;
    }
    
    public InputBuilder inputTableEQ(String table, String value) {
        return inputTable(table, "I", "EQ", value, null);
    }

    public InputBuilder inputTableNE(String table, String value) {
        return inputTable(table, "I", "NE", value, null);
    }

    public InputBuilder inputTableBT(String table, String low, String high) {
        return inputTable(table, "I", "BT", low, high);
    }

    public InputBuilder inputValue(String field, String value) {
        Map<String, Object> values = input.getValues();
        if (null == values) {
            values = new HashMap<>();
            input.setValues(values);
        }
        values.put(field, value);
        return this;
    }

    public InputBuilder outputTable(String table) {
        return outputTable(table, null);
    }

    public InputBuilder outputTable(String table, String columns) {
        Map<String, RfcOutputTypeEnum> outputTypes = input.getOutputTypes();
        if (null == outputTypes) {
            outputTypes = new HashMap<>();
            input.setOutputTypes(outputTypes);
        }
        outputTypes.put(table, RfcOutputTypeEnum.TABLE);
        if (columns != null) {
            outputColumns(table, columns);
        }
        return this;
    }

    public InputBuilder outputColumns(String table, String columns) {
        /* TODO: 等 RfcProxyInput 支援
        Map<String, String> outputColumns = input.getOutputColumns();
        if (null == outputColumns) {
            outputColumns = new HashMap<>();
            input.setOutputColumns(outputColumns);
        }
        outputColumns.put(table, columns);
        */
        return this;
    }
    
    public InputBuilder debugMode(boolean debugMode) {
        input.setDebugMode(debugMode);
        return this;
    }

}
