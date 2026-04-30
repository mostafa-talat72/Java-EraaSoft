package EWalletSystemV3Bonus;

import EWalletSystemV3Bonus.service.impl.EWalletApplicationServiceImpl;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        // Create a new instance of the EWalletApplicationServiceImpl
        // This class contains the main logic of the E-Wallet application
        new EWalletApplicationServiceImpl().start();

        // Call the start() method to launch the application
        // IllegalAccessException may be thrown if reflection or restricted access is used inside start()
    }
}
