package com.yunnex.ops.erp.common.mapper.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MapConvertor")  
@XmlAccessorType(XmlAccessType.FIELD)  
public class MapConvertor {
    
    private List<MapEntry> entries = new ArrayList<MapEntry>();  
  
    public void addEntry(MapEntry entry) {  
        entries.add(entry);  
    }  
  
    public List<MapEntry> getEntries() {  
        return entries;  
    }  
      
    /**
     * 
     * @param entry
     */
    public static class MapEntry {  
  
        private String key;  
  
        private Object value;  
          
        public MapEntry() {  
            super();  
        }  
  
        /**
         * 
         * @param entry
         */
        public MapEntry(Map.Entry<String, Object> entry) {  
            super();  
            this.key = entry.getKey();  
            this.value = entry.getValue();  
        }  
  
        /**
         * 
         * @param entry
         */
        public MapEntry(String key, Object value) {  
            super();  
            this.key = key;  
            this.value = value;  
        }  
  
        public String getKey() {  
            return key;  
        }  
  
        /**
         * 
         * @param entry
         */
        public void setKey(String key) {  
            this.key = key;  
        }  
  
        /**
         * 
         * @param entry
         */
        public Object getValue() {  
            return value;  
        }  
  
        /**
         * 
         * @param entry
         */
        public void setValue(Object value) {  
            this.value = value;  
        }  
    }  
}  