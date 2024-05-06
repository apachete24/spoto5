package com.grupor.spoto5.security;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlFilter {

    public static String filter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Crear una lista blanca personalizada para permitir solo etiquetas y atributos específicos
        Safelist whitelist = Safelist.relaxed()
                .addTags("p", "h1", "h2", "h3", "b", "i", "u", "ul", "li", "br")
                .removeTags("script", "img", "iframe", "object") // Excluir etiquetas que pueden contener scripts
                .removeAttributes(":all", "on*", "data*", "src", "href", "style", "class", "id"); // Excluir atributos relacionados con eventos y otros atributos peligrosos

        // Utilizar Jsoup para realizar la limpieza y normalización del HTML
        String cleanedHtml = Jsoup.clean(input, whitelist);

        return cleanedHtml;
    }
}
