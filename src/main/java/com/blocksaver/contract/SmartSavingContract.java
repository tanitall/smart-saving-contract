package com.blocksaver.contract;

import org.neo.smartcontract.framework.Helper;
import org.neo.smartcontract.framework.SmartContract;
import org.neo.smartcontract.framework.services.neo.Blockchain;
import org.neo.smartcontract.framework.services.neo.Header;
import org.neo.smartcontract.framework.services.neo.Storage;
import org.neo.smartcontract.framework.services.system.ExecutionEngine;

import java.math.BigInteger;

public class SmartSavingContract extends SmartContract {

    private static final String NEO_ASSET = "4e454fa";

    private static final String GAS_ASSET = "474153a";

    private static final String DEPOSIT_TERM = "4445504f5349542d5445524daa";

    private static final String SAVINGS = "534156494e4753aaa";

    private static final String SMART_SAVINGS = "534d4152545f534156494e47aaa";

    public static Object Main(String operation, Object... args) {

        if (operation.equals("create")) {
            return createSavings(ExecutionEngine.callingScriptHash(), new BigInteger((byte[]) args[0]));
        }

        if (operation.equals("getSavingById")) {
            return getSavingById(ExecutionEngine.callingScriptHash(), (byte[]) args[0]);
        }

        return false;

    }


    /**
     * Returns savings data as json string
     *
     * @param owner     A {@link String} containing address of savings owner
     * @param savingsId A {@link String} with unique savings id
     * @return A {@link String} with json savings data representations
     */
    private static String getSavingById(byte[] owner, byte[] savingsId) {
        return "{" +
                "\"id\": " + "\"" + bytesToHex(savingsId) + "\"" + "," +
                "\"neo\": " + getSavingNeoBalance(owner, savingsId) + "," +
                "\"gas\": " + getSavingGasBalance(owner, savingsId) + "," +
                "\"term\": " + getSavingsDepositTerm(owner, savingsId) + "," +
                "}";
    }

    /**
     * Returns unique address of newly created savings.
     *
     * @param owner       A {@link String} containing owner's wallet address
     * @param depositTerm
     * @return A {@link String} with savings unique address
     */
    private static byte[] createSavings(byte[] owner, BigInteger depositTerm) {
        Header header = Blockchain.getHeader(Blockchain.height());
        //TODO: add some uniqueness
        byte[] savingsId = sha256(owner);
//        String savingsArray = asString(Storage.get(Storage.currentContext(), Helper.concat(owner, SAVINGS.getBytes())));
//        savingsArray += savingsArray + ","+savingsId;
//        Storage.put(Storage.currentContext(), SAVINGS + "-" + owner, toByteArray(savingsArray));
        setSavingsNeoBalance(owner, savingsId, BigInteger.ZERO);
        setSavingsGasBalance(owner, savingsId, BigInteger.ZERO);
        setSavingsDepositTerm(owner, savingsId, depositTerm);
        return savingsId;
    }

    private static void setSavingsDepositTerm(byte[] owner, byte[] savingsId, BigInteger depositTerm) {
        //TODO: calculate interest based on deposit term
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, DEPOSIT_TERM), depositTerm);
    }

    private static BigInteger setSavingsGasBalance(byte[] owner, byte[] savingsId, BigInteger amount) {
        BigInteger balance = getSavingGasBalance(owner, savingsId);
        balance = balance.add(amount);
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, GAS_ASSET), toByteArray(amount));
        return balance;
    }

    private static BigInteger setSavingsNeoBalance(byte[] owner, byte[] savingsId, BigInteger amount) {
        BigInteger balance = getSavingNeoBalance(owner, savingsId);
        balance = balance.add(amount);
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, NEO_ASSET), toByteArray(balance));
        return balance;
    }

    private static BigInteger getSavingGasBalance(byte[] owner, byte[] savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, GAS_ASSET)

                )
        );
    }

    private static BigInteger getSavingNeoBalance(byte[] owner, byte[] savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, NEO_ASSET)

                )
        );
    }

    private static BigInteger getSavingsDepositTerm(byte[] owner, byte[] savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, DEPOSIT_TERM)

                )
        );
    }

    private static byte[] getSavingDataId(byte[] owner, byte[] savingId, String assetId) {
        return Helper.concat(hexToBytes(SMART_SAVINGS), Helper.concat(owner, Helper.concat(savingId, hexToBytes(assetId))));
    }

    /*
     * Helpers
     */

    private static byte[] toByteArray(BigInteger value) {
        return value.toByteArray();
    }


    public static BigInteger asBigInteger(byte[] bytes) {
        return new BigInteger(bytes);
    }

    private static String bytesToHex(byte[] bytes)
    {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            hexString.append(bytes[i]);
        }
        return hexString.toString();
    }

    private static byte[] hexToBytes(String value)
    {
        String ss = "";
        for (int i = 0; i < value.length() / 2; i++)
            ss += (char)(toByte(value.charAt(i * 2)) * 16 |toByte(value.charAt(i * 2 + 1)));
        return (byte[])(Object)ss;
    }

    private static int toByte(char c)
    {
        String hex = "0123456789abcdef";
        for (int i = 0; i < hex.length(); i++)
        {
            if (c == hex.charAt(i))
                return i;
        }
        return -1;
    }

}
