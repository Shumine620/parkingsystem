package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {
private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    public void readSelection() throws IOException {

        String input = "222";
       Scanner scanner = new Scanner(input);

        assertEquals(input, inputReaderUtil.readSelection());

    }

    @Test
    void readVehicleRegistrationNumber() {
       final String vehicleRegNumber = "KLMOP";

    }
}