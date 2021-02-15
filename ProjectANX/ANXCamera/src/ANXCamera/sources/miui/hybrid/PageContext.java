package miui.hybrid;

public class PageContext {
    private String id;
    private String url;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && PageContext.class == obj.getClass()) {
            PageContext pageContext = (PageContext) obj;
            String str = this.id;
            String str2 = pageContext.id;
            if (str == str2) {
                return true;
            }
            return (str == null || str2 == null || !str.equals(str2)) ? false : true;
        }
    }

    public String getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public int hashCode() {
        String str = this.id;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
