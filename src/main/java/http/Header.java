/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author leonardo
 */
public class Header {
    
    private final Map<String, List<String>> attributes;
    
    public Header() {
        this.attributes = new HashMap<>();
    }
    
    public void addAttribute(String key, String[] value) {
        addAttribute(key, Arrays.asList(value));
    }
    
    public void addAttribute(String key, List<String> values) {
        this.attributes.put(key, values);
    }
    
    public void addAttribute(String key, String value) {
        addAttribute(key, Arrays.asList(value));
    }
    
    public boolean containsProperty(String property) {
        return this.attributes.containsKey(property);
    }
    
    public List<String> getProperty(String property) {
        return this.attributes.get(property);
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }
    
    
    
}
