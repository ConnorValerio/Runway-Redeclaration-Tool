package Services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import Controllers.Runway;

import javax.xml.validation.Validator;

public class XMLRunwayService {

	public void exportRunway(Runway runway, String passedFilename) throws ParserConfigurationException, TransformerException, SAXException, IOException {

		if (passedFilename.equals("")) {

			// creates file name that will be used to store exported runways
			String filename = this.getDatedFileName() + "_runway_exports.xml";
			File file = new File(filename);

			// checks to see if a runway exporting file already exists for today
			if (file.exists() == true) {

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				// parses the xml document in
				Document xmlDoc = db.parse(file);

				// gets the root element in the parsed file
				Element root = xmlDoc.getDocumentElement();
				root.normalize();

				// Build XML elements and Text Nodes
				Element main = xmlDoc.createElement("runway");

				Element runName = xmlDoc.createElement("runway_name");
				Element runId = xmlDoc.createElement("runway_id");
				Element todaLeft = xmlDoc.createElement("toda_from_left");
				Element todaRight = xmlDoc.createElement("toda_from_right");
				Element tora = xmlDoc.createElement("tora");
				Element asdaLeft = xmlDoc.createElement("asda_from_left");
				Element asdaRight = xmlDoc.createElement("asda_from_right");
				Element ldaLeft = xmlDoc.createElement("lda_from_left");
				Element ldaRight = xmlDoc.createElement("lda_from_right");
				Element blastDistance = xmlDoc.createElement("take_off_plane_blast_distance");

				Text runNameText = xmlDoc.createTextNode(runway.getRunwayName());
				Text runIdText = xmlDoc.createTextNode(Integer.toString(runway.getRunwayID()));
				Text todaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().toda));
				Text todaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().toda));
				Text toraText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().tora));
				Text asdaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().asda));
				Text asdaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().asda));
				Text ldaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().lda));
				Text ldaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().lda));
				Text blastDistanceText = xmlDoc.createTextNode(Double.toString(runway.getBlastDistance()));

				runName.appendChild(runNameText);
				runId.appendChild(runIdText);
				todaLeft.appendChild(todaLeftText);
				todaRight.appendChild(todaRightText);
				tora.appendChild(toraText);
				asdaLeft.appendChild(asdaLeftText);
				asdaRight.appendChild(asdaRightText);
				ldaLeft.appendChild(ldaLeftText);
				ldaRight.appendChild(ldaRightText);
				blastDistance.appendChild(blastDistanceText);

				main.appendChild(runName);
				main.appendChild(runId);
				main.appendChild(todaLeft);
				main.appendChild(todaRight);
				main.appendChild(tora);
				main.appendChild(asdaLeft);
				main.appendChild(asdaRight);
				main.appendChild(ldaLeft);
				main.appendChild(ldaRight);
				main.appendChild(blastDistance);

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
				Element root = xmlDoc.createElement("runways");
				Element main = xmlDoc.createElement("runway");

				Element runName = xmlDoc.createElement("runway_name");
				Element runId = xmlDoc.createElement("runway_id");
				Element todaLeft = xmlDoc.createElement("toda_from_left");
				Element todaRight = xmlDoc.createElement("toda_from_right");
				Element tora = xmlDoc.createElement("tora");
				Element asdaLeft = xmlDoc.createElement("asda_from_left");
				Element asdaRight = xmlDoc.createElement("asda_from_right");
				Element ldaLeft = xmlDoc.createElement("lda_from_left");
				Element ldaRight = xmlDoc.createElement("lda_from_right");
				Element blastDistance = xmlDoc.createElement("take_off_plane_blast_distance");

				Text runNameText = xmlDoc.createTextNode(runway.getRunwayName());
				Text runIdText = xmlDoc.createTextNode(Integer.toString(runway.getRunwayID()));
				Text todaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().toda));
				Text todaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().toda));
				Text toraText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().tora));
				Text asdaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().asda));
				Text asdaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().asda));
				Text ldaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().lda));
				Text ldaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().lda));
				Text blastDistanceText = xmlDoc.createTextNode(Double.toString(runway.getBlastDistance()));

				runName.appendChild(runNameText);
				runId.appendChild(runIdText);
				todaLeft.appendChild(todaLeftText);
				todaRight.appendChild(todaRightText);
				tora.appendChild(toraText);
				asdaLeft.appendChild(asdaLeftText);
				asdaRight.appendChild(asdaRightText);
				ldaLeft.appendChild(ldaLeftText);
				ldaRight.appendChild(ldaRightText);
				blastDistance.appendChild(blastDistanceText);

				main.appendChild(runName);
				main.appendChild(runId);
				main.appendChild(todaLeft);
				main.appendChild(todaRight);
				main.appendChild(tora);
				main.appendChild(asdaLeft);
				main.appendChild(asdaRight);
				main.appendChild(ldaLeft);
				main.appendChild(ldaRight);
				main.appendChild(blastDistance);

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
		} else { // append the new runway to the passedFilename

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
				Element main = xmlDoc.createElement("runway");

				Element runName = xmlDoc.createElement("runway_name");
				Element runId = xmlDoc.createElement("runway_id");
				Element todaLeft = xmlDoc.createElement("toda_from_left");
				Element todaRight = xmlDoc.createElement("toda_from_right");
				Element tora = xmlDoc.createElement("tora");
				Element asdaLeft = xmlDoc.createElement("asda_from_left");
				Element asdaRight = xmlDoc.createElement("asda_from_right");
				Element ldaLeft = xmlDoc.createElement("lda_from_left");
				Element ldaRight = xmlDoc.createElement("lda_from_right");
				Element blastDistance = xmlDoc.createElement("take_off_plane_blast_distance");

				Text runNameText = xmlDoc.createTextNode(runway.getRunwayName());
				Text runIdText = xmlDoc.createTextNode(Integer.toString(runway.getRunwayID()));
				Text todaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().toda));
				Text todaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().toda));
				Text toraText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().tora));
				Text asdaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().asda));
				Text asdaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().asda));
				Text ldaLeftText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromLeft().lda));
				Text ldaRightText = xmlDoc.createTextNode(Double.toString(runway.getOriginalLogicalRunwayFromRight().lda));
				Text blastDistanceText = xmlDoc.createTextNode(Double.toString(runway.getBlastDistance()));

				runName.appendChild(runNameText);
				runId.appendChild(runIdText);
				todaLeft.appendChild(todaLeftText);
				todaRight.appendChild(todaRightText);
				tora.appendChild(toraText);
				asdaLeft.appendChild(asdaLeftText);
				asdaRight.appendChild(asdaRightText);
				ldaLeft.appendChild(ldaLeftText);
				ldaRight.appendChild(ldaRightText);
				blastDistance.appendChild(blastDistanceText);

				main.appendChild(runName);
				main.appendChild(runId);
				main.appendChild(todaLeft);
				main.appendChild(todaRight);
				main.appendChild(tora);
				main.appendChild(asdaLeft);
				main.appendChild(asdaRight);
				main.appendChild(ldaLeft);
				main.appendChild(ldaRight);
				main.appendChild(blastDistance);

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

	// Imports runways from a given file
	public ArrayList<Runway> importRunways(String filename) throws ParserConfigurationException, SAXException, IOException {

		// validate the file being imported from
		// parse an XML document into a DOM tree
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(new File(filename));

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		StreamSource schemaFile = new StreamSource(new File("runway_XSD.xsd"));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an
		// instance document
		Validator validator = (Validator) schema.newValidator();

		// validate the DOM tree
		validator.validate(new DOMSource(document));

		// ArrayLists used in method
		ArrayList<Runway> runways = new ArrayList<Runway>();
		ArrayList<String> parseRunNames = new ArrayList<String>();
		ArrayList<String> parseRunIDs = new ArrayList<String>();
		ArrayList<String> parseTodaLeft = new ArrayList<String>();
		ArrayList<String> parseTodaRight = new ArrayList<String>();
		ArrayList<String> parseTora = new ArrayList<String>();
		ArrayList<String> parseAsdaLeft = new ArrayList<String>();
		ArrayList<String> parseAsdaRight = new ArrayList<String>();
		ArrayList<String> parseLdaLeft = new ArrayList<String>();
		ArrayList<String> parseLdaRight = new ArrayList<String>();
		ArrayList<String> parseBlastDistance = new ArrayList<String>();

		// Parsing XML file in
		File file = new File(filename);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		// creating nodes for each obstacle
		NodeList nodeList = doc.getElementsByTagName("runway");

		// adds each nodes value to an array which is used to populate the
		// RunwayArea ArrayList later on
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node node = nodeList.item(j);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element elem = (Element) node;

				// getting name of runway
				NodeList nameElementList = elem.getElementsByTagName("runway_name");
				Element nameElement = (Element) nameElementList.item(0);
				NodeList name = nameElement.getChildNodes();
				parseRunNames.add(((Node) name.item(0)).getNodeValue());

				// getting ID of runway
				NodeList idElementList = elem.getElementsByTagName("runway_id");
				Element idElement = (Element) idElementList.item(0);
				NodeList id = idElement.getChildNodes();
				parseRunIDs.add(((Node) id.item(0)).getNodeValue());

				// getting runway left toda
				NodeList todaLeftElementList = elem.getElementsByTagName("toda_from_left");
				Element todaLeftElement = (Element) todaLeftElementList.item(0);
				NodeList todaLeft = todaLeftElement.getChildNodes();
				parseTodaLeft.add(((Node) todaLeft.item(0)).getNodeValue());

				// getting runway right toda
				NodeList todaRightElementList = elem.getElementsByTagName("toda_from_right");
				Element todaRightElement = (Element) todaRightElementList.item(0);
				NodeList todaRight = todaRightElement.getChildNodes();
				parseTodaRight.add(((Node) todaRight.item(0)).getNodeValue());

				// getting runway tora
				NodeList toraElementList = elem.getElementsByTagName("tora");
				Element toraElement = (Element) toraElementList.item(0);
				NodeList tora = toraElement.getChildNodes();
				parseTora.add(((Node) tora.item(0)).getNodeValue());

				// getting runway left asda
				NodeList asdaLeftElementList = elem.getElementsByTagName("asda_from_left");
				Element asdaLeftElement = (Element) asdaLeftElementList.item(0);
				NodeList asdaLeft = asdaLeftElement.getChildNodes();
				parseAsdaLeft.add(((Node) asdaLeft.item(0)).getNodeValue());

				// getting runway right asda
				NodeList asdaRightElementList = elem.getElementsByTagName("asda_from_right");
				Element asdaRightElement = (Element) asdaRightElementList.item(0);
				NodeList asdaRight = asdaRightElement.getChildNodes();
				parseAsdaRight.add(((Node) asdaRight.item(0)).getNodeValue());

				// getting runway left lda
				NodeList ldaLeftElementList = elem.getElementsByTagName("lda_from_left");
				Element ldaLeftElement = (Element) ldaLeftElementList.item(0);
				NodeList ldaLeft = ldaLeftElement.getChildNodes();
				parseLdaLeft.add(((Node) ldaLeft.item(0)).getNodeValue());

				// getting runway right lda
				NodeList ldaRightElementList = elem.getElementsByTagName("lda_from_right");
				Element ldaRightElement = (Element) ldaRightElementList.item(0);
				NodeList ldaRight = ldaRightElement.getChildNodes();
				parseLdaRight.add(((Node) ldaRight.item(0)).getNodeValue());

				// getting plane blast distance
				NodeList blastDistanceElementList = elem.getElementsByTagName("take_off_plane_blast_distance");
				Element blastDistanceElement = (Element) blastDistanceElementList.item(0);
				NodeList blastDistance = blastDistanceElement.getChildNodes();
				parseBlastDistance.add(((Node) blastDistance.item(0)).getNodeValue());

			}
		}

		// creates RunwayArea objects based on the information that was read
		// from the file

		for (int i = 0; i < nodeList.getLength(); i++) {

			String runName = parseRunNames.get(i);
			int runId = Integer.parseInt(parseRunIDs.get(i));
			Double todaLeft = Double.parseDouble(parseTodaLeft.get(i));
			Double todaRight = Double.parseDouble(parseTodaRight.get(i));
			Double tora = Double.parseDouble(parseTora.get(i));
			Double asdaLeft = Double.parseDouble(parseAsdaLeft.get(i));
			Double asdaRight = Double.parseDouble(parseAsdaRight.get(i));
			Double ldaLeft = Double.parseDouble(parseLdaLeft.get(i));
			Double ldaRight = Double.parseDouble(parseLdaRight.get(i));
			Double blastDistance = Double.parseDouble(parseBlastDistance.get(i));

			runways.add(new Runway(runName, runId, todaLeft, todaRight, tora, asdaLeft, asdaRight, ldaLeft, ldaRight, blastDistance));

		}

		return runways;

	}
}
