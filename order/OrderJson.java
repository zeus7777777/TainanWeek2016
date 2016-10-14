package order;

public class OrderJson 
{
	String name, dpmt, stuid, content, note, fb, state, email;
	int id;
	public OrderJson(int id, String name, String dpmt, String stuid, String content, String note, String fb, 
			String state, String email)
	{
		this.id = id;
		this.name = name;
		this.dpmt = dpmt;
		this.stuid = stuid;
		this.content = content;
		this.note = note;
		this.fb = fb;
		this.state = state;
		this.email = email;
	}
}
