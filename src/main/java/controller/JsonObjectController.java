package controller;

import com.gilecode.yagson.YaGson;

public class JsonObjectController<T> {
    private final Class<T> type;

    public JsonObjectController(Class<T> type) {
        this.type = type;
    }

        public T createJsonObject(String object){
            return new YaGson().fromJson(object,
                    type);;
        }

}
