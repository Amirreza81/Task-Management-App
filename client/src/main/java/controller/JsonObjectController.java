package controller;

import com.gilecode.yagson.YaGson;

import java.lang.reflect.Type;

public class JsonObjectController<T> {
    private Class<T> type = null;
    private Type tYpe = null;

    public JsonObjectController(Class<T> type) {
        this.type = type;
    }

    public JsonObjectController(Type type) {
        this.tYpe = type;
    }

        public T createJsonObject(String object){
            return new YaGson().fromJson(object,
                    type);
        }
        public T createJsonObject2(String object){
        return new YaGson().fromJson(object,
                tYpe);
        }

}
