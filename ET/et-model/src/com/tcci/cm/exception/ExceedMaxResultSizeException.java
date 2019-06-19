/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.exception;

import java.math.BigInteger;

/**
 * 超過最大筆數Exception，通常用於查詢結果筆數大於限制回傳的筆數。
 *
 * @author Jackson.Lee
 */
public class ExceedMaxResultSizeException extends RuntimeException {

    /**
     * 最大回傳筆數
     */
    private int maxResultsSize = 0;
    /**
     * 回傳筆數
     */
    private BigInteger resultSize = BigInteger.ZERO;

    /**
     * Constructor
     *
     * @param maxResultsSize
     * @param resultSize
     */
    public ExceedMaxResultSizeException(int maxResultsSize, int resultSize) {
        this.maxResultsSize = maxResultsSize;
        this.resultSize = new BigInteger(String.valueOf(resultSize));
    }

    public ExceedMaxResultSizeException(int maxResultsSize, BigInteger resultSize) {
        this.maxResultsSize = maxResultsSize;
        this.resultSize = resultSize;
    }

    public int getMaxResultsSize() {
        return maxResultsSize;
    }

    public void setMaxResultsSize(int maxResultsSize) {
        this.maxResultsSize = maxResultsSize;
    }

    public BigInteger getResultSize() {
        return resultSize;
    }

    public void setResultSize(BigInteger resultSize) {
        this.resultSize = resultSize;
    }
    
}
