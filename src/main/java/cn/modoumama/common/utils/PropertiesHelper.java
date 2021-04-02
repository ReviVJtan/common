/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package cn.modoumama.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesHelper {
	public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
	public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
	public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
	Properties p;
	private int systemPropertiesMode = 0;

	public PropertiesHelper(Properties p) {
		setProperties(p);
	}

	public PropertiesHelper(Properties p, int systemPropertiesMode) {
		setProperties(p);
		if ((systemPropertiesMode != 0) && (systemPropertiesMode != 1) && (systemPropertiesMode != 2)) {
			throw new IllegalArgumentException("error systemPropertiesMode mode:" + systemPropertiesMode);
		}
		this.systemPropertiesMode = systemPropertiesMode;
	}

	public Properties getProperties() {
		return this.p;
	}

	public void setProperties(Properties props) {
		if (props == null)
			throw new IllegalArgumentException("properties must be not null");
		this.p = props;
	}

	public String getRequiredString(String key) {
		String value = getProperty(key);
		if (isBlankString(value)) {
			throw new IllegalStateException("required property is blank by key=" + key);
		}
		return value;
	}

	public String getNullIfBlank(String key) {
		String value = getProperty(key);
		if (isBlankString(value)) {
			return null;
		}
		return value;
	}

	public String getNullIfEmpty(String key) {
		String value = getProperty(key);
		if ((value == null) || ("".equals(value))) {
			return null;
		}
		return value;
	}

	public String getAndTryFromSystem(String key) {
		String value = getProperty(key);
		if (isBlankString(value)) {
			value = getSystemProperty(key);
		}
		return value;
	}

	private String getSystemProperty(String key) {
		String value = System.getProperty(key);
		if (isBlankString(value)) {
			value = System.getenv(key);
		}
		return value;
	}

	public Integer getInteger(String key) {
		String v = getProperty(key);
		if (v == null) {
			return null;
		}
		return Integer.valueOf(Integer.parseInt(v));
	}

	public int getInt(String key, int defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Integer.parseInt(getRequiredString(key));
	}

	public int getRequiredInt(String key) {
		return Integer.parseInt(getRequiredString(key));
	}

	public Long getLong(String key) {
		if (getProperty(key) == null) {
			return null;
		}
		return Long.valueOf(Long.parseLong(getRequiredString(key)));
	}

	public long getLong(String key, long defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Long.parseLong(getRequiredString(key));
	}

	public Long getRequiredLong(String key) {
		return Long.valueOf(Long.parseLong(getRequiredString(key)));
	}

	public Boolean getBoolean(String key) {
		if (getProperty(key) == null) {
			return null;
		}
		return Boolean.valueOf(Boolean.parseBoolean(getRequiredString(key)));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(getRequiredString(key));
	}

	public boolean getRequiredBoolean(String key) {
		return Boolean.parseBoolean(getRequiredString(key));
	}

	public Float getFloat(String key) {
		if (getProperty(key) == null) {
			return null;
		}
		return Float.valueOf(Float.parseFloat(getRequiredString(key)));
	}

	public float getFloat(String key, float defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Float.parseFloat(getRequiredString(key));
	}

	public Float getRequiredFloat(String key) {
		return Float.valueOf(Float.parseFloat(getRequiredString(key)));
	}

	public Double getDouble(String key) {
		if (getProperty(key) == null) {
			return null;
		}
		return Double.valueOf(Double.parseDouble(getRequiredString(key)));
	}

	public double getDouble(String key, double defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Double.parseDouble(getRequiredString(key));
	}

	public Double getRequiredDouble(String key) {
		return Double.valueOf(Double.parseDouble(getRequiredString(key)));
	}

	public Object setProperty(String key, int value) {
		return setProperty(key, String.valueOf(value));
	}

	public Object setProperty(String key, long value) {
		return setProperty(key, String.valueOf(value));
	}

	public Object setProperty(String key, float value) {
		return setProperty(key, String.valueOf(value));
	}

	public Object setProperty(String key, double value) {
		return setProperty(key, String.valueOf(value));
	}

	public Object setProperty(String key, boolean value) {
		return setProperty(key, String.valueOf(value));
	}

	public String getProperty(String key, String defaultValue) {
		return this.p.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		String propVal = null;
		if (this.systemPropertiesMode == 2) {
			propVal = getSystemProperty(key);
		}
		if (propVal == null) {
			propVal = this.p.getProperty(key);
		}
		if ((propVal == null) && (this.systemPropertiesMode == 1)) {
			propVal = getSystemProperty(key);
		}
		return propVal;
	}

	public Object setProperty(String key, String value) {
		return this.p.setProperty(key, value);
	}

	public void clear() {
		this.p.clear();
	}

	public Set<Map.Entry<Object, Object>> entrySet() {
		return this.p.entrySet();
	}

	public Enumeration<?> propertyNames() {
		return this.p.propertyNames();
	}

	public boolean contains(Object value) {
		return this.p.contains(value);
	}

	public boolean containsKey(Object key) {
		return this.p.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.p.containsValue(value);
	}

	public Enumeration<Object> elements() {
		return this.p.elements();
	}

	public Object get(Object key) {
		return this.p.get(key);
	}

	public boolean isEmpty() {
		return this.p.isEmpty();
	}

	public Enumeration<Object> keys() {
		return this.p.keys();
	}

	public Set<Object> keySet() {
		return this.p.keySet();
	}

	public void list(PrintStream out) {
		this.p.list(out);
	}

	public void list(PrintWriter out) {
		this.p.list(out);
	}

	public void load(InputStream inStream) throws IOException {
		this.p.load(inStream);
	}

	public void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		this.p.loadFromXML(in);
	}

	public Object put(Object key, Object value) {
		return this.p.put(key, value);
	}

	public void putAll(Map<? extends Object, ? extends Object> t) {
		this.p.putAll(t);
	}

	public Object remove(Object key) {
		return this.p.remove(key);
	}

	public int size() {
		return this.p.size();
	}

	public void store(OutputStream out, String comments) throws IOException {
		this.p.store(out, comments);
	}

	public void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
		this.p.storeToXML(os, comment, encoding);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		this.p.storeToXML(os, comment);
	}

	public Collection<Object> values() {
		return this.p.values();
	}

	public String toString() {
		return this.p.toString();
	}

	private static boolean isBlankString(String value) {
		return ((value == null) || ("".equals(value.trim())));
	}
}