import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that contains methods to read a CSV file and a properties file.
 */
public class IOUtils {

    /***
     * Method that reads a CSV file and return a list of String arrays
     * @param csvFile: the path to the CSV file
     * @return: String[][]. Each String[] array represents elements in a single line in the CSV file
     */
    public static String[][] readCsv(String csvFile) {
        try {

            // checking number of lines in file
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            int numLines = 0;

            while (reader.readLine() != null) {
                numLines++;
            }
            reader.close();

            reader = new BufferedReader(new FileReader(csvFile));
            String[][] lines = new String[numLines][];
            String textRead;
            int lineIndex = 0;

            while ((textRead = reader.readLine()) != null) {
                String[] splitText = textRead.split(",");
                lines[lineIndex++] = splitText;
            }
            return lines;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    /***
     * Method that reads a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return: Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }
}