package ar.edu.itba.pod.client.Util;

import java.time.LocalDateTime;
import java.util.List;

public class ArgumentCollector {
    private static final List<String> OPTIONS = List.of(
            "addresses",
            "city",
            "inPath",
            "outPath",
            "n",
            "from",
            "to"
    );

    public static Argument obtainArguments() {
        Argument.Builder builder = new Argument.Builder();
        for (String option : OPTIONS) {
            String optionValue = System.getProperty(option);
            if (optionValue != null) {
                switch (option) {
                    case "addresses":
                        builder.addresses(optionValue.split(";"));
                        break;
                    case "city":
                        builder.city(optionValue);
                        break;
                    case "inPath":
                        builder.inPath(optionValue);
                        break;
                    case "outPath":
                        builder.outPath(optionValue);
                        break;
                    case "n":
                        int n = 0;
                        try {
                            n = Integer.parseInt(optionValue);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid n value.");
                        }
                        builder.n(n);
                        break;
                    case "from":
                        builder.from(LocalDateTime.parse(optionValue));
                        break;
                    case "to":
                        builder.to(LocalDateTime.parse(optionValue));
                        break;
                }
            }
        }
        return builder.build();
    }
}
