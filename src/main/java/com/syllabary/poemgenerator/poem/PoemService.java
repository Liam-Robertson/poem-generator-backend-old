package com.syllabary.poemgenerator.poem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PoemService {
    ResourceLoader resourceLoader;

    @Autowired
    public PoemService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    public List<Poem> getAllPoemData(String directory) throws IOException {
        List<Poem> poemContentList = new ArrayList<Poem>();
        Resource[] resources = this.loadResources(directory);
        DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();

        if (resources != null) {
            for (Resource poemResource : resources) {
                File poemFile = poemResource.getFile();
                try {
                    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(poemFile);
                    doc.getDocumentElement().normalize();
                    NodeList nodeList = doc.getElementsByTagName("poem");

                    for (int currentNode = 0; currentNode < nodeList.getLength(); currentNode++) {
                        Node node = nodeList.item(currentNode);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element currentElement = (Element) node;
                            String poemTitle = currentElement.getElementsByTagName("title").item(0).getTextContent();
                            String poemText = currentElement.getElementsByTagName("text").item(0).getTextContent();
                            poemContentList.add(
                                    new Poem(
                                            poemTitle,
                                            poemText
                                    )
                            );
                        }
                    }
                } catch (SAXException | IOException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
        return poemContentList;
    }
}
