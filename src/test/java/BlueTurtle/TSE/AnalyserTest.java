package BlueTurtle.TSE;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import BlueTurtle.commandbuilders.CheckStyleCommandBuilder;
import BlueTurtle.commandbuilders.CoberturaCommandBuilder;
import BlueTurtle.commandbuilders.CommandBuilder;
import BlueTurtle.commandbuilders.PMDCommandBuilder;
import BlueTurtle.settings.CheckStyleSettings;
import BlueTurtle.settings.PMDSettings;

/**
 * Unit test for simple Analyser.
 */
public class AnalyserTest {
	
	@Before
	/**
	 * Set up a command to run PMD and run CheckStyle. These commands are handed to the analyser which runs them.
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		ArrayList<AnalyserCommand> commands = new ArrayList<AnalyserCommand>();
		CommandBuilder commandBuilder;
		PMDSettings pmdSettings = new PMDSettings();
		commandBuilder = new PMDCommandBuilder(pmdSettings);
		String[] pmdCommands = commandBuilder.buildCommand();
		AnalyserCommand c1 = new AnalyserCommand(pmdSettings.getDefaultOutputFilePath(), pmdCommands);
		commands.add(c1);

		CheckStyleSettings checkStyleSettings = new CheckStyleSettings(new File("CheckStyle_Settings.xml"));
		commandBuilder = new CheckStyleCommandBuilder(checkStyleSettings);
		String[] checkStyleCommands = commandBuilder.buildCommand();
		AnalyserCommand c2 = new AnalyserCommand(checkStyleSettings.getDefaultOutputFilePath(), checkStyleCommands);
		commands.add(c2);

		Analyser analyser = new Analyser(commands);

		analyser.analyse();
	}

	/**
	 * Simple test to check if running the analyser actually produces output for checkstyle.
	 * @throws IOException 
	 */
	@Test
	public void testCheckStyleOutput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(JavaController.getUserDir() + "/Runnables/Testcode/checkstyle.xml"));
		assert(br.readLine() != null);
	}
	
	/**
	 * Simple test to check if running the analyser actually produces output for PMD.
	 * @throws IOException 
	 */
	@Test
	public void testPMDOutput() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(JavaController.getUserDir() + "/Runnables/Testcode/pmd.xml"));
		assert(br.readLine() != null);
	}
}
