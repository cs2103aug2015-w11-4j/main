// TestIsId

import java.io.*;
import java.util.*;

public class TestIsId
{
    // Trying to edit something
    // Second 
  public static void main( String [ ] args )
  {
    Scanner scan = new Scanner(System.in);
    String s;
    while (scan.hasNext()) {  
      s = scan.next();
      if (isId(s)) {
        System.out.println(s + " is an identifier");
      } else {
        System.out.println(s + " is not an identifier");
      }
    }
    return;
  }
  
  public static boolean isId( String s )
  	{
	int len = s.length();
 	if (Character.isDigit(s.charAt(0)))
		return false;
	Character ch = s.charAt(len-1);
	String s2 = ch.toString();
	if (((s2.matches("^[a-zA-Z0-9_]*$")) == false))
		return false;
	else if (len > 1)
		return isId(s.substring(0,len-1));
	else
		return true;
	}
}
