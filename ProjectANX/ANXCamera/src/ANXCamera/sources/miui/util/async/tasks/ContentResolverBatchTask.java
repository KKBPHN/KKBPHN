package miui.util.async.tasks;

import android.content.ContentProviderResult;
import java.util.ArrayList;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.async.Task;

public class ContentResolverBatchTask extends Task {
    private final String mAuthority;
    private String mDescription;
    private final ArrayList mOperations;

    public ContentResolverBatchTask(String str, ArrayList arrayList) {
        this.mAuthority = str;
        this.mOperations = arrayList;
    }

    public ContentProviderResult[] doLoad() {
        return AppConstants.getCurrentApplication().getContentResolver().applyBatch(this.mAuthority, this.mOperations);
    }

    public String getDescription() {
        if (this.mDescription == null) {
            StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
            sb.append('[');
            if (this.mOperations.size() == 0) {
                sb.append("NULL");
            } else {
                sb.append(this.mOperations.get(0));
                int size = this.mOperations.size();
                for (int i = 1; i < size; i++) {
                    sb.append("; ");
                    sb.append(this.mOperations.get(i));
                }
            }
            sb.append("]@");
            sb.append(this.mAuthority);
            this.mDescription = sb.toString();
            Pools.getStringBuilderPool().release(sb);
        }
        return this.mDescription;
    }
}
