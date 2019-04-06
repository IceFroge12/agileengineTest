package com.test;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    private static String CHARSET_NAME = "utf8";
    private static final String DEFAULT_TARGET_ELEMENT_ID = "make-everything-ok-button";

    public static void main(String[] args) throws IOException {
        String originalHTML = args[0];
        String modifiedHTML = args[1];
        String targetElementId = args.length >= 3 ? args[2] : DEFAULT_TARGET_ELEMENT_ID;
        Element targetElement = findElement(new File(originalHTML), targetElementId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't find element with id - %s", targetElementId)));
        log.info("Original target element path - {}", targetElement.parent().cssSelector());

//        Originally though, that element might be placed only in the same div, or in relatively higher.
//        Elements parents = findParent(new File(modifiedHTML), targetElement).orElseThrow(() -> new IllegalStateException(String.format("Can't find parent elements for id - %s", targetElementId)));

        Elements parents = findElementsToScan(new File(modifiedHTML)).orElseThrow(() -> new IllegalStateException("Can't find body"));

        List<ElementEqualRate> similarInParent = findSimilarInParent(parents, targetElement);
        similarInParent.forEach(elementEqualRate -> log.info("For element with cssSelector - \" {} \", equal score = {}", elementEqualRate.getElement().cssSelector(), elementEqualRate.getEqualRate()));


    }

    public static Optional<Element> findElement(File fileHTML, String id) throws IOException {
        Document doc = Jsoup.parse(fileHTML, CHARSET_NAME, fileHTML.getAbsolutePath());
        return Optional.ofNullable(doc.getElementById(id));
    }

    //Originally though, that element might be placed only in the same div, or higher.
//    public static Optional<Elements> findParent(File fileHTML, Element targetElement) throws IOException {
//        Document doc = Jsoup.parse(fileHTML, CHARSET_NAME, fileHTML.getAbsolutePath());
//        Elements parents;
//        Element targetElementParent = targetElement.parent();
//        do {
//            String parentCssSelector = targetElementParent.cssSelector();
//            parents = doc.select(parentCssSelector);
//        } while (parents == null && targetElementParent.parent() != null);
//
//        return Optional.ofNullable(parents);
//    }

    public static Optional<Elements> findElementsToScan(File fileHTML) throws IOException {
        Document doc = Jsoup.parse(fileHTML, CHARSET_NAME, fileHTML.getAbsolutePath());
        return Optional.ofNullable(doc.body().children());
    }


    public static List<ElementEqualRate> findSimilarInParent(Elements elements, Element targetElement) {
        return elements
                .stream()
                .flatMap(e -> e.select("*").stream())
                .map(e -> new ElementEqualRate(e, calculateSimilarProperties(e, targetElement)))
                .sorted(Comparator.comparingInt(ElementEqualRate::getEqualRate))
                .collect(Collectors.toList());
    }


    public static int calculateSimilarProperties(Element elements1, Element element2) {
        int count = 0;
        for (Attribute attribute : element2.attributes()) {
            if (elements1.hasAttr(attribute.getKey())) {
                count++;
                if (elements1.attr(attribute.getKey()).equals(attribute.getValue())) {
                    count++;
                }
            }
        }
        if (elements1.text().equals(element2.text())) {
            count++;
        }
        if (elements1.tagName().equals(element2.tagName())) {
            count++;
        }
        return count;
    }

}
