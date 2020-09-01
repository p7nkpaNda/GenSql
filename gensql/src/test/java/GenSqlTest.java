import org.junit.Assert;
import org.junit.Test;

public class GenSqlTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_error(){
        InterpreterPatternGensql.gensql("())");
        InterpreterPatternGensql.gensql("() and (b=1)");
        InterpreterPatternGensql.gensql("() (b=1)");
    }
    @Test
    public void test_singlesql(){
        Assert.assertEquals("select * from apink where (a=2)",
                InterpreterPatternGensql.gensql("(a=2)"));
    }
    @Test
    public void test_and(){
        Assert.assertEquals("select * from apink where (a=2) and (b=1)",
                InterpreterPatternGensql.gensql("(a=2) and (b=1)"));
    }
    @Test
    public void test_or(){
        Assert.assertEquals("select * from apink where (a=2) or (b=1)",
                InterpreterPatternGensql.gensql("(a=2) or (b=1)"));
    }
    @Test
    public void test_not(){
        Assert.assertEquals("select * from apink where (a=2) or (!(b=1) and (c>4))",
                InterpreterPatternGensql.gensql("(a=2) or (!(b=1) and (c>4))"));
        Assert.assertEquals("select * from apink where !(!(b=1) and (c>4))",
                InterpreterPatternGensql.gensql("!(!(b=1) and (c>4))"));
    }
    @Test
    public void test_three(){
        Assert.assertEquals("select * from apink where (a=2) and ((b=1) or (c>4))",
                InterpreterPatternGensql.gensql("(a=2) and ((b=1) or (c>4))"));

        Assert.assertEquals("select * from apink where ((a=2) and (b=1)) or (c>4)",
                InterpreterPatternGensql.gensql("((a=2) and (b=1)) or (c>4)"));
    }

    @Test
    public void test_complexity(){
        Assert.assertEquals("select * from apink where (((a=1) or (b<3)) or (j=8)) and !(!(c=3) or (d=9))",
                InterpreterPatternGensql.gensql("(((a=1) or (b<3)) or (j=8)) and !(!(c=3) or (d=9))"));

    }



}
