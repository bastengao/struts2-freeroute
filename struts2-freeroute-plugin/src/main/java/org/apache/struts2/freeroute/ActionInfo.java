package org.apache.struts2.freeroute;

/**
 * 表示 action 信息
 * @author bastengao
 * @date 12-12-27 23:14
 */
class ActionInfo {

    private String namespace;
    private String actionName;

    ActionInfo(String namespace, String actionName) {
        this.namespace = namespace;
        this.actionName = actionName;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getActionName() {
        return actionName;
    }
}
