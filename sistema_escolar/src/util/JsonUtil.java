package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .create();

    public static void salvar(Object objeto, String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(objeto, writer);
            System.out.println("✅ Dados salvos em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public static <T> List<T> carregarLista(String caminhoArquivo, Type type) {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("Erro ao carregar lista: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
