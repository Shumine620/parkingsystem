package com.parkit.parkingsystem.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InteractiveShellTest {
    /**
     *
     */
    private static final Logger logger = LogManager.getLogger("InteractiveShell");
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadInterfaceTest() {
    }
}

/**
 class ApplicationTest {
 private static final InputStream systemIn = System.in;
 private static final PrintStream systemOut = System.out;

 private static InputStream testIn;
 private static OutputStream testOut;

 @BeforeEach
 public void setUpOutput() {
 testOut = new ByteArrayOutputStream();
 System.setOut(new PrintStream(testOut));
 }

 private void provideInput(String data) {
 testIn = new ByteArrayInputStream(data.getBytes());
 System.setIn(testIn);
 }

 private String getOutput() {
 return testOut.toString();
 }

 @AfterEach
 public void resetSystemInputOutput() {
 System.setIn(systemIn);
 System.setOut(systemOut);
 }

 @Test
 public void testCreateSuccess() {
 String testString = "create 2";
 provideInput(testString);

 Application.main(new String[0]);

 assertEquals("Created units - 2", getOutput());

 //Verifying the empty state. Another handler is invoked for it.
 provideInput("status");
 assertEquals("No units assigned", getOutput());

 //exiting the shell.
 provideInput("exit");
 }

 //  @Test
 public void testStatusAfterCreateParkingLotSuccess() {
 String testString = "status";
 provideInput(testString);

 Application.main(new String[0]);

 assertEquals(AppConstants.PARKING_LOT_IS_EMPTY, getOutput());

 resetSystemInputOutput();

 provideInput("exit");

 Application.main(new String[0]);
 }

 @AfterAll
 public static void exit() {
 //System.setIn(new ByteArrayInputStream("exit".getBytes()));
 }
 }*/