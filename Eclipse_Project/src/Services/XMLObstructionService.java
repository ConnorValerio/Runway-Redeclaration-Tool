package Services;

import Controllers.Obstruction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XMLObstructionService {

	// need String because Obstruction class doesn't store obstruction name
	public void exportObstruction(Obstruction obstruction, String passedFilename) throws ParserConfigurationException, TransformerException, SAXException, IOException {

		if (passedFilename.equals("")) {

			// creates file name that will be used to store exported
			// obstructions
			String filename = this.getDatedFileName() + "_obstruction_exports.xml";
			File file = new File(filename);

			// checks to see if an obstruction exporting file already exists for
			// today
			if (file.exists() == true) {

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				// parses the xml document in
				Document xmlDoc = db.parse(file);

				// gets the root element in the parsed file
				Element root = xmlDoc.getDocumentElement();
				root.normalize();

				// Build XML elements and Text Nodes
				Element main = xmlDoc.createElement("obstruction");

				Element obname = xmlDoc.createElement("obstruction_name");
				Element obwidth = xmlDoc.createElement("obstruction_width");
				Element obheight = xmlDoc.createElement("obstruction_height");
				Element oblength = xmlDoc.createElement("obstruction_length");

				Text obnametext = xmlDoc.createTextNode(obstruction.getName());
				Text obwidthtext = xmlDoc.createTextNode(Double.toString(obstruction.getWidth()));
				Text obheighttext = xmlDoc.createTextNode(Double.toString(obstruction.getHeight()));
				Text oblengthtext = xmlDoc.createTextNode(Double.toString(obstruction.getLength()));

				obname.appendChild(obnametext);
				obwidth.appendChild(obwidthtext);
				obheight.appendChild(obheighttext);
				oblength.appendChild(oblengthtext);

				main.appendChild(obname);
				main.appendChild(obwidth);
				main.appendChild(obheight);
				main.appendChild(oblength);

				root.appendChild(main);

				// used to output the xml structure into a file
				TransformerFactory transformerfac = TransformerFactory.newInstance();
				Transformer transformer = transformerfac.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

				DOMSource source = new DOMSource(xmlDoc);
				StreamResult result = new StreamResult(file);
				transformer.transform(source, result);

			} else {

				// else called if the document doesn't already exist

				// creates a new document
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document xmlDoc = db.newDocument();

				// Build XML elements and Text Nodes
				Element root = xmlDoc.createElement("obstructions");
				Element main = xmlDoc.createElement("obstruction");

				Element obname = xmlDoc.createElement("obstruction_name");
				Element obwidth = xmlDoc.createElement("obstruction_width");
				Element obheight = xmlDoc.createElement("obstruction_height");
				Element oblength = xmlDoc.createElement("obstruction_length");

				Text obnametext = xmlDoc.createTextNode(obstruction.getName());
				Text obwidthtext = xmlDoc.createTextNode(Double.toString(obstruction.getWidth()));
				Text obheighttext = xmlDoc.createTextNode(Double.toString(obstruction.getHeight()));
				Text oblengthtext = xmlDoc.createTextNode(Double.toString(obstruction.getLength()));

				obname.appendChild(obnametext);
				obwidth.appendChild(obwidthtext);
				obheight.appendChild(obheighttext);
				oblength.appendChild(oblengthtext);

				main.appendChild(obname);
				main.appendChild(obwidth);
				main.appendChild(obheight);
				main.appendChild(oblength);

				root.appendChild(main);
				xmlDoc.appendChild(root);

				// used to output the xml structure into a file
				TransformerFactory transformerfac = TransformerFactory.newInstance();
				Transformer transformer = transformerfac.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

				DOMSource source = new DOMSource(xmlDoc);
				StreamResult result = new StreamResult(file);

				transformer.transform(source, result);

			}
		} else { // append the obstruction to the passedFilename.

			File file = new File(passedFilename);

			if (file.exists() == true) {

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				// parses the xml document in
				Document xmlDoc = db.parse(file);

				// gets the root element in the parsed file
				Element root = xmlDoc.getDocumentElement();
				root.normalize();

				// Build XML elements and Text Nodes
				Element main = xmlDoc.createElement("obstruction");

				Element obname = xmlDoc.createElement("obstruction_name");
				Element obwidth = xmlDoc.createElement("obstruction_width");
				Element obheight = xmlDoc.createElement("obstruction_height");
				Element oblength = xmlDoc.createElement("obstruction_length");

				Text obnametext = xmlDoc.createTextNode(obstruction.getName());
				Text obwidthtext = xmlDoc.createTextNode(Double.toString(obstruction.getWidth()));
				Text obheighttext = xmlDoc.createTextNode(Double.toString(obstruction.getHeight()));
				Text oblengthtext = xmlDoc.createTextNode(Double.toString(obstruction.getLength()));

				obname.appendChild(obnametext);
				obwidth.appendChild(obwidthtext);
				obheight.appendChild(obheighttext);
				oblength.appendChild(oblengthtext);

				main.appendChild(obname);
				main.appendChild(obwidth);
				main.appendChild(obheight);
				main.appendChild(oblength);

				root.appendChild(main);

				// used to output the xml structure into a file
				TransformerFactory transformerfac = TransformerFactory.newInstance();
				Transformer transformer = transformerfac.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

				DOMSource source = new DOMSource(xmlDoc);
				StreamResult result = new StreamResult(file);
				transformer.transform(source, result);
			}
		}
	}

	/* returns todays date in string form - used for date-specific import/export
	 * files */
	public String getDatedFileName() {

		// gets date in correct format
		SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String today = sdfdate.format(date);

		return today;
	}

	// Imports obstructions from a given file
	public ArrayList<Obstruction> importObstructions(String filename) throws ParserConfigurationException, SAXException, IOException {

		// validate the file being imported from
		// parse an XML document into a DOM tree
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(new File(filename));

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		StreamSource schemaFile = new StreamSource(new File("obstruction_XSD.xsd"));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an
		// instance document
		Validator validator = (Validator) schema.newValidator();

		// validate the DOM tree
		validator.validate(new DOMSource(document));

		// ArrayLists used in method
		ArrayList<Obstruction> obstructions = new ArrayList<Obstruction>();
		ArrayList<String> parseNames = new ArrayList<String>();
		ArrayList<String> parseWidth = new ArrayList<String>();
		ArrayList<String> parseHeight = new ArrayList<String>();
		ArrayList<String> parseLength = new ArrayList<String>();

		// Parsing XML file in
		File file = new File(filename);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// creating nodes for each obstruction
		NodeList nodeList = doc.getElementsByTagName("obstruction");

		// adds each nodes value to an array which is used to populate the
		// obstructions ArrayList later on
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node node = nodeList.item(j);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element elem = (Element) node;

				// getting name of obstruction
				NodeList nmeElementList = elem.getElementsByTagName("obstruction_name");
				Element nameElement = (Element) nmeElementList.item(0);
				NodeList name = nameElement.getChildNodes();
				parseNames.add(((Node) name.item(0)).getNodeValue());

				// getting width of obstruction
				NodeList widthElementList = elem.getElementsByTagName("obstruction_width");
				Element widthElement = (Element) widthElementList.item(0);
				NodeList width = widthElement.getChildNodes();
				parseWidth.add(((Node) width.item(0)).getNodeValue());

				// getting height of obstruction
				NodeList heightElementList = elem.getElementsByTagName("obstruction_height");
				Element heightElement = (Element) heightElementList.item(0);
				NodeList height = heightElement.getChildNodes();
				parseHeight.add(((Node) height.item(0)).getNodeValue());

				// getting length of obstruction
				NodeList lengthElementList = elem.getElementsByTagName("obstruction_length");
				Element lengthElement = (Element) lengthElementList.item(0);
				NodeList length = lengthElement.getChildNodes();
				parseLength.add(((Node) length.item(0)).getNodeValue());

			}
		}

		// creates obstructions based on the information that was read from the
		// file - dummy values were used for the values that weren't read in
		// from the file but are needed for the Obstruction constructor.

		for (int i = 0; i < nodeList.getLength(); i++) {

			String name = parseNames.get(i);
			Double width = Double.parseDouble(parseWidth.get(i));
			Double height = Double.parseDouble(parseHeight.get(i));
			Double length = Double.parseDouble(parseLength.get(i));
			Double dummy1 = 0.0;
			Double dummy2 = 0.0;

			obstructions.add(new Obstruction(name, width, height, length, dummy1, dummy2));

		}

		return obstructions;

	}

}
