package com.sudarshan.mobileguard.database;

import androidx.room.TypeConverter;
import com.sudarshan.mobileguard.models.ScanResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromStringList(List<String> list) {
        return list == null ? null : gson.toJson(list);
    }

    @TypeConverter
    public static List<String> toStringList(String json) {
        if (json == null) return null;
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String fromRiskLevel(ScanResult.RiskLevel level) {
        return level == null ? null : level.name();
    }

    @TypeConverter
    public static ScanResult.RiskLevel toRiskLevel(String name) {
        return name == null ? null : ScanResult.RiskLevel.valueOf(name);
    }
}