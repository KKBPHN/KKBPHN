package com.miui.internal.log.util;

import java.util.Formatter;
import java.util.Locale;

public class AppendableFormatter {
    private AppendableWrapper mAppendableWrapper;
    private Formatter mFormatter;

    class AppendableWrapper implements Appendable {
        private Appendable iAppendable;

        private AppendableWrapper() {
        }

        public Appendable append(char c) {
            this.iAppendable.append(c);
            return this;
        }

        public Appendable append(CharSequence charSequence) {
            this.iAppendable.append(charSequence);
            return this;
        }

        public Appendable append(CharSequence charSequence, int i, int i2) {
            this.iAppendable.append(charSequence, i, i2);
            return this;
        }

        public void setAppendable(Appendable appendable) {
            this.iAppendable = appendable;
        }
    }

    public AppendableFormatter() {
        this(Locale.US);
    }

    public AppendableFormatter(Locale locale) {
        this.mAppendableWrapper = new AppendableWrapper();
        this.mFormatter = new Formatter(this.mAppendableWrapper, locale);
    }

    public AppendableFormatter format(String str, Object... objArr) {
        this.mFormatter.format(str, objArr);
        return this;
    }

    public AppendableFormatter format(Locale locale, String str, Object... objArr) {
        this.mFormatter.format(locale, str, objArr);
        return this;
    }

    public void setAppendable(Appendable appendable) {
        this.mAppendableWrapper.setAppendable(appendable);
    }
}
