package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * App class launching the parking system application.
 */
public final class App {
    private static final Logger logger = LogManager.getLogger("App");

    public static void main(final String[] args) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
