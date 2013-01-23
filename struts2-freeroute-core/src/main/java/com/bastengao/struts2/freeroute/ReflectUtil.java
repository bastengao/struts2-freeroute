package com.bastengao.struts2.freeroute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author bastengao
 * @date 13-1-7 21:44
 * @since 1.0
 */
public class ReflectUtil {
    private ReflectUtil(){}

    /**
     * 测试某个字段上是否有指定的注解
     *
     * @param field
     * @param clazz
     * @return
     */
    public static boolean isAnnotationPresentOfField(Field field, Class<? extends Annotation> clazz) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType() == clazz) {
                return true;
            }
        }

        return false;
    }

    /**
     * 返回字段上某个注解, 如果没有返回 null
     *
     * @param field
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotationOfField(Field field, Class<T> clazz) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType() == clazz) {
                return clazz.cast(annotation);
            }
        }

        return null;
    }

    /**
     * 返回作用在类上的注解. 如果不存在返回 null
     *
     * @param clazz
     * @param annotation
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class clazz, Class<T> annotation){
        if(clazz.isAnnotationPresent(annotation)){
            Annotation value = clazz.getAnnotation(annotation);
            return annotation.cast(value);
        }

        return null;
    }
}
