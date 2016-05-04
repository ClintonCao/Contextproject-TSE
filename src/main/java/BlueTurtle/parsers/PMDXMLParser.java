package BlueTurtle.parsers;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import BlueTurtle.warnings.CheckStyleWarning;
import BlueTurtle.warnings.PMDWarning;
import BlueTurtle.warnings.Warning;

/**
 * This class can be used to parse a PMD XML output file.
 * 
 * @author BlueTurtle.
 *
 */
public class PMDXMLParser extends XMLParser {

	/**
	 * Parse a PMD report file.
	 * 
	 * @param xmlFilePath
	 *            the location of the PMD report.
	 * @return a list of PMD warnings.
	 */
	@Override
	public List<Warning> parseFile(String xmlFilePath) {
		// List to store the warnings.
		List<Warning> PMDWarnings = new LinkedList<Warning>();
		
		try {

			// Instantiate things that are necessary for the parser.
			File inputFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Parse the file.
			Document doc = dBuilder.parse(inputFile);

			// Normalize the elements of the document.
			doc.getDocumentElement().normalize();

			// Get all list of files where there are warnings.
			NodeList nList = doc.getElementsByTagName("file");

			for (int i = 0; i < nList.getLength(); i++) {
				// Get the file from the list.
				Node file = nList.item(i);

				if (file.getNodeType() == Node.ELEMENT_NODE) {
					// Convert the node to an element.
					Element fileElement = (Element) file;

					// Get the path of the file where the warning is from.
					String filePath = fileElement.getAttribute("name");

					// Get the name of the file where the warning is from.
					String fileName = Paths.get(filePath).getFileName().toString();

					// Get all the warnings.
					NodeList warningList = fileElement.getElementsByTagName("violation");

					for (int j = 0; j < warningList.getLength(); j++) {
						// Get the warning from the list of warnings.
						Node warning = warningList.item(j);

						if (warning.getNodeType() == Node.ELEMENT_NODE) {
							// Convert the node to an element.
							Element warningElement = (Element) warning;

							// packageName of warning
							String packageName = warningElement.getAttribute("package");

							// ruleSet of warning
							String ruleSet = warningElement.getAttribute("ruleset");
							
							// method of warning
							String method = warningElement.getAttribute("method");
							
							// line number where the warning is located.
							int line = Integer.parseInt(warningElement.getAttribute("beginline"));

							// Get the category of the warning.
							String ruleName = warningElement.getAttribute("rule");

							// Add warning to the list of warnings.
							PMDWarnings.add(new PMDWarning(filePath, fileName, line, packageName, ruleSet, method, ruleName));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PMDWarnings;
	}

}