package instr;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Leonardo
 */
public class Pair <K,V> implements Serializable{
    private K key;
    private V value;
    
    public Pair (K key, V value){
        this.key=key;
        this.value=value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.key);
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }
    
    @Override
    public String toString() {
        return "("+key.toString()+", "+value.toString()+")";
    }
}
