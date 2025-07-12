package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
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

    public static <T> T carregarLista(String caminhoArquivo, Class<T> classe) {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            return gson.fromJson(reader, classe);
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo: " + e.getMessage());
            return null;
        }
    }
}
