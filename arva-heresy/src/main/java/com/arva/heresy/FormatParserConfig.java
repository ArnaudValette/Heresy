package com.arva.heresy;

import java.util.HashMap;
import java.util.Map;

public class FormatParserConfig {
    final Map<Character, Byte> flags = new HashMap<>();

    public FormatParserConfig() {
        flags.put('=', (byte) 0b000001);
        flags.put('~', (byte) 0b000010);
        flags.put('+', (byte) 0b000100);
        flags.put('_', (byte) 0b001000);
        flags.put('/', (byte) 0b010000);
        flags.put('*', (byte) 0b100000);
    }

    public boolean has(char c) {
        return flags.containsKey(c);
    }
    public Byte get(char c){
        return flags.get(c);
    }
}
