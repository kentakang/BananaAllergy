package com.kentakang.bananaallergy.test;

import com.kentakang.bananaallergy.ListParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ListParserTest {

    @Test
    void recentVideo() {
        ListParser parser = new ListParser();

        try {
            assertNotEquals(0, parser.recentVideo());
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void getListFromWeb() {
        ListParser parser = new ListParser();
        parser.dbInit();

        try {
            assertNotNull(parser.getListFromWeb(false));
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void getListFromDB() {
        ListParser parser = new ListParser();
        parser.dbInit();

        try {
            assertNotNull(parser.getList(false));
        } catch (IOException e) {
            fail(e);
        }
    }
}