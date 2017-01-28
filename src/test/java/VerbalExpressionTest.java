/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.Knn;
import util.UtilKnn;

/**
 *
 * @author gonza
 */
public class VerbalExpressionTest {
    
    public VerbalExpressionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void test() {
         String v = "6 ,8 ,1.435 ; \n 12. 6,8 ,1;";
         v = v.replaceAll("\\s+", "");
         v = v.replaceAll("\\n+", "");
         System.out.println("Valor: " + v);
         String val = "6.4 ,8.2 ,1.435 ;   " + "12. 6,8 ,1    .8;\n" + "9,2,1;";
         
         String patron = "((\\d+\\.\\d+)+(,\\d+\\.\\d+)+)+;"; 
         String decimal = "((\\d+\\.\\d+)|\\d+,)+(\\d+\\.\\d+)|\\d+\\;"; 
         
        Pattern p = Pattern.compile(decimal);
        Matcher m = p.matcher(v);
        while (m.find()) {
          System.out.println(m.group());
         }
     }
}
