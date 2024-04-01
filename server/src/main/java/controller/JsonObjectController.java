package controller;

import com.gilecode.yagson.YaGson;

public class JsonObjectController<T> {

        public String createJsonObject(T object){
            return new YaGson().toJson(object);
        }

}
