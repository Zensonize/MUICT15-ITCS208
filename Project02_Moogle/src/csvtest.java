import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class csvtest {

	public static void main(String[] args) {
		String abs = "tag1|tag2|tag3";
		String[] a = abs.split("\\|");
		System.out.println(a[0] + " " + a[1] + " " + a[2]);

	}

}
