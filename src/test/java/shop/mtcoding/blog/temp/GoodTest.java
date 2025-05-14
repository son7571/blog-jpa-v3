package shop.mtcoding.blog.temp;

import org.junit.jupiter.api.Test;

public class GoodTest {

    @Test
    public void good_test() {
        try {
            int n = 2;
            int sum = n / 0;
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void good_v2_test() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
