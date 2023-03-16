package com.itexc.dom.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ContextHolder sert a passer des informations de context entre les objets.
 * Utilisable dans le cas ou le developpeur ne souhaite pas surcharger les
 * paramétres des methodes pour passer des informations. ContextHolder
 * fonctionne uniquement si l'invocation est executée dans un seul thread
 */
public class ContextHolder {


    private static final ThreadLocal<Map<String, Object>> THREAD_WITH_CONTEXT = new ThreadLocal<>();


    private ContextHolder() {
    }


    public static void put(String key, Object payload) {
        if (THREAD_WITH_CONTEXT.get() == null) {
            THREAD_WITH_CONTEXT.set(new HashMap<>());
        }
        THREAD_WITH_CONTEXT.get().put(key, payload);
    }


    public static Object get(String key) {


        if (THREAD_WITH_CONTEXT.get() == null) {


            return null;
        }
        return THREAD_WITH_CONTEXT.get().get(key);
    }


    public static void cleanUp() {
        THREAD_WITH_CONTEXT.remove();
    }
}
