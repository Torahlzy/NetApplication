package com.torahli.myapplication.hkbc.databean;

public class PageLink implements ILink {

    String name;
    String url;

    @Override
    public String getLink() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public static PageLink obtain(String name, String url) {
        PageLink page = new PageLink();
        page.name = name;
        page.url = url;
        return page;
    }

    @Override
    public String toString() {
        return "PageLink{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
