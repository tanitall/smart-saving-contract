package com.blocksaver.contract;

import org.neo.smartcontract.framework.SmartContract;

public class SmartSavingContract extends SmartContract {

    public static Object Main(String operation, Object... args) {

        if("addFunds".equals(operation)) {
            return addFunds((String) args[0], (byte[]) args[1]);
        }
        if("createSavingsAccount".equals(operation)) {
            return createSavingsAccount((String) args[0]);
        }
        if("closeSavingsAccount".equals(operation)) {
            return closeSavingsAccount((String) args[0], (byte[]) args[1], (byte[]) args[2]);
        }
        return false;

    }

    private static boolean addFunds(String domain, byte[] owner) {
        return true;
    }

    private static byte[] createSavingsAccount(String domain) {
        return null;
    }

    private static boolean closeSavingsAccount(String domain, byte[] account, byte[] transferTo) {
        return true;
    }

}
