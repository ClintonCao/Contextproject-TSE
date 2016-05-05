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

import BlueTurtle.warnings.FindBugsWarning;
import BlueTurtle.warnings.Warning;

/**
 * This class can be used to parse a FindBugs XML output file.
 * 
 * @author BlueTurtle.
 *
 */
public class FindBugsXMLParser extends XMLParser {

	/**
	 * Parse a FindBugs report file.
	 * 
	 * @param xmlFilePath
	 *            the location of the FindBugs report.
	 * @return a list of FindBugs warnings.
	 */
	@Override
	public List<Warning> parseFile(String xmlFilePath) {
		// List to store the warnings.
		List<Warning> findBugsWarnings = new LinkedList<Warning>();
		
		try {

			// Instantiate things that are necessary for the parser.
			File inputFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Parse the file.
			Document doc = dBuilder.parse(inputFile);

			// Normalize the elements of the document.
			doc.getDocumentElement().normalize();
			
			// Get the list of file pathes of the project.
			NodeList pathsList = doc.getElementsByTagName("Project");
			
			NodeList srcList = doc.getElementsByTagName("SrcDir");

			String pathFront = "";
			if (pathsList != null && pathsList.getLength() > 0) {
				Element pathElement = (Element) pathsList.item(0);
				srcList = pathElement.getElementsByTagName("SrcDir");
			}			

			// Get all list of files where there are warnings.
			NodeList nList = doc.getElementsByTagName("file");

			for (int i = 0; i < nList.getLength(); i++) {
				// Get the file from the list.
				Node file = nList.item(i);

				if (file.getNodeType() == Node.ELEMENT_NODE) {
					// Convert the node to an element.
					Element fileElement = (Element) file;

					// Get the class name where the warning is from.
					String className = fileElement.getAttribute("classname");
					
					// replace the . with \\ in the file name.
					className = className.replaceAll("\\.", "\\\\");
					
					// initially start with 0
					int k = 0;
					// With an empty file path
					String filePath = "";
					
					// continue go down the list of source path if the file does not exist.
					do {
						// get the source path from the node.
						pathFront = srcList.item(k).getTextContent(); 
						
						// concatenate the source path with the class name.
						filePath = pathFront + "\\" + className + ".java";
						
						// increment the counter
						k++;
						// check if the file exits or not.
					} while(!new File(filePath).exists());
					

					// Get the name of the file where the warning is from.
					String fileName = Paths.get(filePath).getFileName().toString();

					// Get all the warnings.
					NodeList warningList = fileElement.getElementsByTagName("BugInstance");

					for (int j = 0; j < warningList.getLength(); j++) {
						// Get the warning from the list of warnings.
						Node warning = warningList.item(j);

						if (warning.getNodeType() == Node.ELEMENT_NODE) {
							// Convert the node to an element.
							Element warningElement = (Element) warning;

							// message of warning
							String message = warningElement.getAttribute("message");

							// category of warning
							String category = warningElement.getAttribute("category");
							
							// priority of warning
							String priority = warningElement.getAttribute("priority");
							
							// line number where the warning is located.
							int line = Integer.parseInt(warningElement.getAttribute("lineNumber"));

							// Get the category of the warning.
							String ruleName = warningElement.getAttribute("type");

							// Add warning to the list of warnings.
							findBugsWarnings.add(new FindBugsWarning(filePath, fileName, line, message, category, priority, ruleName));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return findBugsWarnings;
	}

}