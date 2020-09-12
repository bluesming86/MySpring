package com.ming.ioc;

/**
 * @Author ming
 * @time 2020/9/8 20:26
 */
public class RuntimeBeanReference {

    //ref的属性值
    private String ref;

    public RuntimeBeanReference(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
