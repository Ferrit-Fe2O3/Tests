import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;

public class Sorter {

    ArrayList<String> files = new ArrayList<>();

    private static final String TEMP_FILE = ".tmpFile";

    private static final String OUTPUT_FILE = "output.txt";

    private static final int TEMP_FILES_SIZE = 50;

    public File sortFile(File dataFile) throws IOException {
        File outputFile = new File(OUTPUT_FILE);
        splitFile(dataFile);
        if (files.size() == 1) { // Если входящий файл меньше буферного файла
            Files.move(Path.of(files.get(0)), Path.of(outputFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
        } else {
            for (int i = 1; i < files.size(); i++) {
                File tempFile = null;
                if (i == 1) {
                    tempFile = mergeSort(new File(files.get(0)), new File(files.get(1)));
                    Files.delete(Path.of(Integer.toString(0)));
                    Files.delete(Path.of(Integer.toString(1)));
                } else {
                    tempFile = mergeSort(outputFile, new File(files.get(i)));
                    Files.delete(Path.of(Integer.toString(i)));
                }
                try {
                    Files.move(Path.of(tempFile.toURI()), Path.of(outputFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputFile;
    }

    private File mergeSort(File file1, File file2) {
        try (FileReader fileReader1 = new FileReader(file1); BufferedReader reader1 = new BufferedReader(fileReader1);
             FileReader fileReader2 = new FileReader(file2); BufferedReader reader2 = new BufferedReader(fileReader2);
             FileWriter fileWriter = new FileWriter(TEMP_FILE); BufferedWriter writer = new BufferedWriter(fileWriter)) {

            String left = reader1.readLine();
            String right = reader2.readLine();
            while (true) {
                if (left == null) { // Дописываем хвост
                    while (right != null) {
                        writer.write(right + "\n");
                        writer.flush();
                        right = reader2.readLine();
                    }
                    break;
                }
                if (right == null) { // Дописываем хвост
                    while (left != null) {
                        writer.write(left + "\n");
                        writer.flush();
                        left = reader1.readLine();
                    }
                    break;
                }
                if (Long.parseLong(left) <= Long.parseLong(right)) {
                    writer.write(left+ "\n");
                    writer.flush();
                    left = reader1.readLine();
                } else {
                    writer.write(right + "\n");
                    writer.flush();
                    right = reader2.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(TEMP_FILE);
    }

    private void splitFile(File dataFile) {
        try (FileReader fileReader = new FileReader(dataFile); BufferedReader reader = new BufferedReader(fileReader)) {
            int count = 0;
            int tempName = 0;
            ArrayList<Long> buffer = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.add(Long.parseLong(line));
                count++;
                if (count == TEMP_FILES_SIZE) {
                    sortAndSaveTempFile(Integer.toString(tempName), buffer);
                    files.add(Integer.toString(tempName));
                    tempName++;
                    buffer.clear();
                    count = 0;
                }
            }
            if (!buffer.isEmpty()) {
                sortAndSaveTempFile(Integer.toString(tempName), buffer);
                files.add(Integer.toString(tempName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortAndSaveTempFile(String fileName, ArrayList<Long> data) {
        Collections.sort(data);
        try (FileWriter fileWriter = new FileWriter(fileName); BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for (Long l : data) {
                writer.write(l + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
