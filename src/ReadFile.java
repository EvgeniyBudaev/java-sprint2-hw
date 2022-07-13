import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFile {
    static String readFileContentsOrNull(String path, String name) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.printf(
                    "Невозможно прочитать файл с %s отчётом. Возможно, файл не находится в нужной директории.", name);
            return null;
        }
    }
}
