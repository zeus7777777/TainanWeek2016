package product;

public class ProductJson
{
	public int id, gid, price, price2;
	public String name, description, image;
	public ProductJson(int id, int gid, String name, String description, String image, int price, int price2)
	{
		this.id = id;
		this.gid = gid;
		this.name = name;
		this.description = description;
		this.image = image;
		this.price = price;
		this.price2 = price2;
	}
}
