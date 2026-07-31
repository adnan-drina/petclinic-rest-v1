package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BaseDtoTest {

    @Test
    void testToIndentedStringWithNull() {
        TestBaseDto testDto = new TestBaseDto();
        String result = testDto.testToIndentedString(null);
        assertEquals("null", result);
    }

    @Test
    void testToIndentedStringWithSingleLine() {
        TestBaseDto testDto = new TestBaseDto();
        Object obj = "Simple text";
        String result = testDto.testToIndentedString(obj);
        assertEquals("Simple text", result);
    }

    @Test
    void testToIndentedStringWithMultipleLines() {
        TestBaseDto testDto = new TestBaseDto();
        MultiLineObject obj = new MultiLineObject();
        String result = testDto.testToIndentedString(obj);
        String expected = "Line 1\n    Line 2\n    Line 3";
        assertEquals(expected, result);
    }

    @Test
    void testToIndentedStringWithEmptyString() {
        TestBaseDto testDto = new TestBaseDto();
        String result = testDto.testToIndentedString("");
        assertEquals("", result);
    }

    // Test helper classes
    private static class TestBaseDto extends BaseDto {
        public String testToIndentedString(Object o) {
            return toIndentedString(o);
        }
    }

    private static class MultiLineObject {
        @Override
        public String toString() {
            return "Line 1\nLine 2\nLine 3";
        }
    }
}