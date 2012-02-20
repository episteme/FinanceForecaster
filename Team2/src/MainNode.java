
public class MainNode implements Runnable {
	
	public static void main(String[] args) {
		Thread t = new Thread(new MainNode());
		t.start();
		System.out.println("End of main");
	}
	
	MainNode() {
		
	}
	
	public void run() {
		System.out.println("Start of run");
		MultipleSocketServer obj = new MultipleSocketServer();
		obj.go();
	}
}