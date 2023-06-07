package com.user.search.util;

import java.text.*;

public abstract class BaseSpecification {

    protected static String contains(String keyword){
        return MessageFormat.format("%{0}%", keyword);
    }
}
