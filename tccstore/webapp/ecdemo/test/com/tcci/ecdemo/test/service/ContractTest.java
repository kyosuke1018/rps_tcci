/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.test.service;

import com.tcci.ecdemo.model.contract.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class ContractTest {

    private final static Logger logger = LoggerFactory.getLogger(ContractTest.class);

    public static void main(String[] args) {
        logger.debug("Test: contract/customer/1 success");
        Contract[] contracts = ClientUtil.get(Contract[].class, "contract/customer/1", "jimmy.lee:HMAC-SIGNED-TOKEN");
        showResult(contracts);

        logger.debug("Test: contract/customer/1 fail");
        contracts = ClientUtil.get(Contract[].class, "contract/customer/1", null);
        showResult(contracts);
    }

    public static void showResult(Contract[] contracts) {
        if (null == contracts || 0 == contracts.length) {
            logger.debug("empty");
        } else {
            for (Contract contract : contracts) {
                logger.debug("{} {}", contract.getCode(), contract.getName());
            }
        }
        logger.debug("--------------------");
    }

}
