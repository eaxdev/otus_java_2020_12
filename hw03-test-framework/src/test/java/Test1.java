import metadata.After;
import metadata.Before;
import metadata.Test;
import org.assertj.core.api.Assertions;

public class Test1 {

    @Before
    void setUp() {
        System.out.println(this.getClass().getName() + "::" + this.hashCode() + "::setUp");
    }

    @Test
    void test1() {
        System.out.println(this.getClass().getName() + "::" + this.hashCode() + "::test1");
        Assertions.assertThat(2).isEqualTo(2);
    }

    @Test
    void test2() {
        System.out.println(this.getClass().getName() + "::" + this.hashCode() + "::test2");
        Assertions.assertThat("foo").isEqualTo("bar");
    }

    @After
    void tearDown() {
        System.out.println(this.getClass().getName() + "::" + this.hashCode() + "::tearDown");
    }

}
