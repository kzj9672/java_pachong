package top.sellet.entity;

/**
 * @author mo
 */
public class ImageWeb {
    /**
     * 网站名称
     */
    private String name;
    /**
     * 网站地址
     */
    private String address;

    public ImageWeb() {
    }

    public ImageWeb(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ImageWeb{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
