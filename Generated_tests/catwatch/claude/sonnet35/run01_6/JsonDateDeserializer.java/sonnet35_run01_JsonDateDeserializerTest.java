package org.zalando.catwatch.backend;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zalando.catwatch.backend.model.util.JsonDateDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public class sonnet35_run01_JsonDateDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    private JsonDateDeserializer deserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deserializer = new JsonDateDeserializer();
    }

    @Test
    public void testDeserializeValidDate() throws IOException {
        when(jsonParser.getText()).thenReturn("2023-05-24T10:30:00");

        Date result = deserializer.deserialize(jsonParser, context);

        assertNotNull(result);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        assertEquals("2023-05-24T10:30:00", format.format(result));
    }

    @Test(expected = ParseException.class)
    public void testDeserializeInvalidDate() throws IOException, ParseException {
        when(jsonParser.getText()).thenReturn("invalid-date-format");

        deserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeNullDate() throws IOException {
        when(jsonParser.getText()).thenReturn(null);

        Date result = deserializer.deserialize(jsonParser, context);

        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        when(jsonParser.getText()).thenReturn("");

        Date result = deserializer.deserialize(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testConstructor() {
        JsonDateDeserializer deserializer = new JsonDateDeserializer();
        assertNotNull(deserializer);
    }
}