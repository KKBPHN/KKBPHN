package miui.date;

import android.content.res.Resources;
import com.miui.internal.R;
import java.util.Locale;
import miui.util.AppConstants;
import miui.util.SoftReferenceSingleton;

public class CalendarFormatSymbols {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public CalendarFormatSymbols createInstance() {
            return new CalendarFormatSymbols();
        }
    };
    private Resources mResources = AppConstants.getCurrentApplication().getResources();

    public static CalendarFormatSymbols getDefault() {
        return (CalendarFormatSymbols) INSTANCE.get();
    }

    public String[] getAmPms() {
        return this.mResources.getStringArray(R.array.am_pms);
    }

    public String[] getChineseDays() {
        return this.mResources.getStringArray(R.array.chinese_days);
    }

    public String[] getChineseDigits() {
        return this.mResources.getStringArray(R.array.chinese_digits);
    }

    public String[] getChineseLeapMonths() {
        return this.mResources.getStringArray(R.array.chinese_leap_months);
    }

    public String[] getChineseMonths() {
        return this.mResources.getStringArray(R.array.chinese_months);
    }

    public String[] getChineseSymbolAnimals() {
        return this.mResources.getStringArray(R.array.chinese_symbol_animals);
    }

    public String[] getDetailedAmPms() {
        return this.mResources.getStringArray(R.array.detailed_am_pms);
    }

    public String[] getEarthlyBranches() {
        return this.mResources.getStringArray(R.array.earthly_branches);
    }

    public String[] getEras() {
        return this.mResources.getStringArray(R.array.eras);
    }

    public String[] getHeavenlyStems() {
        return this.mResources.getStringArray(R.array.heavenly_stems);
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public String[] getMonths() {
        return this.mResources.getStringArray(R.array.months);
    }

    public String[] getShortMonths() {
        return this.mResources.getStringArray(R.array.months_short);
    }

    public String[] getShortWeekDays() {
        return this.mResources.getStringArray(R.array.week_days_short);
    }

    public String[] getShortestMonths() {
        return this.mResources.getStringArray(R.array.months_shortest);
    }

    public String[] getShortestWeekDays() {
        return this.mResources.getStringArray(R.array.week_days_shortest);
    }

    public String[] getSolarTerms() {
        return this.mResources.getStringArray(R.array.solar_terms);
    }

    public String[] getWeekDays() {
        return this.mResources.getStringArray(R.array.week_days);
    }
}
