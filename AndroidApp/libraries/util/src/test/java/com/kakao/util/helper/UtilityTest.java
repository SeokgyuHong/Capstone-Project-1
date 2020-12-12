package com.kakao.util.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kevin.kang on 16. 8. 11..
 */
@DisplayName("Utility's")
class UtilityTest {

    private HashMap<String, String> paramMap;

    @BeforeEach
    void setup() {
        paramMap = new HashMap<>();
    }

    @Nested
    @DisplayName("buildQueryString()")
    class BuildQueryString {
        @Test
        @DisplayName("with null input should return null")
        void withNull() {
            assertNull(Utility.buildQueryString(null));
        }

        @Test
        @DisplayName("with empty map should return null")
        void withEmptyMap() {
            assertNull(Utility.buildQueryString(paramMap));
        }

        @Test
        @DisplayName("with one element")
        void withOneElement() {
            paramMap.put("place", "1111");
            assertEquals(Utility.buildQueryString(paramMap), "place=1111");
        }

        @Test
        @DisplayName("with two element")
        void withTwoElement() {
            paramMap.put("place", "1111");
            paramMap.put("nickname", "kevin");
            assertTrue(Arrays.asList("place=1111&nickname=kevin", "nickname=kevin&place=1111").contains(Utility.buildQueryString(paramMap)));
        }

        @Test
        @DisplayName("with null element")
        void withNullElement() {
            paramMap.put("place", null);
            assertEquals(Utility.buildQueryString(paramMap), "place=null");
        }
    }

    @Nested
    @DisplayName("asCsv")
    class AsCsv {
        @Test
        void withNull() {
            String csv = Utility.asCsv(null);
            assertNull(csv);
        }

        @Test
        void withEmpty() {
            String csv = Utility.asCsv(Collections.emptyList());
            assertNull(csv);
        }

        @Test
        void withOneLong() {
            String csv = Utility.asCsv(Collections.singletonList(1));
            assertEquals("1", csv);
        }

        @Test
        void withTwoLongs() {
            String csv = Utility.asCsv(Arrays.asList(1, 2));
            assertEquals("1,2", csv);
        }
    }
}