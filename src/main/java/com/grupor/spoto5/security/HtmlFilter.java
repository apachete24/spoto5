package com.grupor.spoto5.security;

public class HtmlFilter {

    public static String filter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Permitir solo las etiquetas y atributos específicos
        String filteredInput = input.replaceAll("<(?!\\/?(p|h[1-3]|b|i|u|ul|li|br)\\b)[^>]+>", "")
                .replaceAll("javascript:", "")
                .replaceAll("on\\w+\\s*=\\s*['\"]?([^'\"]+)['\"]?", "");

        return filteredInput;
    }
}


