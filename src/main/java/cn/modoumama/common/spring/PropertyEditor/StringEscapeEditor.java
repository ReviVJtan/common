package cn.modoumama.common.spring.PropertyEditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by kedong on 2017/3/6.
 * escapeHtml和escapeJavascript方法会把中文字符转换成Unicode编码
 * 防止XSS、SQL注入
 */
public class StringEscapeEditor extends PropertyEditorSupport {


    private boolean escapeHTML;

    private boolean escapeJavaScript;

    private boolean escapeSQL;

    public StringEscapeEditor() {
        super();
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript, boolean escapeSQL) {
        super();
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
        this.escapeSQL = escapeSQL;
    }

    @Override
    public void setAsText(String text) {

        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                value = htmlEncode(value);
            }

            if (escapeJavaScript) {
                value = StringEscapeUtils.escapeJavaScript(value);
            }

            if (escapeSQL) {
                value = StringEscapeUtils.escapeSql(value);
            }
            setValue(value);
        }

    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }


    private String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
