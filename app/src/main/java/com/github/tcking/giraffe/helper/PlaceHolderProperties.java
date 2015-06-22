package com.github.tcking.giraffe.helper;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 支持placeHolder的Properties，例如：
 * app_home=/giraffe
 * cache_dir=${app_home}/cache
 * image_cache=${cache_dir}/imageCache
 * log_file=${app_home}/app.log
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/6/18.
 */
public class PlaceHolderProperties extends Properties {
    private static Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
    @Override
    public synchronized void load(Reader in) throws IOException {
        super.load(in);
    }

    @Override
    public String getProperty(String name) {
        String value = super.getProperty(name);
        if (value==null) {
            return null;
        }
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String group = matcher.group(1);
            Set<String> input=new HashSet<>();
            input.add(name);
            String replace = getPlaceHolderProperties(group, input);
            if (replace != null) {
                value = matcher.replaceFirst(replace);
                matcher.reset(value);
            } else {
                throw new RuntimeException("Can't parse placeHoler:" + name);
            }
        }
        return value;
    }

    private String getPlaceHolderProperties(String name, Set<String> input) {
        String value = super.getProperty(name);
        if (value==null) {
            return null;
        }
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            if (input.contains(name)) {
                throw new RuntimeException("circular reference :" + name);
            } else {
                input.add(name);
            }
            String group = matcher.group(1);
            String replace = getPlaceHolderProperties(group, input);
            if (replace != null) {
                value = matcher.replaceFirst(replace);
            }
        }
        return value;
    }

}
