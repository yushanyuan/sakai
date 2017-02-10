package cn.net.sikai.cc;

  
public class ParseException extends Exception {
  
  private static final long serialVersionUID = -698988229805566128L;

  private static final String DEFAULT_MESSAGE="Melete Zip Parsing Exception";
  
  public
  ParseException() {
    super(DEFAULT_MESSAGE);
  }
  
  public 
  ParseException(String the_message) {
    super(the_message);
  }
  
  public 
  ParseException(Throwable the_throwable) {
    super(DEFAULT_MESSAGE,
          the_throwable);
  }
  
  public 
  ParseException(String the_message,
                 Throwable the_throwable) {
    super(the_message,the_throwable);
  }
  
}
