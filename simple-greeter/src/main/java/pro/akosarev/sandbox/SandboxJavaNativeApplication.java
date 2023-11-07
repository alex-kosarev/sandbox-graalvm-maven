package pro.akosarev.sandbox;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class SandboxJavaNativeApplication {

    public static void main(String[] args) {
        var name = Stream.of(args)
                .filter(Predicate.not(String::isBlank))
                .findFirst()
                .orElse("user");
        System.out.printf("Hello from Maven, %s!%n", name);
    }
}
