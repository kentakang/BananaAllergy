package com.kentakang.bananaallergy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IOException {
        ListParser parser = new ListParser();
        ArrayList<String> list = parser.getListFromWeb(false);
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
