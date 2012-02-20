
public class MainNode implements Runnable {
	
	static Integer i = 1;
	
	public static void main(String[] args) {
		Thread t = new Thread(new MainNode());
		t.start();
		System.out.println("Hello tes123");
		i = 2;
		Thread p = new Thread(new MainNode());
		p.start();
	}
	
	MainNode() {
		
	}
	
	public void run() {
		if (i == 1) {
			System.out.println("Hello tes123 run");
			MultipleSocketServer obj = new MultipleSocketServer();
			obj.go();
		}
		if (i == 2) {
			System.out.println("Poo in my shoe");
		}
	}
}