package com.blocksaver.contract;

import org.neo.smartcontract.framework.SmartContract;
import org.neo.smartcontract.framework.services.neo.Blockchain;
import org.neo.smartcontract.framework.services.neo.Header;
import org.neo.smartcontract.framework.services.neo.Storage;
import org.neo.smartcontract.framework.services.system.ExecutionEngine;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SmartSavingContract extends SmartContract {

    private static final String NEO_ASSET = "NEO";

    private static final String GAS_ASSET = "NEO";

    private static final String DEPOSIT_TERM = "DEPOSIT-TERM";

    private static final String SAVINGS = "Savings";

    public static Object Main(String operation, Object... args) {

        if (operation.equals("create")) {
            return createSavings(asString(ExecutionEngine.callingScriptHash()), new BigInteger((byte[]) args[0]));
        }

        if (operation.equals("getSavingById")) {
            return getSavingById(asString(ExecutionEngine.callingScriptHash()), (String) args[0]);
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
    private static String getSavingById(String owner, String savingsId) {
        return "{" +
                "\"id\": " + "\"" + savingsId + "\"" + "," +
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
    private static String createSavings(String owner, BigInteger depositTerm) {
        Header header = Blockchain.getHeader(Blockchain.height());
        String savingsId = asString(sha256(toByteArray(owner + header.timestamp())));
        String savingsArray = asString(Storage.get(Storage.currentContext(), SAVINGS + "-" + owner));
        savingsArray += savingsArray + ","+savingsId;
        Storage.put(Storage.currentContext(), SAVINGS + "-" + owner, toByteArray(savingsArray));
        setSavingsNeoBalance(owner, savingsId, BigInteger.ZERO);
        setSavingsGasBalance(owner, savingsId, BigInteger.ZERO);
        setSavingsDepositTerm(owner, savingsId, depositTerm);
        return savingsId;
    }

    private static void setSavingsDepositTerm(String owner, String savingsId, BigInteger depositTerm) {
        //TODO: calculate interest based on deposit term
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, DEPOSIT_TERM), depositTerm);
    }

    private static BigInteger setSavingsGasBalance(String owner, String savingsId, BigInteger amount) {
        BigInteger balance = getSavingGasBalance(owner, savingsId);
        balance = balance.add(amount);
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, GAS_ASSET), toByteArray(amount));
        return balance;
    }

    private static BigInteger setSavingsNeoBalance(String owner, String savingsId, BigInteger amount) {
        BigInteger balance = getSavingNeoBalance(owner, savingsId);
        balance = balance.add(amount);
        Storage.put(Storage.currentContext(), getSavingDataId(owner, savingsId, NEO_ASSET), toByteArray(balance));
        return balance;
    }

    private static BigInteger getSavingGasBalance(String owner, String savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, GAS_ASSET)

                )
        );
    }

    private static BigInteger getSavingNeoBalance(String owner, String savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, NEO_ASSET)

                )
        );
    }

    private static BigInteger getSavingsDepositTerm(String owner, String savingId) {
        return asBigInteger(
                Storage.get(
                        Storage.currentContext(),
                        getSavingDataId(owner, savingId, DEPOSIT_TERM)

                )
        );
    }

    private static String getSavingDataId(String owner, String savingId, String assetId) {
        return "SmartSaving-" + owner + "-" + savingId + "-" + assetId;
    }

    /*
     * Helpers
     */

    public static byte[] toByteArray(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] toByteArray(BigInteger value) {
        return value.toByteArray();
    }

    public static String asString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static BigInteger asBigInteger(byte[] bytes) {
        return new BigInteger(bytes);
    }

}
