package com.miui.internal.initialization;

import com.miui.internal.variable.Android_App_AlertDialog_Builder_class;
import com.miui.internal.variable.Android_App_AlertDialog_class.Factory;
import com.miui.internal.variable.Android_App_ResourcesManager_class;
import com.miui.internal.variable.Android_Content_Res_AssetManager_class;
import com.miui.internal.variable.Android_Content_Res_ResourcesImpl_class;
import com.miui.internal.variable.Android_Preference_EditTextPreference_class;
import com.miui.internal.variable.Android_Preference_Preference_class;
import com.miui.internal.variable.Android_Text_Util_Linkify_class;
import com.miui.internal.variable.Android_View_ViewGroup_class;
import com.miui.internal.variable.Android_View_View_class;
import com.miui.internal.variable.Android_Widget_AbsSeekBar_class;
import com.miui.internal.variable.Android_Widget_AbsSpinner_class;
import com.miui.internal.variable.Android_Widget_GridView_class;
import com.miui.internal.variable.Android_Widget_ListPopupWindow_class;
import com.miui.internal.variable.Android_Widget_ListView_class;
import com.miui.internal.variable.Android_Widget_PopupWindow_class;
import com.miui.internal.variable.Android_Widget_Spinner_class;
import com.miui.internal.variable.Internal_Policy_Impl_PhoneWindow_class;
import miui.util.Log;

public class OverrideHelper {
    public static final void doInitialize() {
        Log.i("miuisdk", "initialize without hook");
        Factory.getInstance().get();
        Android_App_AlertDialog_Builder_class.Factory.getInstance().get();
        Android_App_ResourcesManager_class.Factory.getInstance().get();
        Android_Content_Res_AssetManager_class.Factory.getInstance().get();
        Android_Content_Res_ResourcesImpl_class.Factory.getInstance().get();
        Android_Preference_EditTextPreference_class.Factory.getInstance().get();
        Android_Preference_Preference_class.Factory.getInstance().get();
        Android_Text_Util_Linkify_class.Factory.getInstance().get();
        Android_View_View_class.Factory.getInstance().get();
        Android_View_ViewGroup_class.Factory.getInstance().get();
        Android_Widget_AbsSeekBar_class.Factory.getInstance().get();
        Android_Widget_AbsSpinner_class.Factory.getInstance().get();
        Android_Widget_GridView_class.Factory.getInstance().get();
        Android_Widget_ListPopupWindow_class.Factory.getInstance().get();
        Android_Widget_ListView_class.Factory.getInstance().get();
        Android_Widget_PopupWindow_class.Factory.getInstance().get();
        Android_Widget_Spinner_class.Factory.getInstance().get();
        Internal_Policy_Impl_PhoneWindow_class.Factory.getInstance().get();
    }
}
