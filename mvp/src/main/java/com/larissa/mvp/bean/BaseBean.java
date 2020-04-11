package com.larissa.mvp.bean;

import android.util.Log;

import androidx.databinding.BaseObservable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseBean extends BaseObservable {
    /**
     * 将bean的所有参数转为map
     * @return
     */
    public Map<String,String> getParams() {
        Class<? extends BaseBean> clazz = this.getClass();
        Class<? extends Object> superClass = clazz.getSuperclass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = superClass.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return null;
        }

        Map<String,String> params = new HashMap<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null) {
                    ParamNames paramsName = field.getAnnotation(ParamNames.class);
                    String key;
                    if (paramsName == null) {
                        key = field.getName();
                    } else {
                        key = paramsName.value();
                    }
                    params.put(key, String.valueOf(field.get(this)));
                }
            }

            for (Field superField : superFields) {
                superField.setAccessible(true);
                if (superField.get(this) != null) {
                    ParamNames superParamsName = superField.getAnnotation(ParamNames.class);
                    String superKey;
                    if (superParamsName == null) {
                        superKey = superField.getName();
                    } else {
                        superKey = superParamsName.value();
                    }
                    params.put(superKey, String.valueOf(superField.get(this)));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i(getClass().getCanonicalName(),"BaseParams:toMap:IllegalAccessException: " + e.toString());
        }

        return params;
    }

    @Override
    public String toString() {
        return "BaseBean{}";
    }
}
