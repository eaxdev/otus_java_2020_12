package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileLoader implements Loader {

    private final  List<Measurement> measurements;

    // @formatter:off
    private static final Type LIST_TYPE = new TypeToken<List<Measurement>>(){}.getType();
    // @formatter:on

    public FileLoader(String fileName) throws FileProcessException {
        try {
            var resource = getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                throw new FileProcessException("File '" + fileName + "' is not found in resources");
            }
            var jsonContent = Files.readString(Path.of(resource.toURI()));
            this.measurements = new Gson().fromJson(jsonContent, LIST_TYPE);
        } catch (URISyntaxException | IOException e) {
            throw new FileProcessException(e);
        }
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        return measurements;
    }
}
