package com.blocksaver.contract;

import org.neo.smartcontract.framework.SmartContract;

public class SmartSavingContract extends SmartContract {

    public static Object main(String operation, Object... args) {

        switch (operation) {
            case "addFunds": {
                return addFunds((String) args[0], (byte[]) args[1]);
            }
            case "createSavingsAccount": {
                return createSavingsAccount((String) args[0]);
            }
            case "closeSavingsAccount": {
                return closeSavingsAccount((String) args[0], (byte[]) args[1], (byte[]) args[2]);
            }
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