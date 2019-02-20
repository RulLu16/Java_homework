package hw2;

class Employee{
	private long id;
	private String name;
	private int age;
	
	Employee(int id, String name, int age)
	{
		this.id=id;
		this.name=name;
		this.age=age;
	}
	public void printinfo()
	{
		System.out.print(id+", "+name+", "+age);
	}
}

class Manager extends Employee{
	private String department;
	
	Manager(int id, String name, int age, String department)
	{
		super(id, name, age);
		this.department=department;
	}
	
	public void printinfo()
	{
		super.printinfo();
		System.out.print(", "+department);
	}
}

public class s20171666hw2 {
	public static void main(String arg[]) {
		Employee[] e={new Employee(1, "John", 27), new Employee(2, "Eujin", 25), new Employee(3, "Alex", 26), new Employee(4, "Jenny", 23), new Employee(5, "Tom", 25)};
		System.out.println("===Employee===");
		for(int i=0;i<5;i++) {
			System.out.print("[e00");
			e[i].printinfo();
			System.out.println("]");
		}
		Manager[] m= {new Manager(1, "Andy", 33, "Marketing"), new Manager(2, "Kate", 30, "Sales")};
		System.out.println("===Manager===");
		for(int i=0;i<2;i++)
		{
			System.out.print("[m00");
			m[i].printinfo();
			System.out.println("]");
		}
	}
}
