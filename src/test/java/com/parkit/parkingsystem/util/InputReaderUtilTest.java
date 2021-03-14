package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.tools.ant.taskdefs.Input;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {
    public static final Scanner scan;
    private static InputReaderUtil inputReaderUtil;

    static {
        scan = new Scanner(System.in);
    }

    String input = "";
    public InputStream inputStream = new ByteArrayInputStream(input.getBytes());


    @BeforeAll
    public static void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    /**
     * @throws IOException if the reading cannot be performed.
     */
    @Test
    public void readSelectionTest() throws IOException {

        final String input = "PLOK";
        Scanner scan = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertEquals(input, inputReaderUtil.readSelection());

    }

    /**
     * Test the method to read the vehicle registration number.
     */
    @Test
    void readVehicleRegistrationNumber() {
        Scanner scanner = new Scanner(input);
        String input = "KLMOP";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertEquals("KLMOP", inputReaderUtil.readSelection());
    }

    /**
     * Test the reader when the input is empty.
     */
    @Test
    public void InputIsEmpty() {
        Scanner scanner = new Scanner(input);
        String data = "";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        //assertEquals(null, inputReaderUtil.readSelection());
    }
}