package BlueTurtle.parsers;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for GDCParser.
 * 
 * @author BlueTurtle.
 *
 */
public class GDCParserTest {

	private static String testSet = "./resources/htmlExample.html";
	private static String testSet2 = "./resources/asat-gdc-mapping.html";


	/**
	 * Test that the parser can parse a valid GDC html file.
	 */
	@Test
	public void testParseCorrectBehaviour() {
		GDCParser parser = new GDCParser();

		HashMap<String, List<String>> categoryInfo = parser.parseFile(testSet);

		assertTrue(categoryInfo.containsKey("Naming Conventions"));
	}

	/**
	 * Test that the parser created the right number of categories.
	 */
	@Test
	public void testCreateRightNumberOfCategories() {
		GDCParser parser = new GDCParser();

		HashMap<String, List<String>> categoryInfo = parser.parseFile(testSet);

		assertSame(1, categoryInfo.size());
	}
	
	/**
	 * Test that the parser created the right amount of warnings.
	 */
	@Test
	public void testCreateRightAmountOfWarnings() {
		GDCParser parser = new GDCParser();

		HashMap<String, List<String>> categoryInfo = parser.parseFile(testSet);

		assertSame(2, categoryInfo.get("Naming Conventions").size());
	}	

//	/**
//	 * Test that the parser parse the wrong file.
//	 */
//	@Test
//	public void testParseTheWrongFile() {
//		FindBugsXMLParser parser = new FindBugsXMLParser();
//		
//		String testSet3 = "./resources/ex.xml";
//
//		List<Warning> warnings = parser.parseFile(testSet3);
//		
//		assertNotSame(6, warnings.toString());
//	}

}