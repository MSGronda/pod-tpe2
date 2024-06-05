package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.City;
import ar.edu.itba.pod.client.Query;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class ArgumentCollector {
    private static final List<String> OPTIONS = List.of(
            "addresses",
            "city",
            "inPath",
            "outPath",
            "n",
            "from",
            "to",
            "query"
    );

    public static Argument obtainArguments() {
        Argument.Builder builder = new Argument.Builder();
        StringBuilder errors = new StringBuilder();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (String option : OPTIONS) {
            String optionValue = System.getProperty(option);
            if (optionValue != null) {
                switch (option) {
                    case "addresses":
                        String[] addresses = optionValue.split(";");
                        if (addresses.length < 1) {
                            errors.append("At least one address must be provided\n");
                            break;
                        }
                        builder.addresses(addresses);
                        break;
                    case "city":
                        try {
                            builder.city(City.valueOf(optionValue));
                        } catch (IllegalArgumentException e) {
                            errors.append(String.format("Valid cities are %s\n", (Object) City.values()));
                            break;
                        }
                        break;
                    case "inPath":
                        Path inPath = Path.of(optionValue);
                        if (!Files.isDirectory(inPath)) {
                            errors.append("inPath is not a directory\n");
                            break;
                        }
                        builder.inPath(inPath);
                        break;
                    case "outPath":
                        Path outPath = Path.of(optionValue);
                        if (!Files.isDirectory(outPath)) {
                            errors.append("outPath is not a directory\n");
                            break;
                        }
                        builder.outPath(outPath);
                        break;
                    case "n":
                        int n = 0;
                        try {
                            n = Integer.parseInt(optionValue);
                            builder.n(n);
                        } catch (NumberFormatException e) {
                            errors.append("Invalid n value.");
                        }
                        break;
                    case "from":
                        try {
                            builder.from(LocalDate.parse(optionValue, dateTimeFormatter).atStartOfDay());
                        } catch (DateTimeParseException e) {
                            errors.append("from is not a date in a valid format\n");
                        }
                        break;
                    case "to":
                        try {
                            builder.to(LocalDate.parse(optionValue, dateTimeFormatter).atStartOfDay());
                        } catch (DateTimeParseException e) {
                            errors.append("to is not a date in a valid format\n");
                        }
                        break;
                    case "query":
                        try {
                            builder.query(Query.fromNum(Integer.parseInt(optionValue)));
                        } catch (Exception e) {
                            errors.append("Query num is not valid\n");
                        }
                        break;
                }
            }
        }

        Argument arguments = builder.build();

        arguments.getQuery().checkQueryArguments(arguments, errors);

        if (arguments.getInPath() == null) {
            errors.append("-DinPath is a required argument, and it must be a directory\n");
        }
        if (arguments.getOutPath() == null) {
            errors.append("-DoutPath is a required argument, and it must be a directory\n");
        }
        if (arguments.getCity() == null) {
            errors
                    .append("-Dcity=")
                    .append(Arrays.stream(City.values()).map(Enum::name))
                    .append(" is a required argument\n");
        }
        if (arguments.getAddresses() == null) {
            errors.append("-Daddresses is a required argument\n");
        }

        if (!errors.isEmpty()) {
            errors.insert(0, "\n");
            throw new IllegalArgumentException(errors.toString());
        }
        return arguments;
    }
}
