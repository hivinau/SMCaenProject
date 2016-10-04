/**
 * @author Hivinau GRAFFE & Jesus OLARRA GARNICA
 * @version 1.0.0
 * @since september, 2016
 */
package fr.unicaen.projects.smcaenproject.utilities;

import java.awt.Color;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XMLUtility {
	
	private static final String STRINGS_XML_PATH = "./res/strings.xml";
	private static final String DIMENS_XML_PATH = "./res/dimens.xml";
	private static final String COLORS_XML_PATH = "./res/colors.xml";

	public final static String getString(String key) throws Exception {
		
		return XMLUtility.getElement(XMLUtility.STRINGS_XML_PATH, key);
	}

	public final static int getSize(String key) throws Exception {
		
		String element = XMLUtility.getElement(XMLUtility.DIMENS_XML_PATH, key);
		
		int size = -1;
		
		try {
			
			size = Integer.parseInt(element);
			
		} catch(NumberFormatException exception) {
			
			//TODO : can not convert element into valid integer.
		}
		
		return size;
	}
	
	public final static Color getColor(String key) throws Exception {
		
		Color color = Color.white;
		
		String hexColor = XMLUtility.getElement(XMLUtility.COLORS_XML_PATH, key);
		
		if(hexColor != null) {

			Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
			Matcher matcher = pattern.matcher(hexColor);
			
			if(!matcher.matches()) {
				
				throw new Exception("Color hex format is not valid");
			}
			
			color = Color.decode(hexColor);
		}
		
		return color;
	}

	private final static String getElement(String filePath, String key) throws Exception {
		
		String value = null;
		
		try {
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
			File xmlFile = new File(filePath);
			
			Document document = documentBuilder.parse(xmlFile);
			
			Element resourcesElement = document.getDocumentElement();
			resourcesElement.normalize();
			
			NodeList strings = resourcesElement.getChildNodes();
			
			int stringsCount = strings.getLength();
			
			for(int i = 0; i < stringsCount; i++) {
				
				Node node = strings.item(i);
				
				short nodeType = node.getNodeType();
				
				if(nodeType == Node.ELEMENT_NODE) {
					
					Element element = (Element) node;
					
					String attribute = element.getAttribute("name");
					
					if(attribute.equals(key)) {
						
						value = element.getTextContent();
					}
				}
			}
			
		} catch(Exception ex) {
			
			throw ex;
		} 
		
		return value;
	}
}
