package EwalletSystem;

import EwalletSystem.service.impl.EWalletApplicationServiceImpl;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        new EWalletApplicationServiceImpl().start();
    }
}
