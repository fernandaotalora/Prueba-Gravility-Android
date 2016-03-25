package adapter;

public class DataApplication{

    private String imageBase64;
    private String name;
    private String title;
    private String priceAumount;
    private String priceCurrency;
    private String rights;

    public DataApplication(String image,String name,String title,String priceAumount,String priceCurrency,String rights){
        this.imageBase64    = image;
        this.name           = name;
        this.title          = title;
        this.priceAumount   = priceAumount;
        this.priceCurrency  = priceCurrency;
        this.rights         = rights;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceAumount() {
        return priceAumount;
    }

    public void setPriceAumount(String priceAumount) {
        this.priceAumount = priceAumount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }
}