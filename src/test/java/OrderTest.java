import org.junit.Test;

public class OrderTest {

    @Test
    public void nullConsumerSideShouldResultInException() {
        try {
            new Order(111, "AAPL", null, 10000);
        }
        catch (Exception e){
            return;
        }
        throw new RuntimeException("Should throw an exception, but it didn't");
    }

    @Test
    public void nullProductShouldResultInException() {
        try {
            new Order(111, null, Side.BUY, 10000);
        }
        catch (Exception e){
            return;
        }
        throw new RuntimeException("Should throw an exception, but it didn't");
    }

}
