import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("foo")
class JupiterTest {

  @Test
  void test() throws Exception {
    Thread.sleep(200);
  }
}
