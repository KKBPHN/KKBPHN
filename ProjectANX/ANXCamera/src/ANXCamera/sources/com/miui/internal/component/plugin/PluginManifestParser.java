package com.miui.internal.component.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PluginManifestParser {
    protected static final String ATTR_GROUP = "group";
    protected static final String ATTR_LEVEL = "level";
    protected static final String ATTR_LOCATION = "location";
    protected static final String ATTR_MIN_CAPATIBLE_LEVEL = "minCapatibleLevel";
    protected static final String ATTR_MIN_LEVEL = "minLevel";
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_OPTIONAL = "optional";
    protected static final String ATTR_RESOURCES = "resources";
    protected static final String ATTR_TARGET_LEVEL = "targetLevel";
    protected static final String ATTR_VERSION_CODE = "versionCode";
    protected static final String ATTR_VERSION_NAME = "versionName";
    protected static final String ATTR_VISIBILITY = "visibility";
    protected static final String ELEMENT_ACTIVITY_EXTENSION = "activity-extension";
    protected static final String ELEMENT_ACTIVITY_OUTLET = "activity-outlet";
    protected static final String ELEMENT_DEPENDENCIES = "dependencies";
    protected static final String ELEMENT_DEPENDENCY = "dependency";
    protected static final String ELEMENT_EXTENSIONS = "extensions";
    protected static final String ELEMENT_FUNCTION_EXTENSION = "function-extension";
    protected static final String ELEMENT_FUNCTION_OUTLET = "function-outlet";
    protected static final String ELEMENT_OUTLETS = "outlets";
    protected static final String ELEMENT_PLUGIN = "plugin";

    private int getAttribute(Element element, String str, int i) {
        return Integer.parseInt(getAttribute(element, str, String.valueOf(i)));
    }

    private AccessPermission getAttribute(Element element, String str, AccessPermission accessPermission) {
        return AccessPermission.valueOf(getAttribute(element, str, accessPermission.name()).toUpperCase());
    }

    private Demand getAttribute(Element element, String str, Demand demand) {
        return Demand.valueOf(getAttribute(element, str, demand.name()).toUpperCase());
    }

    private String getAttribute(Element element, String str) {
        return getAttribute(element, str, true, null);
    }

    private String getAttribute(Element element, String str, String str2) {
        return getAttribute(element, str, false, str2);
    }

    private String getAttribute(Element element, String str, boolean z, String str2) {
        String attribute = element.getAttribute(str);
        if (attribute != null && !"".equals(attribute)) {
            return attribute;
        }
        if (!z) {
            return str2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("missing attribute ");
        sb.append(str);
        sb.append(" in element ");
        sb.append(element.getTagName());
        throw new PluginParseException(sb.toString());
    }

    private boolean getAttribute(Element element, String str, boolean z) {
        return Boolean.parseBoolean(getAttribute(element, str, String.valueOf(z)));
    }

    private void handleDependenciesElement(Element element, Plugin plugin) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            boolean z = true;
            if (item.getNodeType() == 1) {
                Element element2 = (Element) item;
                if (ELEMENT_DEPENDENCY.equals(element2.getTagName())) {
                    Dependency dependency = new Dependency();
                    dependency.setName(getAttribute(element2, ATTR_NAME));
                    if (getAttribute(element2, ATTR_RESOURCES, Demand.PROHIBITED) != Demand.REQUIRED) {
                        z = false;
                    }
                    dependency.setResourcesRequired(z);
                    dependency.setOptional(getAttribute(element2, ATTR_OPTIONAL, false));
                    dependency.setMinLevel(getAttribute(element2, ATTR_MIN_LEVEL, 0));
                    dependency.setTargetLevel(getAttribute(element2, ATTR_TARGET_LEVEL, 0));
                    plugin.addDependency(dependency);
                }
            }
        }
    }

    private void handleExtensionsElement(Element element, Plugin plugin) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == 1) {
                Element element2 = (Element) item;
                String tagName = element2.getTagName();
                if (ELEMENT_ACTIVITY_EXTENSION.equals(tagName) || ELEMENT_FUNCTION_EXTENSION.equals(tagName)) {
                    Extension extension = new Extension();
                    extension.setName(getAttribute(element2, ATTR_NAME));
                    extension.setLocation(getAttribute(element2, "location"));
                    plugin.addExtension(extension);
                }
            }
        }
    }

    private void handleOutletsElement(Element element, Plugin plugin) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == 1) {
                Element element2 = (Element) item;
                String tagName = element2.getTagName();
                if (ELEMENT_ACTIVITY_OUTLET.equals(tagName) || ELEMENT_FUNCTION_OUTLET.equals(tagName)) {
                    Outlet outlet = new Outlet();
                    outlet.setName(getAttribute(element2, ATTR_NAME));
                    outlet.setVisibility(getAttribute(element, ATTR_VISIBILITY, AccessPermission.PUBLIC));
                    plugin.addOutlet(outlet);
                }
            }
        }
    }

    private void handlePluginElement(Element element, Plugin plugin) {
        plugin.setName(getAttribute(element, ATTR_NAME));
        plugin.setGroup(getAttribute(element, ATTR_GROUP, plugin.getName()));
        plugin.setVersionCode(getAttribute(element, ATTR_VERSION_CODE, 0));
        plugin.setVersionName(getAttribute(element, ATTR_VERSION_NAME, String.valueOf(plugin.getVersionCode())));
        plugin.setLevel(getAttribute(element, "level", plugin.getVersionCode()));
        plugin.setMinCapatibleLevel(getAttribute(element, ATTR_MIN_CAPATIBLE_LEVEL, plugin.getLevel()));
        plugin.setResources(getAttribute(element, ATTR_RESOURCES, AccessPermission.PRIVATE));
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == 1) {
                Element element2 = (Element) item;
                String tagName = element2.getTagName();
                if (ELEMENT_DEPENDENCIES.equals(tagName)) {
                    handleDependenciesElement(element2, plugin);
                } else if (ELEMENT_EXTENSIONS.equals(tagName)) {
                    handleExtensionsElement(element2, plugin);
                } else if (ELEMENT_OUTLETS.equals(tagName)) {
                    handleOutletsElement(element2, plugin);
                }
            }
        }
    }

    public Plugin parsePlugin(File file) {
        return parsePlugin((InputStream) new FileInputStream(file));
    }

    public Plugin parsePlugin(InputStream inputStream) {
        Plugin plugin = new Plugin();
        try {
            handlePluginElement(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getDocumentElement(), plugin);
            return plugin;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new PluginParseException();
        } catch (SAXException e2) {
            e2.printStackTrace();
            throw new PluginParseException();
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new PluginParseException();
        }
    }
}
